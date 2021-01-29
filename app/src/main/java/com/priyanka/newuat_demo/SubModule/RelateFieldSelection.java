package com.priyanka.newuat_demo.SubModule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.priyanka.newuat_demo.R;
import com.priyanka.newuat_demo.SharedPrefrence;
import com.priyanka.newuat_demo.singletone.variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RelateFieldSelection extends AppCompatActivity implements com.priyanka.newuat_demo.Adapter.Adapter.OnItemClickLister{

    String module;
    private String TAG="RelateFieldSelection";
    Databasehelper databasehelper;
    SharedPrefrence prefrence;
    String url;
    String auth;
    String mParam1;
    Adapter adapter;
    ListView listView;
    ArrayList<HashMap<String, String>> map;
    String name;
    String key="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relate_field_selection);

        //intent
        Intent intent=getIntent();
        module=intent.getStringExtra("module");
        name=intent.getStringExtra("name");
        try {

            key=intent.getStringExtra("key");
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.e(TAG, "onCreate: "+module);

        //DataClass initialization
        databasehelper=new Databasehelper(getApplicationContext());
        listView=findViewById(R.id.listView);
        map = new ArrayList<>();

        if (module.equals("Team")) {
            mParam1="Accounts";
        }else {
            mParam1 = databasehelper.getFrontEndname(module);
        }
        prefrence=new SharedPrefrence(getApplicationContext());
        url=prefrence.getURl()+ variables.version+variables.URL_GETENTRY_LIST;
        auth=variables.BEARER+prefrence.getToken();
        databasehelper=new Databasehelper(getApplicationContext());



        Request(module);

    }
    void Request(String module){
        Log.e(TAG, "Request: this is your url"+url );
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            Log.e(TAG, "onResponse: "+response );
            try {
                ArrayList<String> keyList = new ArrayList<>();
                JSONArray jsonArray=response.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    HashMap hashMap=new HashMap();
                    Iterator<String> keys=jsonObject.keys();
                    while (keys.hasNext()){
                        keyList.add(keys.next());
                    }
                    for (int j=0;j<jsonObject.length();j++){
                        hashMap.put(keyList.get(j),jsonObject.getString(keyList.get(j)));
//                            Log.e(TAG, "in for loop:" + keyList.get(j) + ":=>" + jsonObject.getString(keyList.get(j)));
                    }
                    map.add(hashMap);
                    Log.e(TAG, "onResponse: "+map );
                    adapter= new com.priyanka.newuat_demo.Adapter.Adapter(getApplicationContext(),10,map,mParam1,this,name,key);
                    listView.setAdapter((ListAdapter) adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e(TAG, "onErrorResponse: "+error )){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
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

    @Override
    public void onItemClicked(String name) {
        this.name=name;
        this.finish();
    }
}