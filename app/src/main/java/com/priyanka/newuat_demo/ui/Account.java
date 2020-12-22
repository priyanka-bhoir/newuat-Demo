package com.priyanka.newuat_demo.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.priyanka.newuat_demo.R;
import com.priyanka.newuat_demo.SharedPrefrence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

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
    String TAG="Fragment";
    RecyclerView recyclerView;
    Adapter adapter;


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
        moduleUrl=prefrence.getURl()+"/api/v1/getentry-list?page=1";

        //for recycler
//        recyclerView= recyclerView.findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);

        adapter= new Adapter();


        if (databasehelper.getEntryList()==false) {
            request = (JsonObjectRequest) ReqestModule(moduleUrl, auth);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    1000 * 6,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request);
        }
        else {
            getEntryArrayList=databasehelper.getEntryListData();
            Log.e(TAG, "onCreate: "+getEntryArrayList);


        }

    }

    private Object ReqestModule(String moduleUrl, String auth) {

        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.POST, moduleUrl, null, new Response.Listener<JSONObject>() {
            private String TAG="fragment";

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: "+response );
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
                    object1.put("module_name",mParam1);
                    object1.put("max_result",25);
                    object1.put("sort","email");
                    object1.put("order_by","ASC");
                    object1.put("query","");
                    object1.put("favorite","false");
                    object1.put("save_search","false");
                    object1.put("assigned_user_id",prefrence.getId());
                    JSONObject object2=new JSONObject();
                    String selectedField=selectedfield(databasehelper,mParam1);
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
        return objectRequest;
    }

    private String selectedfield(Databasehelper databasehelper,String modulename) {
        String s;
        s=databasehelper.getlayoutdefs(modulename);
        Log.e("TAG", "selectedfield: from database "+s );
        return s;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        return view;
    }
}