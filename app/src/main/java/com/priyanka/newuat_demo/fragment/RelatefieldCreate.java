package com.priyanka.newuat_demo.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RelatefieldCreate#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class RelatefieldCreate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String module;
    private String TAG="RelatefieldCreate";
    LinearLayout linearLayout;
    private View view;
    String url;
    SharedPrefrence prefrence;
    String auth;
    Databasehelper databasehelper;

    //here we have to request for getEntryList using module name

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RelatefieldCreate.
     */
    // TODO: Rename and change types and number of parameters
    public static RelatefieldCreate newInstance(String param1, String param2,String module) {
        RelatefieldCreate fragment = new RelatefieldCreate(module);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
//        args.putString(module,module);
        fragment.setArguments(args);
        return fragment;
    }

    public RelatefieldCreate(String module) {
        // Required empty public constructor
        this.module=module;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e(TAG, "onCreateView:you have to call module "+module);

        view=inflater.inflate(R.layout.fragment_relatefield_create, container, false);
        mParam1=databasehelper.getFrontEndname(module);
        linearLayout= view.findViewById(R.id.linearlayout);
//        Request(module);
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}