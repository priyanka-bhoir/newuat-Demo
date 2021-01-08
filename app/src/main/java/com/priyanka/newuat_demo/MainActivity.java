package com.priyanka.newuat_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.developer.dk.toastmessage.ToasterMsg;
import com.google.android.material.textfield.TextInputEditText;
import com.priyanka.newuat_demo.Database.Databasehelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    TextInputEditText unametxt, passwordtxt,urltxt;
    Button button;
    String name,pass,url;
    SharedPrefrence prefrence;
    Intent i;
    ProgressDialog progressBar;
    Databasehelper databasehelper;
    private CheckBox checkBox;
    private SharedPreferences.Editor prefeditor;
    String status;
    private String TAG="MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unametxt=findViewById(R.id.edittxt_name);
        passwordtxt=findViewById(R.id.edittxt_pass);
        urltxt=findViewById(R.id.edittxt_url);
        button=findViewById(R.id.button);
        checkBox=findViewById(R.id.checkBox);

//        startActivity(new Inte~nt(this,drawer.class));

        prefrence=new SharedPrefrence(getApplicationContext());
        databasehelper=new Databasehelper(getApplicationContext());
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//        prefeditor= (SharedPreferences.Editor) getSharedPreferences("loginPrefs",MODE_PRIVATE);
        prefeditor = loginPreferences.edit();

        boolean saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin) {
            unametxt.setText(loginPreferences.getString("username", ""));
            passwordtxt.setText(loginPreferences.getString("password", ""));
            urltxt.setText(loginPreferences.getString("url",""));
            checkBox.setChecked(true);
        }
        //progressbar
        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading...........");
//        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressBar.setProgress(0);
//        progressBar.setMax(100);

//        Log.e("TAG", "onCreate: prefrence"+ prefrence.getUname());
        i=new Intent(MainActivity.this,drawer.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //sets the maximum value 100
                // display

                name= unametxt.getText().toString();
                pass= passwordtxt.getText().toString();
                url= urltxt.getText().toString();

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
                    if (checkBox.isChecked()) {
                        prefeditor.putBoolean("saveLogin", true);
                        prefeditor.putString("username", name);
                        prefeditor.putString("password", pass);
                        prefeditor.putString("url",url);
                        prefrence.setPassword(pass);
                        prefrence.setUname(name);
                        prefrence.setURl(url);
                        prefeditor.apply();
                    } else {
                        prefeditor.clear();
                        prefeditor.commit();
                    }

                Log.e("TAG", "onClick: in "+url );
//                if (databasehelper.getLogin()==false) {
                    fetchlogin(name, pass, url);

//                }


                }
            }
        });

    }

    private void fetchlogin(String unametxt, String pass, String url) {

        Log.e("TAG", "fetchlogin: "+url );
        progressBar.show();
        String urlstr=url+"/api/v1/login";
        RequestQueue queue;
        Log.e("TAG", "fetchlogin: "+urlstr );
       JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, urlstr, null,
               response -> {
                    try {
                       JSONObject object = response.getJSONObject("data");
                       status=response.getString("status");
                       String a=object.getString("token");
                       String id=object.getString("id");
                       Log.e("TAG", "onResponse: "+ a);
                       i.putExtra("token",a);
//                       startActivity(i);
                       prefrence.setToken(a);
                       prefrence.setId(id);
                       databasehelper.insertLogin(object.toString());
                        if (status.equals(200)){
                            Log.e(TAG, "fetchlogin: " );
                            i=new Intent(MainActivity.this,drawer.class);
//                            if (!MainActivity.this.isFinishing()) {
//                                progressBar.show();
//                            }
                            startActivity(i);
                        }else {
                            Log.e(TAG, "fetchlogin:");
                            progressBar.dismiss();
                            ToasterMsg.showMessage(getApplicationContext(),"LoginError");
                        }

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }, error -> Log.e("TAG", "onErrorResponse: "+error )) {

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
        queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(objectRequest);
    }
}