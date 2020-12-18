package com.priyanka.newuat_demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.android.volley.AuthFailureError;
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class drawer extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Intent intent;
    RequestQueue queue;
    HashMap<Integer,String> modules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        modules=new HashMap<>();
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        queue= Volley.newRequestQueue(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "hey Welcome to world", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        intent = getIntent();
        String token=intent.getStringExtra("token");
        String auth= "Bearer "+token;
        Log.e("TAG", "onCreate: "+token );

        String url="https://newuat.njcrm.in/api/v1/module-list";
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
                        modules.put(i,modulename);
                        Log.e("TAG", "onResponse:modules: "+modules );
                        Log.e("TAG", "onResponse: module: "+modulename +"\n");

                    }
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

        queue.add(request);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.Dashboard, R.id.Sms_call, R.id.Global_search)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Menu m =navigationView.getMenu();
        Log.e("TAG", "onCreate:modules.size(): "+modules.size() );
        for(int i=0;i<modules.size();i++) {
            Log.e("TAG", "onCreate: " + modules.get(i));
            m.add(0, 0, 0, modules.get(i)).setIcon(R.drawable.side_nav_bar);

        }
        m.add(0, 0, 0, "priyanka").setIcon(R.drawable.side_nav_bar);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        menu.add(0, 0, 0, "Option1").setShortcut('3', 'c');

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        menu.add(0, 0, 0, "Option1").setShortcut('3', 'c');
//        menu.show()
        return super.onPrepareOptionsMenu(menu);
//        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}