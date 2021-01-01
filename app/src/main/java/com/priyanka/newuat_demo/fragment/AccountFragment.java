package com.priyanka.newuat_demo.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.priyanka.newuat_demo.Adapter.Adapter;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.MainActivity;
import com.priyanka.newuat_demo.Models.GetEntry;
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
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Databasehelper databasehelper;
    Context context;
    JsonObjectRequest request;
    SharedPrefrence prefrence;
    RequestQueue queue;
    String auth, moduleUrl;
    ArrayList<GetEntry> getEntryArrayList;
    String TAG = "Fragment";
    ListView recyclerView;
    Adapter adapter;
    ArrayList<HashMap<String, String>> map;
    View view;
    ProgressDialog dialog;
    drawer draw;
    Fragment fragment = null;
    ImageView imageView;
    int page;
    String nexturl = null;
    String current_page;
    String total;
    String from;
    String last_page;
    String to;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView textView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment(String s) {
        mParam1 = s;
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
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment(param1);
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
        prefrence = new SharedPrefrence(context);
        databasehelper = new Databasehelper(context);
        queue = Volley.newRequestQueue(context);
        auth = "Bearer " + prefrence.getToken();
        page = 1;
        moduleUrl = prefrence.getURl() + "/api/v1/getentry-list?page=";
        textView = getActivity().findViewById(R.id.pagebox);
        draw = new drawer();

        databasehelper.getMobileListData();

        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(true);
        dialog.setMessage("Loading...........");
        dialog.show();

//        swipeRefreshLayout.setRefreshing(false);
        mParam2 = databasehelper.getBackendname(mParam1);

    }

    private Object ReqestModule(String moduleUrl, String auth) {

        Log.e(TAG, "ReqestModule:moduleUrl: " + moduleUrl);
        dialog.show();
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, moduleUrl, null, new Response.Listener<JSONObject>() {
            private String TAG = "fragment";
            JSONArray jsonArray;
            String s = null;

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: " + response);
                try {
                    try {
                        s = response.getString("error_message");
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e);
                    }
                    jsonArray = response.getJSONArray("data");
                    Log.e(TAG, "onResponse:data " + jsonArray);
                    JSONObject jsonObject = response.getJSONObject("links");
                    Log.e(TAG, "onResponse:links:" + jsonObject);
                    JSONObject jsonObject1 = response.getJSONObject("meta");
                    Log.e(TAG, "onresponse:meta:" + jsonObject1);
                    ArrayList<String> keyList = new ArrayList<>();
                    JSONObject object;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        object = jsonArray.getJSONObject(i);
                        HashMap hashMap = new HashMap();
                        Iterator<String> keys = object.keys();
                        while (keys.hasNext()) {
                            keyList.add(keys.next());
                        }
                        for (int j = 0; j < object.length(); j++) {
                            Log.e(TAG, "keyList:==>" + keyList.get(i) + "\n");
                            hashMap.put(keyList.get(j), object.getString(keyList.get(j)));
                            Log.e(TAG, "in for loop:" + keyList.get(j) + ":=>" + object.getString(keyList.get(j)));
                        }
                        Log.e(TAG, "onResponse:object in for loop " + object);
                        map.add(hashMap);
                        Log.e(TAG, "map=>" + map);
                    }
                    current_page = jsonObject1.getString("current_page");
                    Log.e(TAG, "onResponse:current_page: " + current_page);
                    total = jsonObject1.getString("total");
                    from = jsonObject1.getString("from");

                    nexturl = jsonObject.getString("next");
                    Log.e(TAG, "onResponse:nexturl: " + nexturl);
                    to = jsonObject1.getString("to");
                    textView.setText(to + "/" + total);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "onResponse: from:" + from);
                recyclerView.smoothScrollToPosition(Integer.parseInt(from));
                if (s != null) {
                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onResponse: " + s);
//                    ListView listView=view.findViewById(R.id.recyclerview);
                    recyclerView.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
//                    dialog.dismiss();
                } else if (jsonArray.length() > 0) {
                    Log.e(TAG, "onResponse: to==> " + to);
                    adapter = new Adapter(context, 20, map, mParam1, getActivity());
                    imageView.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(adapter);
                    Log.e(TAG, "onCreateView:size of list " + map.size());
//                    dialog.dismiss();
                }
            }
        }, error -> {

        }) {
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
                    object1.put("module_name", mParam2);
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
                dialog.dismiss();
                return object.toString().getBytes();
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 100,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Log.e("parthi","------objectRequest------>"+objectRequest);
        return objectRequest;
    }

    public JSONArray selectedfield(Databasehelper databasehelper, String modulename) {
        String s;
        s = databasehelper.getlayoutdefs(modulename);
        JSONArray jsonArray = null;
        JSONObject object = null;
        try {
            object = new JSONObject(s);
            jsonArray = object.getJSONArray("list");
            Log.e("TAG", "selectedfield: from database " + jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        Log.e(TAG, "onCreateView: " + view);
        recyclerView = getActivity().findViewById(R.id.recyclerview);
        swipeRefreshLayout = getActivity().findViewById(R.id.sw_refresh);

        imageView = getActivity().findViewById(R.id.nodatafound);
        map = new ArrayList<>();

//        dialog = new ProgressDialog(getContext());
//        dialog.setCancelable(true);
//        dialog.setMessage("Loading...........");

//        dialog.show();
        request = (JsonObjectRequest) ReqestModule(moduleUrl + "1", auth);
        queue.add(request);
//        dialog.dismiss();


        Log.e(TAG, "onCreateView:getSelectedItemPosition " + recyclerView.getSelectedItemPosition());
        recyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e(TAG, "onScrollStateChanged:scrollState " + scrollState);
                if (!recyclerView.canScrollVertically(1)) {
                    Log.e(TAG, "onScrollStateChanged: you are at the bottom");
                    if (nexturl == null) {
                        dialog.dismiss();
                        Snackbar.make(getView(), "this is the end", Snackbar.LENGTH_SHORT).show();
                    } else {
                        request = (JsonObjectRequest) ReqestModule(nexturl, auth);
                        queue.add(request);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e(TAG, "onScroll: " + firstVisibleItem + " visibleItemCount: " + visibleItemCount + " totalItemCount:" + totalItemCount);
                if (firstVisibleItem == 0) {
                    swipeRefreshLayout.setOnRefreshListener(() -> {
                        request = (JsonObjectRequest) ReqestModule(moduleUrl, auth);
                        queue.add(request);
                        swipeRefreshLayout.setRefreshing(false);
                    });
                }
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length > 0) {

            for (int i = 0; i < permissions.length; i++) {

                Log.e("parthi", "permissions===>" + permissions[i]);
                Log.e("parthi", "grantResults===>" + grantResults[i]);
                switch (permissions[i]) {

                    case Manifest.permission.SEND_SMS:
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED ) {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("permission required to send messages")
                                    .setPositiveButton("OK", null).show();

                        } /*else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)) {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("permission required to send messages")
                                    .setPositiveButton("OK", null).show();
                        } */else {
                            toRedirectToPermissionPage();
                        }
                        break;


                    case Manifest.permission.CALL_PHONE:
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                            doCall(phoneNo);
                        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("permission required to call")
                                    .setPositiveButton("OK", null).show();
                        } else {

                            toRedirectToPermissionPage();
                        }
                        break;
                    default:
                        break;

                }
            }

        }
    }

    private void toRedirectToPermissionPage() {

        new AlertDialog.Builder(context)
                .setMessage("permission required to send messages")
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    Log.e(TAG, "onRequestPermissionsResult: " + context.getPackageName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(uri);
                    startActivity(intent);
                }).show();
    }
}