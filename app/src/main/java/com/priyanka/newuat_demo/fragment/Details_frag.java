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
// * Use the {@link Details_frag#newInstance} factory method to
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
    private static Detailsadapter adapter;
    Databasehelper databasehelper;
    RequestQueue queue;
    HashMap hashMap;
    ArrayList<String> key;
    ArrayList<String> value;

    public Details_frag(ArrayList<String> listOfKeys, ArrayList<String> listOfValues, String module) {
        this.key=listOfKeys;
        value=listOfValues;
        this.module=module;
    }


    public interface NameValue{
        //void getname(String name);
    }

//    public Details_frag(Detailsadapter adapter,String module) {
//        this.id=id;
//        this.adapter=adapter;
//        Log.e(TAG, "Details_frag: "+"==>Fragmnet Created"+id+"||==>"+module );
//        // Required empty public constructor
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment Details_frag.
     */
    // TODO: Rename and change types and number of parameters
//    public static Details_frag newInstance(String param1, String param2) {
//        Details_frag fragment = new Details_frag();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
            databasehelper = new Databasehelper(getContext());
        Log.e(TAG, "onCreate:module "+module );
            mParam2 = databasehelper.getBackendname(module);
            url=prefrence.getURl();
            auth=variables.BEARER+prefrence.getToken();
            Log.e(TAG, "onCreate: of details_frag---> "+url );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_details_frag, container, false);
//        detailtabrequest(url+variables.version+variables.URL_DETAIL);
        listView=view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        adapter=new Detailsadapter(module,context,10, key,value);
        listView.setAdapter(adapter);
        return view;
        // Inflate the layout for this fragment

    }

}