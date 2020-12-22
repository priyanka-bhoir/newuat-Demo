package com.priyanka.newuat_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.priyanka.newuat_demo.Database.Databasehelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    TextInputEditText unametxt, passwordtxt,urltxt;
    Button button;
    String name,pass,url;
    RequestQueue queue;
    SharedPrefrence prefrence;
    Intent i;
    ProgressDialog progressBar;
    Databasehelper databasehelper;
    JsonObjectRequest objectRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unametxt=findViewById(R.id.edittxt_name);
        passwordtxt=findViewById(R.id.edittxt_pass);
        urltxt=findViewById(R.id.edittxt_url);
        button=findViewById(R.id.button);
        prefrence=new SharedPrefrence(getApplicationContext());
        databasehelper=new Databasehelper(getApplicationContext());

        //progressbar
        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading...........");
//        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressBar.setProgress(0);
//        progressBar.setMax(100);

//        Log.e("TAG", "onCreate: prefrence"+ prefrence.getUname());
        i=new Intent(MainActivity.this,drawer.class);
//        startActivity(i);
        if (prefrence.getUname()!=null){
            unametxt.setText(prefrence.getUname());
            passwordtxt.setText(prefrence.getPassword());
            urltxt.setText(prefrence.getURl());
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //sets the maximum value 100
                // display

                name=unametxt.getText().toString();
                pass=passwordtxt.getText().toString();
                url=urltxt.getText().toString();

                if(name.isEmpty()){
                    unametxt.setError("Empty");
                    unametxt.setFocusable(true);
                }else if (pass.isEmpty()){
                    passwordtxt.setError("Empty");
                    passwordtxt.setFocusable(true);
                }
                else if (!Patterns.WEB_URL.matcher((urltxt.getText().toString())).matches()){
                    urltxt.setError("Invalid");
                    urltxt.setFocusable(true);
                }else {
                prefrence.setPassword(pass);
                prefrence.setUname(name);
                prefrence.setURl(url);
                Log.e("TAG", "onClick: "+url );
                if (databasehelper.getLogin()==false) {
                    objectRequest=fetchlogin(name, pass, url);
                    queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(objectRequest);

                }
                i=new Intent(MainActivity.this,drawer.class);
                startActivity(i);
                progressBar.show();
            }}
        });

    }

    private JsonObjectRequest fetchlogin(String unametxt, String pass, String url) {

        Log.e("TAG", "fetchlogin: "+url );

        String urlstr=url+"/api/v1/login";
        Log.e("TAG", "fetchlogin: "+urlstr );
       JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, urlstr, null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {

                       try {
                           JSONObject object = response.getJSONObject("data");

                           String a=object.getString("token");
                           String id=object.getString("id");
                           Log.e("TAG", "onResponse: "+ a);
                           i.putExtra("token",a);
                           startActivity(i);
                           prefrence.setToken(a);
                           prefrence.setId(id);
                           databasehelper.insertLogin(object.toString());

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.e("TAG", "onErrorResponse: "+error );
           }
       }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
//                return super.getHeaders();
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
//                return super.getBody();
                JSONObject object= new JSONObject();
                try {
                    object.put("user_name",unametxt);
                    object.put("password",pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return object.toString().getBytes();

            }


        };
       objectRequest.setRetryPolicy(new DefaultRetryPolicy(
               6000*3,
               DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
               DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return objectRequest;
    }
}