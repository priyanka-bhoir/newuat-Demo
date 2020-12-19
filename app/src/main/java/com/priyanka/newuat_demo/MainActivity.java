package com.priyanka.newuat_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unametxt=findViewById(R.id.edittxt_name);
        passwordtxt=findViewById(R.id.edittxt_pass);
        urltxt=findViewById(R.id.edittxt_url);
        button=findViewById(R.id.button);
        prefrence=new SharedPrefrence(getApplicationContext());

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
        queue= Volley.newRequestQueue(this);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //sets the maximum value 100
                // display

                name=unametxt.getText().toString();
                pass=passwordtxt.getText().toString();
                url=urltxt.getText().toString();
                prefrence.setPassword(pass);
                prefrence.setUname(name);
                prefrence.setURl(url);
                Log.e("TAG", "onClick: "+url );
                fetchlogin(name,pass,url);
//                progressBar=new ProgressDialog(getApplicationContext());
//                progressBar.setCancelable(true);//you can cancel it by pressing back button
//                progressBar.setMessage("File downloading ...");
//                progressBar.setProgressStyle(ProgressDialog.);
//                progressBar.setProgress(0);//initially progress is 0
//                progressBar.setMax(100);
                progressBar.show();
            }
        });

    }

    private void fetchlogin(String unametxt, String pass, String url) {

        Log.e("TAG", "fetchlogin: "+url );

        String urlstr=url+"/api/v1/login";
       JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, urlstr, null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {

                       try {
                           JSONObject object = response.getJSONObject("data");

                           String a=object.getString("token");
                           Log.e("TAG", "onResponse: "+ a);
                           i=new Intent(MainActivity.this,drawer.class);
                           i.putExtra("token",a);
                           startActivity(i);
                           prefrence.setToken(a);
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
        queue.add(objectRequest);
    }
}