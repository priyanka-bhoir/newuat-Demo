package com.priyanka.newuat_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Account_Detail extends AppCompatActivity {
    String id;
    Intent i;
    private String TAG="Account_Detail";
    final String version="/api/v1/";
    final String URL_DETAIL="getentry-detail";
    final String URL_SUB_LAYOUT="sub-layout";
    SharedPrefrence prefrence;
    RequestQueue queue;
    String auth;
    String module;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__detail);
        i=getIntent();
        prefrence=new SharedPrefrence(getApplicationContext());
        auth=prefrence.getToken();
        id=i.getStringExtra("id");
        queue = Volley.newRequestQueue(getApplicationContext());
        module=i.getStringExtra("module_name");
        tabLayout=findViewById(R.id.tab);
        Log.e(TAG, "onCreate: "+id );
        String url=prefrence.getURl();
        Log.e(TAG, "onCreate: "+url );
        detailtabrequest(url+version+URL_DETAIL);
        relatetabreqest(url+version+URL_SUB_LAYOUT);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG, "onTabSelected: "+tab.getPosition() );
                if (tab.getPosition()==0){
                    //send detail request

                }
                else {
                    //send Related

                }            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void detailtabrequest(String url) {
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,url,null,response -> {
            try {
                JSONObject object=response.getJSONObject("entry_list");
                String modulename=object.getString("module_name");
                JSONObject name_value_list=object.getJSONObject("name_value_list");
                JSONArray relationship_list=object.getJSONArray("relationship_list");
                JSONObject multi_fields=object.getJSONObject("multi_fields");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> {

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }

            @Override
            public byte[] getBody() {
                JSONObject object=new JSONObject();
                JSONObject object1=new JSONObject();
                try {
                    object1.put("action","show");
                    object1.put("module_name",module);
                    object1.put("id",id);
                    object1.put("select_fields",selectedfileds(module));
                    object1.put("select_relate_fields","[]");
                    object.put("rest_data",object1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return object.toString().getBytes();
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 100,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private JSONArray selectedfileds(String module) {
        return null;
    }

    private void relatetabreqest(String url) {
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,url,null,response -> {

        },error -> {

        }){};
    }
}