package com.priyanka.newuat_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Account_Detail extends AppCompatActivity {
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
    String mParam2;
    ViewPagerAdapter viewPagerAdapter;
    ImageView imageView;
    ListView listView;
    Detailsadapter adapter;
//    Context context;

//    private FragmentStateAdapter createCardAdapter() {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
//        return (PagerAdapter)adapter;
//    }

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentTitle = new ArrayList<>();
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


        //view pager
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),1,id,module);
        viewPager.setAdapter(viewPagerAdapter);



        Log.e(TAG, "onCreate: " + id);
        String url = prefrence.getURl();
        Log.e(TAG, "onCreate: " + url);
        mParam2 = databasehelper.getBackendname(module);
        map = new ArrayList<>();
        imageView = findViewById(R.id.nodatafound);

        getSupportActionBar().setTitle(mParam2+" Details");

        //request
//        detailtabrequest(url + version + URL_DETAIL);


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
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }



    private void relatetabreqest(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, response -> {

        }, error -> {

        }) {
        };
    }
}