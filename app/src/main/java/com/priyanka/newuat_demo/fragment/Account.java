package com.priyanka.newuat_demo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.priyanka.newuat_demo.Adapter.Adapter;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.Models.GetEntry;
import com.priyanka.newuat_demo.Models.User;
import com.priyanka.newuat_demo.R;
import com.priyanka.newuat_demo.SharedPrefrence;
import com.priyanka.newuat_demo.drawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Databasehelper databasehelper;
    Context context;
    JsonObjectRequest request;
    SharedPrefrence prefrence;
    RequestQueue queue;
    String auth,moduleUrl;
    ArrayList<GetEntry> getEntryArrayList;
    ArrayList<User> userArrayList;
    String TAG="Fragment";
    ListView recyclerView;
    Adapter adapter;
    ArrayList<HashMap<String,String>> map;
    View view;
    Dialog dialog;
    drawer draw;
    Fragment fragment=null;
    ImageView imageView;
    String page;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Account(String s) {
        mParam1=s;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account.
     */
    // TODO: Rename and change types and number of parameters
    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account(param1);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Fragment");
        prefrence=new SharedPrefrence(context);
        databasehelper=new Databasehelper(context);
        queue = Volley.newRequestQueue(context);
        auth="Bearer "+prefrence.getToken();
        moduleUrl=prefrence.getURl()+"/api/v1/getentry-list";

        draw=new drawer();

        databasehelper.getMobileListData();

        dialog=new Dialog(context);
        dialog.show();

        mParam2=databasehelper.getBackendname(mParam1);

//        if (databasehelper.getEntryList(mParam1)==false) {
//            request = (JsonObjectRequest) ReqestModule(moduleUrl, auth);
//            request.setRetryPolicy(new DefaultRetryPolicy(
//                    1000 * 100,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(request);
//        }
//        else {
//            getEntryArrayList=databasehelper.getEntryListData();
//            Log.e(TAG, "onCreate: "+getEntryArrayList);
//
//
//        }

        //        for recycler
//        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);
//        adapter=new Adapter(context,4,map);
//        recyclerView.setAdapter(adapter);


    }

    private Object ReqestModule(String moduleUrl, String auth) {

        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.POST, moduleUrl, null, new Response.Listener<JSONObject>() {
            private String TAG="fragment";
            JSONArray jsonArray;
            String s=null;
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: "+response );
                try {
                        try {
                            s=response.getString("error_message");
                            }catch (Exception e){
                            Log.e(TAG, "onResponse: "+e );
                            }
                    jsonArray=response.getJSONArray("data");
                    Log.e(TAG, "onResponse:data "+jsonArray );
                    JSONObject jsonObject=response.getJSONObject("links");
                    Log.e(TAG, "onResponse:links:" +jsonObject);
                    JSONObject jsonObject1=response.getJSONObject("meta");
                    Log.e(TAG, "onresponse:meta:" +jsonObject1);
                    ArrayList<String> keyList=new ArrayList<>();
                    map=new ArrayList<>();
                    JSONObject object;
                    for (int i=0;i<jsonArray.length();i++) {
                        object = jsonArray.getJSONObject(i);
                        HashMap hashMap = new HashMap();
                        Iterator<String> keys = object.keys();
                        while (keys.hasNext()) {
                            keyList.add(keys.next());
                        }

                        for (int j = 0; j < object.length(); j++) {

//                            Log.e(TAG,"Key:"+keys+"\n");
                            Log.e(TAG, "keyList:==>" + keyList.get(i) + "\n");
//                            for(int k=0;k<object1.length();k++){
//                            Log.e(TAG,"in for loop getJSONArray==>"+object1.getString(k));
//                            if (keyList.get(j).equals(object1.getString(k))) {
//                                Log.e(TAG, "onResponse: object1.getString(k)->"+object1.getString(k) );
                            hashMap.put(keyList.get(j), object.getString(keyList.get(j)));
                            Log.e(TAG, "in for loop:" + keyList.get(j) + ":=>" + object.getString(keyList.get(j)));
                            }
//                            }
//                            Log.e(TAG, "onResponse:object " + object+"\n");
//                        }
                        Log.e(TAG, "onResponse:object in for loop " + object);
                        map.add(hashMap);
                        Log.e(TAG, "map=>" + map);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                if (s!=null){
                    Toast.makeText(context,"no data found",Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onResponse: "+s );
//                    ListView listView=view.findViewById(R.id.recyclerview);
                    recyclerView.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    dialog.cancel();
                }
                else if (jsonArray.length()>0) {
                    adapter=new Adapter(context,20,map,mParam1);
                    imageView.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(adapter);
                    Log.e(TAG, "onCreateView:size of list "+map.size() );
                    dialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
//                return super.getHeaders();
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Accept","application/json");
                headers.put("Authorization",auth);
                return headers;
            }

            @Override
            public byte[] getBody() {
//                return super.getBody();
                JSONObject object= new JSONObject();
                try {
                    JSONObject object1=new JSONObject();
                    object1.put("module_name",mParam2);
                    object1.put("max_result",25);
                    object1.put("sort","name");
                    object1.put("order_by","ASC");
                    object1.put("query","");
                    object1.put("favorite","false");
                    object1.put("save_search","false");
                    JSONObject object2=new JSONObject();
                    JSONArray selectedField=selectedfield(databasehelper,mParam1);
                    object2.put("select_fields",selectedField);
                    object1.put("name_value_list",object2);
                    object.put("rest_data",object1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("TAG", "getBody: "+object.toString() );
                return object.toString().getBytes();
            }


        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 100,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return objectRequest;
    }

    public JSONArray selectedfield(Databasehelper databasehelper,String modulename) {
        String s;
        s=databasehelper.getlayoutdefs(modulename);
        JSONArray jsonArray = null;
        JSONObject object= null;
        try {
            object = new JSONObject(s);
            jsonArray=object.getJSONArray("list");
            Log.e("TAG", "selectedfield: from database "+jsonArray );
        } catch (JSONException e) {
            e.printStackTrace();
        }
//                gson.fromJson(s,type);
        return jsonArray;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_account, container, false);
        Log.e(TAG, "onCreateView: "+view );
        recyclerView=  getActivity().findViewById(R.id.recyclerview);
        imageView=getActivity().findViewById(R.id.nodatafound);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        adapter=new Adapter(context,4,map);
//        Log.e(TAG, "onCreateView:size of list "+map.size() );
//        recyclerView.setAdapter(adapter);
//        if (databasehelper.getEntryList(mParam1)==false) {

////        }
////        else {
//            getEntryArrayList=databasehelper.getEntryListData();
//            Log.e(TAG, "onCreate: "+getEntryArrayList);


        recyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!recyclerView.canScrollVertically(1)){
                    request = (JsonObjectRequest) ReqestModule(moduleUrl, auth);
                    queue.add(request);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

//        }

        return view;
    }
}