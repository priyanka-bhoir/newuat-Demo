package com.priyanka.newuat_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.singletone.variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RelateFieldSelection extends AppCompatActivity {

    String module;
    private String TAG="RelateFieldSelection";
    Databasehelper databasehelper;
    SharedPrefrence prefrence;
    String url;
    String auth;
    String mParam1;
    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relate_field_selection);

        //intent
        Intent intent=getIntent();
        module=intent.getStringExtra("module");
        Log.e(TAG, "onCreate: "+module);

        //DataClass initialization
        databasehelper=new Databasehelper(getApplicationContext());


        mParam1=databasehelper.getFrontEndname(module);

        prefrence=new SharedPrefrence(getApplicationContext());
        url=prefrence.getURl()+ variables.version+variables.URL_GETENTRY_LIST;
        auth=variables.BEARER+prefrence.getToken();
        databasehelper=new Databasehelper(getApplicationContext());

        Request(module);

    }
    void Request(String module){
        Log.e(TAG, "Request: this is your url"+url );
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: "+response );
                try {
                    JSONArray jsonArray=response.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error );
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
//                return super.getHeaders();
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", auth);
                Log.e("parthi","header---->"+headers);
                return headers;
            }
            @Override
            public byte[] getBody() {
                JSONObject object = new JSONObject();
                try {
                    JSONObject object1 = new JSONObject();
                    object1.put("module_name", module);
                    object1.put("max_result", 25);
                    object1.put("sort", "name");
                    object1.put("order_by", "ASC");
                    object1.put("query", "");
                    object1.put("favorite", "false");
                    object1.put("save_search", "false");
                    JSONObject object2 = new JSONObject();
                    JSONArray selectedField = selectedfield(databasehelper, mParam1);
                    object2.put("select_fields", selectedField);
                    object1.put("name_value_list", object2);
                    object.put("rest_data", object1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("TAG", "getBody: " + object.toString());
//                dialog.dismiss();
                return object.toString().getBytes();
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 100,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    public JSONArray selectedfield(Databasehelper databasehelper, String modulename) {
        String s;
        s = databasehelper.getlayoutdefs(modulename);
        JSONArray jsonArray = null;
        JSONObject object = null;
        try {
            object = new JSONObject(s);
            jsonArray = object.getJSONArray("list");
//            Log.e("TAG", "selectedfield: from database " + jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}