package com.priyanka.newuat_demo;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.priyanka.newuat_demo.ui.Account;
import com.priyanka.newuat_demo.ui.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class drawer extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Intent intent;
    RequestQueue queue;
    //    HashMap<Integer,String> modules;
    module_pojo modulePojo;
    ArrayList<module_pojo> modules;
    JsonObjectRequest request,mobileUrlRequest;
    Menu m;
    NavigationView navigationView;
    DrawerLayout drawer;
    NavController navController;
    SharedPrefrence prefrence;
    Toolbar toolbar;
    int timeoutMs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        setContentView(R.layout.activity_drawer);
        toolbar = findViewById(R.id.toolbar);
        modules = new ArrayList<>();
        prefrence=new SharedPrefrence(getApplicationContext());
        timeoutMs=60000*3;
        setSupportActionBar(toolbar);

        String version="/api/v1/";
        String moduleUrl = "https://newuat.njcrm.in/api/v1/module-list";
        String mobileLayoutURL="https://newuat.njcrm.in/api/v1/mobile-layout";

        FloatingActionButton fab = findViewById(R.id.fab);
        queue = Volley.newRequestQueue(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "hey Welcome to world", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        intent = getIntent();
        String token = intent.getStringExtra("token");
        String tokenpref=prefrence.getToken();
        Log.e("TAG", "onCreate:tokenpref "+tokenpref );
        String auth = "Bearer " + token;
        Log.e("TAG", "onCreate: " + token);

        request = (JsonObjectRequest) ReqestModule(moduleUrl, auth);

        mobileUrlRequest= (JsonObjectRequest)MobileLayout(mobileLayoutURL,auth);

        String name=prefrence.getUname();

//        R.string.nav_header_title=name;
        mobileUrlRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeoutMs,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
        queue.add(mobileUrlRequest);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.Dashboard, R.id.Sms_call, R.id.Global_search)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        m = navigationView.getMenu();
        Log.e("TAG", "onCreate:modules.size(): " + modules.size());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });


    }



    private void selectDrawerItem(MenuItem item) {
        Fragment fragment=null;
        Log.e("TAG", "selectDrawerItem: "+ item.getTitle().toString());
        switch (item.getTitle().toString()){
            default:
                toolbar.setTitle(item.getTitle().toString());
                fragment=new Account();
                loadfragment(fragment);
                break;
    }
}

    private void loadfragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_view, fragment);
        transaction.commit();
        drawer.closeDrawer(GravityCompat.START);
    }


    private void createMenu(ArrayList<module_pojo> modules, Menu m) {
        for(int i=0;i<modules.size();i++) {
            Log.e("TAG", "onCreate:modules " + modules.get(i));
            switch (modules.get(i).getName()) {
                case "Account":

                     m.add(0, 0, 0, modules.get(i).getPlural()).setIcon(R.drawable.ic_baseline_account_balance_24);
                     break;

                case "Task":
                    m.add(0, 0, 0, modules.get(i).getPlural()).setIcon(R.drawable.ic_baseline_add_task_24);
                    break;

                case "Note":
                    m.add(0, 0, 0, modules.get(i).getPlural()).setIcon(R.drawable.ic_baseline_note_24);
                    break;

                case "Call":
                    m.add(0, 0, 0, modules.get(i).getPlural()).setIcon(R.drawable.ic_baseline_call_24);
                    break;

                case "Campaign":
                    m.add(0, 0, 0, modules.get(i).getPlural()).setIcon(R.drawable.ic_baseline_campaign_24);
                    break;

                case "CheckList":
                    m.add(0, 0, 0, modules.get(i).getPlural()).setIcon(R.drawable.ic_baseline_check_24);
                    break;

                case "EmailEvent":
                    m.add(0, 0, 0, modules.get(i).getPlural()).setIcon(R.drawable.ic_baseline_mark_email_read_24);
                    break;

                case "EmailRecipient":
                    m.add(0,0,0,modules.get(i).getPlural()).setIcon(R.drawable.ic_baseline_mark_email_unread_24);
                    break;

                case "Email":
                    m.add(0,0,0,modules.get(i).getPlural()).setIcon(R.drawable.ic_baseline_email_24);
                    break;

                case "Message":
                    m.add(0,0,0,modules.get(i).getPlural()).setIcon(R.drawable.ic_baseline_message_24);
                    break;

                case "Contact":
                    m.add(0,0,0,modules.get(i).getPlural()).setIcon(R.drawable.ic_baseline_contacts_24);
                    break;

                case "Whatsapp":
                    m.add(0,0,0,modules.get(i).getPlural()).setIcon(R.drawable.whatsapp);
                    break;

                default:
                    m.add(0,0,0,modules.get(i).getPlural()).setIcon(R.drawable.ic_launcher_foreground);


            }

        }
        SubMenu subMenu=m.addSubMenu("Others");
        subMenu.add(0,0,0,"Import Contact").setIcon(R.drawable.ic_baseline_contacts_24);
        subMenu.add(0,0,0,"Settings").setIcon(R.drawable.ic_baseline_settings_24);
        subMenu.add(0,0,0,"Sign Out");
    }

    private Object ReqestModule(String url,String auth) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("TAG", "onResponse: "+response.toString() );
                try {
                    JSONObject jsonObject= new JSONObject(response.toString());

                    JSONArray arrayRequest= jsonObject.getJSONArray("module_list");

                    for(int i=0;i<arrayRequest.length();i++){
//                        JSONObject object=new JSONObject(String.valueOf(arrayRequest));
                        String modulename=arrayRequest.getJSONObject(i).getString("module_name");
                        String Singular= arrayRequest.getJSONObject(i).getString("module_singular");
                        String plurar= arrayRequest.getJSONObject(i).getString("module_plural");
//                        modules.put(i,modulename);
                        modulePojo=new module_pojo(modulename,Singular,plurar);
                        modules.add(modulePojo);

                        Log.e("TAG", "onResponse:modules: "+modules );
                        Log.e("TAG", "onResponse: module: "+modulename +"\n");

                    }

                    createMenu(modules,m);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "onErrorResponse: "+error );

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
        };
        return request;
    }

    private Object MobileLayout(String url, String auth) {
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("TAG", "onResponse:MobileLayout "+response );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG","onError:MobileLayout "+error);

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
        };
        return request;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        createMenu(modules,menu);
        getMenuInflater().inflate(R.menu.drawer, menu);
        menu.add(0, 0, 0, "Option1").setShortcut('3', 'c');

        return true;
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        createMenu(modules,menu);
        getMenuInflater().inflate(R.menu.drawer, menu);
        menu.add(0, 0, 0, "Option1").setShortcut('3', 'c');
//        menu.show()
        return super.onPrepareOptionsMenu(menu);
//        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this,item.getTitle(),Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}