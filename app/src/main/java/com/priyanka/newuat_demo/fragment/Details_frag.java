package com.priyanka.newuat_demo.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.priyanka.newuat_demo.Adapter.Detailsadapter;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.R;
import com.priyanka.newuat_demo.SharedPrefrence;
import com.priyanka.newuat_demo.singletone.variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Details_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Details_frag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String module;
    private static String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listView;
    Context context;
    private View view;
    SharedPrefrence prefrence;
    String url;
    String auth;
    String errormessage=null;
    HashMap record;
    ArrayList<HashMap<String, String>> map;
    Detailsadapter adapter;
    Databasehelper databasehelper;
    RequestQueue queue;
    HashMap hashMap;


    public interface NameValue{
        //void getname(String name);
    }

    public Details_frag(String id, String module) {
        this.id=id;
        this.module=module;
        Log.e(TAG, "Details_frag: "+"==>Fragmnet Created"+id+"||==>"+module );
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Details_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Details_frag newInstance(String param1, String param2) {
        Details_frag fragment = new Details_frag(id, module);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
            prefrence=new SharedPrefrence(getContext());
            map = new ArrayList<>();
            databasehelper = new Databasehelper(context);
            mParam2 = databasehelper.getBackendname(module);
            url=prefrence.getURl();
            auth=variables.BEARER+prefrence.getToken();
            Log.e(TAG, "onCreate: of details_frag---> "+url );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_details_frag, container, false);
        detailtabrequest(url+variables.version+variables.URL_DETAIL);
        listView=view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        
        return view;
        // Inflate the layout for this fragment

    }
    private void detailtabrequest(String url) {
        Log.e(TAG, "detailtabrequest:auth--> " + auth);
        Log.e(TAG, "detailtabrequest: " + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            JSONObject name_value_list = null;

            try {
                try {
                    errormessage = response.getString("error_message");
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e);
                }
                Log.e(TAG, "detailtabrequest:response--> " + response);
                JSONObject object = response.getJSONObject("entry_list");
                String modulename = object.getString("module_name");
                name_value_list = object.getJSONObject("name_value_list");
                JSONArray relationship_list = object.getJSONArray("relationship_list");
                JSONObject multi_fields = object.getJSONObject("multi_fields");
                Log.e(TAG, "detailtabrequest:name_value_list==> " + name_value_list);
                ArrayList<String> keyList = new ArrayList<>();
                ArrayList<String> Valuelist=new ArrayList<>();
                Iterator<String> keys=name_value_list.keys();
                record = new HashMap();
                hashMap=new HashMap();
                while (keys.hasNext()){
//                    Log.e(TAG, "detailtabrequest:keys.next()==> "+keys.next());
//                    record.put(keys.next(),new HashMap<>());
                    keyList.add(keys.next());
                }
                Log.e(TAG, "detailtabrequest: "+keyList);
                Log.e(TAG, "detailtabrequest: "+name_value_list.length() );
                for (int i = 0; i < name_value_list.length(); i++) {
//                    Log.e(TAG, "detailtabrequest:===> inside for loop :"+keyList.get(i) );
                    JSONObject jsonObject=name_value_list.getJSONObject(keyList.get(i));
//                    hashMap.put(keyList.get(i),name_value_list.getString(keyList.get(i)));

                    hashMap.put(jsonObject.getString("name"),jsonObject.getString("value"));
                    Log.e(TAG, "detailtabrequest:hashMap==> "+hashMap );
                    Valuelist.add(jsonObject.getString("value"));
//                    Log.e(TAG, "detailtabrequest:name_value_list.getString(keyList.get(i))===> "+ name_value_list.getString(keyList.get(i)));
//                    Log.e(TAG, "detailtabrequest: "+hashMap.get(keyList.get(i)));
//                    map.add(hashMap);
                }
                Log.e(TAG, "detailtabrequest:this is the size of map====>"+ hashMap.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (errormessage!=null){
//                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "onResponse: " + errormessage);
////                listView.setVisibility(View.INVISIBLE);
//            }
//            else if (name_value_list.length()>0){
            Set<String> keySet = hashMap.keySet();
            ArrayList<String> listOfKeys = new ArrayList<String>(keySet);
            Collection<String> value=  hashMap.values();
            ArrayList<String> listOfValues = new ArrayList<String>(value);

            adapter=new Detailsadapter(module,context,10, listOfKeys,listOfValues);
//                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(adapter);
                Log.e(TAG, "onCreateView:size of list " + listOfKeys.size()+"values size>>"+listOfValues.size());
//            }
        }, error -> {
            Log.e(TAG, "detailtabrequest:error " + error);

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //return super.getHeaders();

                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", auth);
                Log.e(TAG, "headers:------------> " + headers);
                return headers;
            }

            @Override
            public byte[] getBody() {
                JSONObject object = new JSONObject();
                try {
                    JSONObject object1 = new JSONObject();
                    object1.put("action", "show");
                    object1.put("module_name", mParam2);
                    object1.put("id", id);
                    object1.put("select_fields", selectedfileds(module));
                    object1.put("select_relate_fields", new JSONArray());
                    object.put("rest_data", object1);
                    Log.e(TAG, "getBody: ody of request " + object);
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
        queue = Volley.newRequestQueue(context);
        Log.e(TAG, "detailtabrequest requets1: " + request + "||" + queue);

        queue.add(request);
        Log.e(TAG, "detailtabrequest===: " + request);
    }

    private JSONArray selectedfileds(String module) {
        String s;
        s = databasehelper.getlayoutdefs(module);
        JSONArray jsonArray = null;
        JSONObject object = null;
        try {
            object = new JSONObject(s);
            jsonArray = object.getJSONArray("detail");
            Log.e("TAG", "selectedfield: from database " + jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;

    }
}