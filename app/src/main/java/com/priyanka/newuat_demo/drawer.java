package com.priyanka.newuat_demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.Models.MobileLayout;
import com.priyanka.newuat_demo.Models.module_pojo;
import com.priyanka.newuat_demo.fragment.AccountFragment;

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
import java.util.HashMap;
import java.util.Map;

import static com.priyanka.newuat_demo.R.drawable.ic_baseline_settings_24;

public class drawer extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Intent intent;
    RequestQueue queue;
    //    HashMap<Integer,String> modules;
    module_pojo modulePojo;
    ArrayList<module_pojo> modules;
    ArrayList<MobileLayout> mobileLayoutArrayList;
    JsonObjectRequest request,mobileUrlRequest;
    Menu m;
    NavigationView navigationView;
    DrawerLayout drawer;
    NavController navController;
    SharedPrefrence prefrence;
    Toolbar toolbar;
    int timeoutMs;
    String url;
    Databasehelper databasehelper;
    Gson gson;
    String version="/api/v1/";
    String TAG="Drawer class";
    MobileLayout mobileLayout;
    ProgressDialog progressDialog;




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
        gson=new Gson();
        url=prefrence.getURl();
        Log.e(TAG, "onCreate: "+url);
        databasehelper=new Databasehelper(getApplicationContext());

        String moduleUrl = url+version+"module-list";
        String mobileLayoutURL=url+version+"mobile-layout";

//        FloatingActionButton fab = findViewById(R.id.fab);
        queue = Volley.newRequestQueue(this);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "hey Welcome to world", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        intent = getIntent();
//        String token = intent.getStringExtra("token");
        String tokenpref=prefrence.getToken();
        String name=prefrence.getUname();
        Log.e(TAG, "onCreate:tokenpref "+tokenpref );
        String auth = "Bearer " + tokenpref;

        //Dialog box
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...........");

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
        if (databasehelper.getModule()==false) {
            request = (JsonObjectRequest) ReqestModule(moduleUrl, auth);
            queue.add(request);
        }
        else {
            modules=databasehelper.getModuleData();
            Log.e(TAG, "onCreate:modules===>"+modules.get(0));
            createMenu(modules,m);
        }

        if (databasehelper.getMobileList()==false) {
            mobileUrlRequest = (JsonObjectRequest) MobileLayout(mobileLayoutURL, auth);
            queue.add(mobileUrlRequest);

        }
        else{
            mobileLayoutArrayList=databasehelper.getMobileListData();
        }
        Log.e("TAG", "onCreate:modules.size(): " + modules.size());
        navigationView.setNavigationItemSelectedListener(item -> {
            Log.e(TAG, "onNavigationItemSelected: "+item.getItemId() );
            selectDrawerItem(item);
            return true;
        });
        toolbar.setNavigationIcon(R.drawable.ic_baseline_dehaze_24);

    }



    private void selectDrawerItem(MenuItem item) {
        Fragment fragment=null;
        Log.e("TAG", "selectDrawerItem: "+ item.getTitle().toString());
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getTitle().toString()){
            case "Dashboard":
                toolbar.setTitle(item.getTitle().toString());
                break;
            case "Sms/Call":
                toolbar.setTitle(item.getTitle().toString());
                break;
            case "Global Search":
                toolbar.setTitle(item.getTitle().toString());
                break;
            case "Import Contact":
                toolbar.setTitle(item.getTitle().toString());
                break;
            case "Settings":
                toolbar.setTitle(item.getTitle().toString());
                break;
            case "Sign Out":
                logout();
                break;
            default:
//                if (item.getTitle().toString()==)
                toolbar.setTitle(item.getTitle().toString());
//                toolbar.setSubtitle("Test Subtitle");

//                progressDialog.show();
                fragment=new AccountFragment(item.getTitle().toString());
                loadfragment(fragment);
                progressDialog.dismiss();
                break;
    }
}

    private void logout() {
        prefrence.setToken(null);
        String lurl=url+version+"/logout";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, lurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String error=response.getString("error_message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: error "+error );
            }
        });
        queue.add(request);
        Intent intent=new Intent(this,MainActivity.class);
        this.finish();
        startActivity(intent);
    }

    public void loadfragment(Fragment fragment) {
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
        subMenu.add(0,0,0,"Settings").setIcon(ic_baseline_settings_24);
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
                        String modulename=arrayRequest.getJSONObject(i).getString("module_name");
                        String Singular= arrayRequest.getJSONObject(i).getString("module_singular");
                        String plurar= arrayRequest.getJSONObject(i).getString("module_plural");
//                        modules.put(i,modulename);
                        modulePojo=new module_pojo(modulename,Singular,plurar);
                        modules.add(modulePojo);
                        databasehelper.insertModules(modulePojo);
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
                try {
                    JSONArray array=response.getJSONArray("mobile_layout");
                    Log.e("TAG", "onResponse:array "+array );
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject=array.getJSONObject(i);
                        Log.e("TAG", "onResponse:jsonObject "+jsonObject );
                        String module_name =jsonObject.getString("module_name");
                        Log.e("TAG", "onResponse:module_name "+ module_name);
                        String module_label=jsonObject.getString("module_label");
                        Log.e(TAG,"onResponse:module_label: "+module_label);
                        JSONArray jsonObject3 =jsonObject.getJSONArray("fielddefs");
                        Log.e("TAG", "onResponse:fielddefs "+jsonObject3 );
                        JSONObject jsonArray=jsonObject.getJSONObject("layoutdefs");
                        Log.e("TAG", "onResponse:layoutdefs "+jsonArray );
                        mobileLayout=new MobileLayout(module_name,module_label,jsonArray.toString(),jsonObject3.toString());
                        databasehelper.insertMobileLayout(mobileLayout);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        request.setRetryPolicy(new DefaultRetryPolicy(
                timeoutMs,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        createMenu(modules,menu);
//        Log.e(TAG, "onCreateOptionsMenu: "+toolbar.getTitle());
//        getMenuInflater().inflate(R.menu.drawer, menu);
//        menu.add(0, 0, 0, "Option1").setShortcut('3', 'c');

        return true;
    }


//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
////        createMenu(modules,menu);
////        getMenuInflater().inflate(R.menu.drawer, menu);
////        menu.add(0, 0, 0, "Option1").setShortcut('3', 'c');
////        menu.show()
//        return super.onPrepareOptionsMenu(menu);
////        return true;
//
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(this,"you clicked",Toast.LENGTH_LONG).show();
//        drawer.openDrawer(Gravity.LEFT);
//        progressDialog.show();
//        Log.e(TAG, "onOptionsItemSelected: " +toolbar.getTitle().toString());
        try {
//            String module=databasehelper.getBackendname(toolbar.getTitle().toString());
            Intent intent=new Intent(drawer.this,CreateFeature.class);
            intent.putExtra("module",toolbar.getTitle().toString());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.e(TAG, "onOptionsItemSelected:module==> "+module );
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}