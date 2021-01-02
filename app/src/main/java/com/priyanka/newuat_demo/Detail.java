package com.priyanka.newuat_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.priyanka.newuat_demo.Adapter.Adapter;
import com.priyanka.newuat_demo.Adapter.Detailsadapter;
import com.priyanka.newuat_demo.Adapter.ViewPagerAdapter;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.fragment.Details_frag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.ContentValues.TAG;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.priyanka.newuat_demo.singletone.variables.URL_DETAIL;

public class Detail extends AppCompatActivity {
    String id;
    Intent i;
    private String TAG = "Account_Detail";
    final String version = "/api/v1/";
    final String URL_SUB_LAYOUT = "sub-layout";
    SharedPrefrence prefrence;
    public String auth;
    String module;
    TabLayout tabLayout;
    ImageView favroite_img, addr_img, call_img, sms_img, wp_img, email_img, checkin_checkout, export_img;
    TextView name_txt, company_txt, addr_txt;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabItem tabitem_details, tabitem_realted;
    ArrayList<HashMap<String, String>> map;
    Databasehelper databasehelper;
    String mParam2,email,number;
    ViewPagerAdapter viewPagerAdapter;
    ImageView imageView;
    ListView listView;
    Detailsadapter adapter;
    Adapter adapter1;
    Details_frag.NameValue nameValue;
    HashMap hashMap;
    RequestQueue queue;

//    Context context;

//    private FragmentStateAdapter createCardAdapter() {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
//        return (PagerAdapter)adapter;
//    }

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentTitle = new ArrayList<>();
    private String errormessage;
    private String displayname,companyname,displayaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__detail);
        i = getIntent();
        prefrence = new SharedPrefrence(getApplicationContext());
        auth = "Bearer " +prefrence.getToken();
        databasehelper = new Databasehelper(getApplicationContext());
        pagerAdapter= new PagerAdapter() {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return false;
            }
        };
        id = i.getStringExtra("id");
        module = i.getStringExtra("module_name");
        //id s find
        //images
        tabLayout = findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        favroite_img = findViewById(R.id.card_fav);
        checkin_checkout = findViewById(R.id.checkin_checkout);
        call_img = findViewById(R.id.card_call);
        sms_img = findViewById(R.id.card_sms);
        wp_img = findViewById(R.id.card_wp);
        email_img = findViewById(R.id.card_email);
        addr_img = findViewById(R.id.card_map);
        export_img = findViewById(R.id.card_export);
        //textview
        name_txt = findViewById(R.id.card_name_text);
        company_txt = findViewById(R.id.card_compay_text);
        addr_txt = findViewById(R.id.card_address_text);
        listView=findViewById(R.id.list);



        Log.e(TAG, "onCreate: " + id);
        String url = prefrence.getURl();
        Log.e(TAG, "onCreate: " + url);
        mParam2 = databasehelper.getBackendname(module);
        map = new ArrayList<>();
        imageView = findViewById(R.id.nodatafound);

        getSupportActionBar().setTitle(mParam2+" Details");

        //request
        detailtabrequest(url + version + URL_DETAIL);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG, "onTabSelected: " + tab.getPosition());
                if (tab.getPosition() == 0) {
                    //send detail request
//                    detailtabrequest(url + version + URL_DETAIL);
                } else {
                    //send Related
                    relatetabreqest(url + version + URL_SUB_LAYOUT);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

            for(int i=0;i<listOfKeys.size();i++){
                if (listOfKeys.get(i).equals("name")){
                    displayname=listOfValues.get(i);
                }
                if (listOfKeys.get(i).equals("account_id")){
                    companyname=listOfValues.get(i);
                }
                if (listOfKeys.get(i).equals("address")){
                    displayaddress=listOfValues.get(i);
                }
                if (listOfKeys.get(i).equals("email")){
                    email=listOfValues.get(i);
                }
                if (listOfKeys.get(i).equals("phone")){
                    number=listOfValues.get(i);
                }
            }
            name_txt.setText((!displayname.isEmpty())?displayname : "");
            company_txt.setText((!companyname.isEmpty())?companyname : "");
            addr_txt.setText((!displayaddress.isEmpty())?displayaddress : "");

//            adapter=new Detailsadapter(module,getApplicationContext(),10, listOfKeys,listOfValues);
            //view pager
            viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),1,listOfKeys,listOfValues,module);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


            //Setting OnClickListeners
            call_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (number.isEmpty()){
                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }else {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Log.e(TAG, "call permission: not granted ");
                            ActivityCompat.requestPermissions(Detail.this, new String[]{Manifest.permission.CALL_PHONE}, 10);
                        }
//                else if()
//                else if(EasyPermissions.hasPermissions((Activity)context, Manifest.permission.CALL_PHONE)){
//                    requestPermissions((Activity)context,new String[]{Manifest.permission.CALL_PHONE},10);
//
//                }
                        else if (ActivityCompat.shouldShowRequestPermissionRationale(Detail.this, Manifest.permission.CALL_PHONE)) {
                            ActivityCompat.requestPermissions(Detail.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    9);
                        }
                     else {
                            String num = "tel:" + number;
                            final Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse(num));
                            startActivity(intent);
                        }
                    }
                }
            });
            sms_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (number.isEmpty()){
                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }else {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "Sms Permission: not granted ");
                        ActivityCompat.requestPermissions(Detail.this, new String[]{Manifest.permission.SEND_SMS}, 10);
                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(Detail.this, Manifest.permission.SEND_SMS)) {
                        new AlertDialog.Builder(Detail.this)
                                .setMessage("permission required to send messages")
                                .setPositiveButton("OK", null).show();
//                    ActivityCompat.requestPermissions(Recycler.this,
//                            new String[]{Manifest.permission.SEND_SMS},
//                            9);
                    } else {
                        adapter1=new Adapter(getApplicationContext(),20);
                        adapter1.OpenDialog(number, 3, getApplicationContext());
                    }
                }
                }
            });
//            wp_img

//                listView.setVisibility(View.VISIBLE);
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
        queue = Volley.newRequestQueue(getApplicationContext());
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
    private void relatetabreqest(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, response -> {

        }, error -> {

        }) {
        };
    }
}