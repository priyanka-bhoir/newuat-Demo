package com.priyanka.newuat_demo.SubModule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputLayout;
import com.priyanka.newuat_demo.CreateFeature;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.Models.module_pojo;
import com.priyanka.newuat_demo.R;

import java.util.ArrayList;

public class Modules extends AppCompatActivity {
    Databasehelper databasehelper;
    ArrayList<module_pojo> arrayList;
    private String TAG="Modules";
    LinearLayout linearLayout;
    ArrayAdapter adapter;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        Intent intent=getIntent();
        key=intent.getStringExtra("key");
        databasehelper=new Databasehelper(getApplicationContext());
        arrayList=databasehelper.getModuleData();
        linearLayout=findViewById(R.id.modeule_linear);
        ArrayList list=new ArrayList();
        for (int i=0;i<arrayList.size();i++){
//            createfield(arrayList.get(0).getName(),arrayList.get(0).getSingular(),arrayList.get(0).getPlural());
            list.add(arrayList.get(i).getSingular());
        }
        adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,list);
        createfield();

    }

    private void createfield() {
        ListView listView=new ListView(linearLayout.getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemClick: "+arrayList.get(position).getSingular());
                Intent intent =new Intent(Modules.this,RelateFieldSelection.class);
                intent.putExtra("module",arrayList.get(position).getSingular());
                intent.putExtra("key",key);
                startActivityForResult(intent,20);
            }
        });
        linearLayout.addView(listView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent=new Intent();
        intent.putExtra("name",data.getStringExtra("name"));
        intent.putExtra("module",data.getStringExtra("module"));
        intent.putExtra("key",data.getStringExtra("key"));
        setResult(Activity.RESULT_OK,intent);
        Log.e(TAG, "onActivityResult:  i am function of Module activity--: ");
        finish();
    }
}