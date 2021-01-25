package com.priyanka.newuat_demo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.priyanka.newuat_demo.Adapter.Adapter;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.Models.TeamData;
import com.priyanka.newuat_demo.SubModule.AddressPicker;
import com.priyanka.newuat_demo.SubModule.RelateFieldSelection;
import com.priyanka.newuat_demo.fragment.Details_frag;
import com.priyanka.newuat_demo.fragment.SelectDateFragment;
import com.priyanka.newuat_demo.fragment.SelectTimeFragment;
import com.priyanka.newuat_demo.singletone.variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;


import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CreateFeature extends AppCompatActivity  {

    private static final String TAG = "CreateFeature";

    // Variables
    String module;
    String name=null;
    String backEndName;
    String mainurl;

    //Databse
    Databasehelper databasehelper;

    //Clases
    Detail detail;

    //fragmnet
    Details_frag details_frag;
    //JsonArray
    JSONArray array;

    //Layouts
    LinearLayout linearLayout;

    //Widget
    TextInputEditText textView,textView1;
    LinearLayout.LayoutParams params,params1,params2;

    TextInputLayout textInputLayout;

    Adapter.OnItemClickLister mlistener;

    JSONObject jsonObject;

    SharedPrefrence prefrence;

//    HashMap<String ?>



    //Interfaces
    public void setOnItemClickListener(Adapter.OnItemClickLister listener){
        mlistener=listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feature);

        Log.e(TAG, "onCreate: " );

        //intent
        Intent  i=getIntent();
        module=i.getStringExtra("module");
        try {
            name=i.getStringExtra("name");
            Log.e(TAG, "onCreate: RelateFieldSelection "+name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Database
        databasehelper=new Databasehelper(getApplicationContext());

        //preffrences
        prefrence=new SharedPrefrence(getApplicationContext());

        //main url
        mainurl=prefrence.getURl();


        //Class
        detail=new Detail();

        //fragmant
//        String a=databasehelper.getBackendname(module);
        details_frag=new Details_frag(module);

        //variables initialization
        array=detail.selectedfileds(module,databasehelper,"edit");
        linearLayout=findViewById(R.id.activty_create_linear_layout);
        getSupportActionBar().hide();
//        android.widget.Toolbar toolbar=findViewById(R.id.create_toolbar);
        Toolbar toolbar=findViewById(R.id.create_toolbar);
//        setActionBar(toolbar);
//        setSupportActionBar(toolbar);
//        if (toolbar.getMenu().size()==0){
            toolbar.inflateMenu(R.menu.save);
//            String name=databasehelper.getFrontEndname(module);
            toolbar.setTitle(module);
//        }
//        getSupportActionBar().men;


        // fetching module key

        backEndName=databasehelper.getBackendname(module);
        Log.e(TAG, "onCreate:backEndName===> "+backEndName );

        //functions calling
        createLayout(array,getApplicationContext(),linearLayout);



    }

    private void createTextRelationShip(LinearLayout linearLayout, String key, String typr, String required, String module, String backend_name, String table_name) {
        // create a checkbox and Inputtxtlayout inside a linearvertical layout

//        LinearLayout.LayoutParams params3=
        final Boolean[] flag = new Boolean[1];
        TextInputLayout.LayoutParams textinputparams = new TextInputLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        textinputparams.setMargins(15,5,15,5);
        TextInputLayout.LayoutParams edittxtparams = new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT);
        edittxtparams.setMargins(15,5,15,5);

        LinearLayout linearLayout1=new LinearLayout(linearLayout.getContext());
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.setTag(R.id.required,required);
        linearLayout1.setTag(R.id.name,backend_name);
        linearLayout1.setTag(R.id.key,key);
        linearLayout1.setTag(R.id.type,typr);
        CheckBox checkBox=new CheckBox(linearLayout1.getContext());
        checkBox.setText("Select Exixting Account");
        checkBox.setLayoutParams(params);
        checkBox.setPadding(20,5,10,1);
        TextInputLayout textInputLayout=new TextInputLayout(linearLayout1.getContext());
        textInputLayout.setLayoutParams(textinputparams);
        textInputLayout.setTag(R.id.type,typr);
        textInputLayout.setTag(R.id.key,key);
        textInputLayout.setTag(R.id.name,backend_name);
        textInputLayout.setTag(R.id.required,required);
        TextInputEditText textInputEditText=new TextInputEditText(textInputLayout.getContext());
        textInputEditText.setHint(key);
        textInputEditText.setTag(R.id.type,typr);
        textInputEditText.setTag(R.id.key,key);
        textInputEditText.setTag(R.id.tableName,table_name);
        textInputEditText.setTag(R.id.required,required);
        textInputEditText.setTag(R.id.module,module);
        textInputEditText.setTag(R.id.name,backend_name);
        textInputEditText.setLayoutParams(edittxtparams);

//        if (checkBox.isChecked()){
//
//        }
//        else {
////            textInputEditText.setFocusable(true);
////            textInputEditText.setClickable(true);
//            textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);
//        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "onCheckedChanged: "+isChecked+" buttonView==>"+buttonView.isChecked());
                if (isChecked){
                    textInputEditText.setText("");
                    textInputEditText.setFocusable(false);
                    textInputEditText.setClickable(false);
//                    textInputEditText.setEnabled(true);
                    textInputEditText.setCursorVisible(false);
//                    textInputEditText.setKeyListener(null);
                    textInputEditText.setOnClickListener((View view) -> {
                        Intent intent=new Intent(CreateFeature.this, RelateFieldSelection.class);
                        intent.putExtra("module", module);
                        startActivityForResult(intent,12);});
                }
                else {

                    textInputEditText.setFocusable(true);
                    textInputEditText.setClickable(true);
                    textInputEditText.requestFocus();
                    textInputEditText.setEnabled(true);
                    textInputEditText.setCursorVisible(true);
                    textInputEditText.setOnClickListener(null);
                    textInputEditText.setFocusableInTouchMode(true);
                    textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                }

            }
        });
        linearLayout1.addView(checkBox);
        textInputLayout.addView(textInputEditText);
        linearLayout1.addView(textInputLayout);
        linearLayout.addView(linearLayout1);

    }

    public void createLayout(JSONArray details, Context context, LinearLayout linearLayout){
        // this function is for creating over all Dynamic Layout with
        String key=null;
        String backend_name=null;

        params = new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT);
        params.setMargins(5,5,10,5);

        TextInputLayout.LayoutParams textinputparams = new TextInputLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        textinputparams.setMargins(10,5,10,5);
        TextInputLayout.LayoutParams edittxtparams = new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT);
        edittxtparams.setMargins(10,5,0,5);

        for(int i=0;i<details.length();i++){
            try {
                key=details_frag.getDisplayNames(details.getString(i),"display_label",context);
                backend_name=details.getString(i);
                if (key!=null){
                    String typr=details_frag.getDisplayNames(details.getString(i),"type",context);
                    Log.e(TAG, "createLayout: this is your type==>"+typr);
                    String required=details_frag.getDisplayNames(details.getString(i),"required",context);

//                    Log.e(TAG, "createLayout: this is the type you search for===> "+typr );
                    TextInputLayout textInputLayout;
                    LinearLayout linearLayout1=new LinearLayout(CreateFeature.this);
                    linearLayout1.setOrientation(LinearLayout.VERTICAL);
                    String module=details_frag.getDisplayNames(details.getString(i),"module",context);
//                    linearLayout1.setLayoutParams();
                    switch(typr){
                        case "text":
                        case "url":
                        case "textarea":
                        case "comment":
                            textInputLayout= new TextInputLayout(CreateFeature.this);
                            textInputLayout.setLayoutParams(textinputparams);
                            textInputLayout.setPadding(10,5,10,5);
                            TextInputEditText textInputEditText=new TextInputEditText(textInputLayout.getContext());
                            textInputEditText.setTag(R.id.type,typr);
                            textInputEditText.setTag(R.id.key,key);
                            textInputEditText.setTag(R.id.required,required);
                            textInputEditText.setTag(R.id.name,backend_name);
                            textInputEditText.setLayoutParams(textinputparams);

                            textInputLayout.setHint(key);
//                            textInputLayout.setBackground(R.color.material_on_background_emphasis_high_type);
                            textInputLayout.addView(textInputEditText);
                            linearLayout.addView(textInputLayout);
                            break;
                        case "relate":
//                            Log.e(TAG, "createLayout: I am your relate field==> "+key+" || "+details.getString(i)+" || "+ module);
                            String tableName=details_frag.getDisplayNames(details.getString(i),"table_name",context);
                            relateData(linearLayout,key,typr,required,module,details.getString(i),backend_name,textinputparams,edittxtparams,tableName);
                            break;
                        case "multi-phone":
                            linearLayout1.setTag(R.id.type,typr);
                            linearLayout1.setTag(R.id.key,key);
                            linearLayout1.setTag(R.id.required,required);
                            linearLayout1.setTag(R.id.name,backend_name);
                            linearLayout.addView(linearLayout1);
                            createMultiField(linearLayout1,key,typr,required,InputType.TYPE_CLASS_NUMBER,backend_name);
                            break;
                        case "multi-email":
                            linearLayout1.setTag(R.id.type,typr);
                            linearLayout1.setTag(R.id.key,key);
                            linearLayout1.setTag(R.id.required,required);
                            linearLayout1.setTag(R.id.name,backend_name);
                            linearLayout.addView(linearLayout1);
                            createMultiField(linearLayout1,key,typr,required,InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS,backend_name);
                            break;
                        case "select":
                            String options=details_frag.getDisplayNames(details.getString(i),"options",context);
                            selectField(linearLayout,key,typr,required,textinputparams,edittxtparams,options,backend_name);
                            break;
//                        case "textarea":
//                        case "comment":
//                            EditText textView=new EditText(linearLayout.getContext());
//                            textView.setHint(key);
//                            textView.setTag(R.id.key,key);
//                            textView.setTag(R.id.type,typr);
//                            textView.setTag(R.id.name,backend_name);
////                            module="";
//                            textView.setTag(R.id.module,module);
//                            textView.setTag(R.id.required,required);
//                            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT);
//                            params.setMargins(15,10,15,10);
//                            textView.setLayoutParams(params);
//                            //textView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
//                            textView.setMaxLines(5);
////                            textView.
//                            textView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//                            linearLayout.addView(textView);
//                            break;
                        case "multi-address":
//                        case "multi-tag":
                            linearLayout1.setTag(R.id.type,typr);
                            linearLayout1.setTag(R.id.key,key);
                            linearLayout1.setTag(R.id.required,required);
                            linearLayout1.setTag(R.id.name,backend_name);
                            linearLayout.addView(linearLayout1);
                            createMultiField(linearLayout1,key,typr,required,InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS,backend_name);

                            break;
                        case "datetime":
                            createDate(linearLayout,key,typr,required,backend_name);
                            break;
                        case "text-relationship":
                            String table_name = details_frag.getDisplayNames(details.getString(i),"table_name",context);
                            Log.e(TAG, "createLayout: i am your text relationship"+key );
                            createTextRelationShip(linearLayout,key,typr,required,module,backend_name,table_name);
                            break;
                        default:

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void relateData(LinearLayout linearLayout, String key, String typr, String required, String module, String string, String backend_name, TextInputLayout.LayoutParams textinputparams, TextInputLayout.LayoutParams edittxtparams, String tableName) {

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT);
        params.setMargins(15,10,15,10);

        TextInputLayout textInputLayout=new TextInputLayout(CreateFeature.this);
        textInputLayout.setLayoutParams(textinputparams);
        textInputLayout.setPadding(10,5,10,5);
        textInputLayout.setHint(key);


        switch (string) {
            case "assigned_user_id":
                //this is for user
                AutoCompleteTextView autoCompleteTextView=new AutoCompleteTextView(textInputLayout.getContext());
                autoCompleteTextView.setLayoutParams(edittxtparams);
                autoCompleteTextView.setPadding(10,40,10,40);
//                autoCompleteTextView.setHint(key);
                autoCompleteTextView.setTag(R.id.key,key);
                autoCompleteTextView.setTag(R.id.type,typr);
                autoCompleteTextView.setTag(R.id.required,required);
                autoCompleteTextView.setTag(R.id.name,backend_name);
                autoCompleteTextView.setTag(R.id.tableName,tableName);
                // fetch names of team members
                ArrayList<TeamData> arrayList = databasehelper.fetchAllMemberNames();
//                Log.e(TAG, "relateData: autoCompleteTextView==>"+arrayList.get(1).getName());
                ArrayList<String> arrayList1=new ArrayList<>();
                for (int i=0;i<arrayList.size();i++)
                {
                    arrayList1.add(arrayList.get(i).getName());
                }
                ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList1);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(arrayAdapter);
                textInputLayout.addView(autoCompleteTextView);
                linearLayout.addView(textInputLayout);
                Log.e(TAG, "relateData: autoCompleteTextView.getListSelection()==> "+autoCompleteTextView.getListSelection());
                break;
            case "team_id":
                //this is for team members
                LinearLayout linearLayout1=new LinearLayout(CreateFeature.this);
                linearLayout1.setOrientation(LinearLayout.VERTICAL);
                linearLayout1.setTag(R.id.key,key);
                linearLayout1.setTag(R.id.type,typr);
                linearLayout1.setTag(R.id.required,required);
                linearLayout1.setTag(R.id.name,backend_name);
                linearLayout.addView(linearLayout1);
                createMultiField(linearLayout1,key,typr,required,InputType.TYPE_CLASS_TEXT,backend_name);

                break;
            default:

                EditText editText=new EditText(textInputLayout.getContext());
                editText.setLayoutParams(edittxtparams);
//                editText.setHint(key);
                editText.setTag(R.id.key,key);
                editText.setTag(R.id.type,typr);
                editText.setTag(R.id.required,required);
                editText.setTag(R.id.module,module);
                editText.setTag(R.id.name,backend_name);
                editText.setTag(R.id.tableName);
                editText.setPadding(10,40,10,40);
                textInputLayout.addView(editText);
                linearLayout.addView(textInputLayout);
                editText.setFocusable(false);
                if (mlistener!=null){
                    editText.setText(name);
                }
                editText.setOnClickListener(v -> {
                    Log.e(TAG, "onClick: this is default case of fraagmnet==>"+module);
                    Intent intent=new Intent(CreateFeature.this, RelateFieldSelection.class);
                    intent.putExtra("module",module);
                    startActivityForResult(intent,10);
                });
            }

    }

    private void createDate(LinearLayout linearLayout, String key, String typr, String required, String backend_name) {
        //Date and time picker here
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.setMargins(30,20,30,20);
        LinearLayout linearLayout1=new LinearLayout(linearLayout.getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setTag(R.id.required,required);
        linearLayout1.setTag(R.id.key,key);
        linearLayout1.setTag(R.id.type,typr);
        linearLayout1.setTag(R.id.name,backend_name);
        EditText editText=new EditText(linearLayout1.getContext());
        params.weight=1;
        editText.setLayoutParams(params);
        editText.setHint("Time");
//        editText.setTag(R.id.tag,"");
        editText.setFocusable(false);
        editText.setTag(R.id.name,backend_name);
        editText.setTag(R.id.key,key);
        editText.setTag(R.id.type,typr);
        editText.setTag(R.id.required,required);
        editText.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        EditText editText1=new EditText(linearLayout1.getContext());
        editText1.setLayoutParams(params);
        editText1.setFocusable(false);
        editText1.setTag(R.id.required,required);
        editText1.setTag(R.id.type,typr);
        editText1.setTag(R.id.key,key);
        editText1.setTag(R.id.name,backend_name);
        editText1.setInputType(InputType.TYPE_CLASS_DATETIME);
        editText1.setHint("Date:");
        ImageView imageView=new ImageView(linearLayout1.getContext());
        editText.setOnClickListener(v -> {
            DialogFragment newFragment = new SelectTimeFragment(editText1);
            newFragment.show(getSupportFragmentManager(), "DatePicker");
            newFragment = new SelectDateFragment(editText);
            newFragment.show(getSupportFragmentManager(), "DatePicker");
//            if (editText.getTag(R.id.tag).equals("full")){
//                imageView.setImageResource(R.drawable.ic_baseline_clear_24);
//                linearLayout1.addView(imageView);
//            }
        });
        editText1.setOnClickListener(v -> {
            DialogFragment newFragment = new SelectTimeFragment(editText1);
            newFragment.show(getSupportFragmentManager(), "DatePicker");
//            if (editText1.getTag(R.id.tag).equals("full")){
//                imageView.setImageResource(R.drawable.ic_baseline_clear_24);
//                linearLayout1.addView(imageView);
//            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                editText1.setText("");
                editText.setTag(R.id.tag,"Empty");
                editText1.setTag(R.id.tag,"Empty");
                linearLayout1.removeView(imageView);
            }
        });

        linearLayout1.addView(editText);
        linearLayout1.addView(editText1);
        linearLayout.addView(linearLayout1);
    }

    private String fetchTeamId(String name){
        String abc="";
        ArrayList<TeamData> arrayList = databasehelper.fetchAllMemberNames();
//                Log.e(TAG, "relateData: autoCompleteTextView==>"+arrayList.get(1).getName());
//        ArrayList<String> arrayList1=new ArrayList<>();
        for (int i=0;i<arrayList.size();i++)
        {
            if (arrayList.get(i).getName().equals(name)){
                abc=arrayList.get(i).getId();
            }
        }
        return abc;
    }

    private String domeMoileLayout(String s, String options){
        String value = databasehelper.fetchDomeValue(options);
        String abc = "";
//        Log.e(TAG, "selectField: "+value);
//        ArrayList<String> arrayList = new ArrayList<>();

        // Fetching value from json string
        try {
            JSONArray jsonArray=new JSONArray(value);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String dome_key=jsonObject.getString("dom_key");
                String dom_value=jsonObject.getString("dom_value");
//                arrayList.add(dom_value);
                if (dom_value.equals(s)){
                    abc=dome_key;
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return abc;
    }

    private void selectField(LinearLayout linearLayout, String key, String typr, String required, LinearLayout.LayoutParams textinputparams, LinearLayout.LayoutParams edittxtparams, String options, String backend_name) {
        // Create a spinner from data selected from options field (dom_list)
        //fetch options from mobile_layout
        //fetch dom_list from mobile layout request
//        Log.e(TAG, "selectField:  these are your options==>"+options +" Key==>"+key);
        String value = databasehelper.fetchDomeValue(options);
//        Log.e(TAG, "selectField: "+value);
        ArrayList<String> arrayList = new ArrayList<>();

        // Fetching value from json string
        try {
            JSONArray jsonArray=new JSONArray(value);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String dome_key=jsonObject.getString("dom_key");
                String dom_value=jsonObject.getString("dom_value");
                arrayList.add(dom_value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Set these vlaues on spinnner

        TextView textView=new TextView(linearLayout.getContext());
        textView.setText(key);
        textView.setLayoutParams(params);

        linearLayout.addView(textView);

        LinearLayout linearLayout1=new LinearLayout(linearLayout.getContext());
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT));
        linearLayout1.setTag(R.id.required,required);
        linearLayout1.setTag(R.id.type,typr);
        linearLayout1.setTag(R.id.key,key);
        linearLayout1.setTag(R.id.name,backend_name);

        Spinner spinner=new Spinner(linearLayout1.getContext());
        ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList);
        spinner.setAdapter(adapter);
        spinner.setTag(R.id.required,required);
        spinner.setTag(R.id.type,typr);
        spinner.setTag(R.id.key,key);
        spinner.setTag(R.id.name,backend_name);
        spinner.setTag(R.id.option,options);

        linearLayout1.addView(spinner);
        linearLayout.addView(linearLayout1);
//        Log.e(TAG, "selectField: linearLayout.getChildCount()==>>  "+linearLayout.getChildCount() );

    }

    private void createMultiField(LinearLayout linearLayout, String key, String typr, String required, int typeClass,String backend_name) {

        int indexOfMyView = linearLayout.getChildCount();

        LinearLayout linearLayout1=new LinearLayout(linearLayout.getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT));
        linearLayout1.setTag(R.id.key,key);
        linearLayout1.setTag(R.id.type,typr);
        linearLayout1.setTag(R.id.required,required);
        linearLayout1.setTag(R.id.name,backend_name);

        TextInputLayout.LayoutParams textinputparams = new TextInputLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        textinputparams.setMargins(10,5,10,5);
        TextInputLayout.LayoutParams edittxtparams = new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT);
        edittxtparams.setMargins(10,5,0,5);
//                            Log.e(TAG, "createLayout: linearLayout.getWidth(): "+ linearLayout.getWidth());
        textInputLayout=new TextInputLayout(linearLayout1.getContext());
        textinputparams.weight=1;
        textInputLayout.setLayoutParams(textinputparams);
        textInputLayout.setPadding(10,5,10,5);
        textInputLayout.setTag(R.id.type,typr);
        textInputLayout.setTag(R.id.key,key);
        textInputLayout.setTag(R.id.required,required);
        textInputLayout.setTag(R.id.name,backend_name);
        TextInputEditText textInputEditText=new TextInputEditText(textInputLayout.getContext());
        textInputEditText.setTag(R.id.type,typr);
        textInputEditText.setTag(R.id.key,key);
        textInputEditText.setTag(R.id.required,required);
        textInputEditText.setTag(R.id.name,backend_name);
        textInputEditText.setFocusable(true);
        if (key.equals("Team")){
            textInputEditText.setFocusable(false);
            textInputEditText.setTag(R.id.key,key+indexOfMyView);

        }
        if (typr.equals("multi-address")){
            textInputEditText.setFocusable(false);
            textInputEditText.setTag(R.id.key,key+indexOfMyView);
        }
        textInputEditText.setInputType(typeClass);
        textInputEditText.setFocusable(true);
        if (key.equals("Phone")){
            textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }
        textInputEditText.setLayoutParams(edittxtparams);
        textInputLayout.setHint(key);
        textInputLayout.addView(textInputEditText);
        linearLayout1.addView(textInputLayout);

        params.weight= (float) 10;

        ImageView imageView1=new ImageView(linearLayout1.getContext());
        imageView1.setImageResource(R.drawable.ic_baseline_add_24);
        imageView1.setLayoutParams(params);
        imageView1.setTag(R.id.tag,"main");
        linearLayout1.addView(imageView1);


        Log.e(TAG, "createMultiField: "+indexOfMyView );
        if (indexOfMyView>0){
            imageView1.setImageResource(R.drawable.ic_baseline_clear_24);
            imageView1.setTag(R.id.tag,"sub");
        }

        linearLayout.addView(linearLayout1,indexOfMyView);


        Log.e(TAG, " LinearLayoutCount___ONCREATE ==> "+linearLayout.getChildCount() + " index == "+indexOfMyView);
//        imageView1.setOnClickListener(v -> {
//            Log.e(TAG, "onClick: you clicked plus:" +key);
//            Log.e(TAG, "createMultiField:this is your resource tag===> "+imageView1.getTag(R.id.resource) );
////            if (imageView1.getTag(R.id.resource)!=null){
////                Log.e(TAG, "onClick: removing func called" +linearLayout.getChildAt(R.id.resource));
////                linearLayout.removeView(linearLayout.getChildAt(R.id.resource));
////            }else{
//
//            Log.e(TAG, "createMultiField: indexOfMyView called:"+linearLayout.getChildCount()+" || "+indexOfMyView);
//            for (int i=0;i<linearLayout.getChildCount();i++){
//                Log.e(TAG, "createMultiField: for loop called:"+linearLayout.getChildCount()+" || "+indexOfMyView );
//                if (linearLayout.getChildAt(i) instanceof LinearLayout){
//                    Log.e(TAG, "createMultiField: th" );
//                    int temp=i;
//                    Log.e(TAG, "createMultiField: this is temp field==>"+temp);
//                    if (linearLayout.getChildCount()==indexOfMyView+1){
//                        if (!textInputEditText.getText().toString().isEmpty()) {
//                            Log.e(TAG, "onClick: this is your edit text text+++>> "+textInputEditText.getText() );
//                            createMultiField(linearLayout, key, typr, required, typeClass, textinputparams, edittxtparams);
//                        }else {
//                            textInputEditText.setError("Empty");
//                            textInputEditText.setFocusable(true);
//                        }
//                    }
//                }
//            }
//        });

    imageView1.setOnClickListener(v -> {

        Log.e(TAG, " Linear Layout Count ==> "+linearLayout.getChildCount());
        try {
            boolean flag = false;
            if (imageView1.getTag(R.id.tag).equals("main")) {

                for (int i=0;i<linearLayout.getChildCount();i++){
                    if (linearLayout.getChildAt(i) instanceof LinearLayout){
                        LinearLayout linearLayout2= (LinearLayout) linearLayout.getChildAt(i);


                        for (int j=0;j<linearLayout2.getChildCount();j++){

                            if (linearLayout2.getChildAt(j) instanceof TextInputLayout){
                                TextInputLayout textInputLayout= (TextInputLayout) linearLayout2.getChildAt(j);
                                if (textInputLayout.getEditText() instanceof  TextInputEditText){
                                    TextInputEditText textInputEditText1= (TextInputEditText) textInputLayout.getEditText();

                                    String TextValue = textInputEditText1.getText().toString().trim();

                                    if (!TextValue.equals("") && TextValue.length()>0){
                                        Log.e("Asmita==>","======= IFFFF ====== ");
                                        flag =true;
                                        // createMultiField(linearLayout, key, typr, required, typeClass);
                                    }
                                    else {
                                        flag=false;
//                                        Log.e("Asmita==>"," ====== ELSEEEEE ====== ");
                                        textInputEditText1.setError("Empty");
                                        textInputEditText1.setFocusable(true);
                                    }
                                }
                            }
                        }

                    }
                }
                if(flag){
                    createMultiField(linearLayout, key, typr, required, typeClass,backend_name);
                }else {

                }

//                if (!textInputEditText.getText().toString().isEmpty()) {
//                    Log.e(TAG, "onClick: this is your edit text text+++>> " + textInputEditText.getText());
//
//                    createMultiField(linearLayout, key, typr, required, typeClass);
//                } else {
//                    textInputEditText.setError("Empty");
//                    textInputEditText.setFocusable(true);
//                }

            }

            else {
                //here write the logic for removing view
                Log.e(TAG, "createMultiField: else block called" );
                linearLayout.removeView(linearLayout1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    });
    textInputEditText.setOnClickListener(v -> {
        Intent intent;
        if (textInputEditText.getTag(R.id.type).equals("relate")){
            intent=new Intent(CreateFeature.this,RelateFieldSelection.class);
            intent.putExtra("module","Team");
            intent.putExtra("name",key+indexOfMyView);
        //        intent.putExtra("flag", (Integer) textInputEditText.getTag(R.id.flag));
            startActivityForResult(intent,11);
        }else if (textInputEditText.getTag(R.id.type).equals("multi-address")){
           intent=new Intent(this, AddressPicker.class);
           intent.putExtra("key",key+indexOfMyView);
           startActivityForResult(intent,19);
        }
    });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult:ohh yeah activity came back " );
        String id = null;

        if (requestCode ==10){
            if (resultCode==RESULT_OK){
                name=data.getStringExtra("name");
                module=data.getStringExtra("module");
                id=data.getStringExtra("id");
                Log.e(TAG, "onActivityResult: "+ name+"|| "+module);
                // loop through the existing layout and set text at relate field
                String module1=databasehelper.getBackendname(module);
                setRelatedata(name, module1,id);

            }
        }else if (requestCode==11){
            if (resultCode==RESULT_OK){
                name=data.getStringExtra(   "name");
                module=data.getStringExtra("module");
                String key=data.getStringExtra("flag");
                id= data.getStringExtra("id");
                Log.e(TAG, "onActivityResult: "+ name+"|| "+module+" falg==>"+key);
                // loop through the existing layout and set text at relate field
                setRelatedataTeam(name, "Accounts",key ,id);

            }
        }else if (requestCode==12){
            if (resultCode==RESULT_OK){
                // this is for text-relationship attribute
                name=data.getStringExtra("name");
                module=data.getStringExtra("module");
                Log.e(TAG, "onActivityResult: "+ name+"|| "+module);
                textRelationship(name,module);
            }
        }
        else if (requestCode==19){
            Log.e(TAG, "onActivityResult: this is reqest code 19 " );
            if (resultCode==RESULT_OK){
                String street=data.getStringExtra("street");
                String area=data.getStringExtra("area");
                String city=data.getStringExtra("city");
                String state=data.getStringExtra("state");
                String country=data.getStringExtra("country");
                String postalcode=data.getStringExtra("postalcode");
                String addresstype=data.getStringExtra("addresstype");
                String key=data.getStringExtra("key");
                List<String> list=new ArrayList<>();
                list.add(street);
                list.add(area);
                list.add(city);
                list.add(state);
                list.add(country);
                list.add(postalcode);
                list.add(addresstype);
                String res = "";
                for (int i=0;i<list.size();i++) {
                    if (!list.get(i).isEmpty()){
                        res += list.get(i)+", ";
                    }
                }
                Log.e(TAG, "onActivityResult: "+res);
                setRelatedataTeam(res,module,key,id);
            }
        }

    }

    private void textRelationship(String name, String module) {
        for(int i=0;i<linearLayout.getChildCount();i++){
            if (linearLayout.getChildAt(i) instanceof LinearLayout){
                LinearLayout linearLayout1 = (LinearLayout) linearLayout.getChildAt(i);
                for (int j=0;j<linearLayout1.getChildCount();j++){
                    Log.e(TAG, "textRelationship: "+linearLayout1.getChildAt(j) );
                    if (linearLayout1.getChildAt(j) instanceof TextInputLayout){
                        Log.e(TAG, "textRelationship: hey i am your TextInputLayout(" );
                        TextInputLayout textInputLayout= (TextInputLayout) linearLayout1.getChildAt(j);
                        TextInputEditText textInputEditText= (TextInputEditText) textInputLayout.getEditText();
                        textInputEditText.setText(name);
                        if (textInputEditText.getTag(R.id.module).equals(module)){
                        }
                    }
                }
            }
        }
    }

    private void setRelatedataTeam(String name, String accounts, String key,String id) {
//        Log.e(TAG, "setRelatedataTeam: hey i am called==> "+module );
        for (int i=0;i<linearLayout.getChildCount();i++){
            if (linearLayout.getChildAt(i) instanceof LinearLayout){
                LinearLayout linearLayout1= (LinearLayout) linearLayout.getChildAt(i);
                for (int j=0;j<linearLayout1.getChildCount();j++){
//                    Log.e(TAG, "setRelatedataTeam:linearLayout1 "+linearLayout1.getChildAt(j) );
                    if (linearLayout1.getChildAt(j) instanceof LinearLayout){
                        LinearLayout linearLayout2= (LinearLayout) linearLayout1.getChildAt(j);
//                        Log.e(TAG, "setRelatedataTeam: linearLayout2"+linearLayout2.getChildCount());
                        for (int k=0;k<linearLayout2.getChildCount();k++){
//                            Log.e(TAG, "setRelatedataTeam:linearLayout2.getChildAt(k===> "+linearLayout2.getChildAt(k) );
                            if (linearLayout2.getChildAt(k) instanceof TextInputLayout){
//                                Log.e(TAG, "setRelatedataTeam:TextInputLayout " );
                                TextInputLayout textInputLayout= (TextInputLayout) linearLayout2.getChildAt(k);
                                try {
//                                    Log.e(TAG, "setRelatedataTeam: "+textInputLayout.getTag(R.id.key));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                Log.e(TAG, "setRelatedataTeam:textInputLayout.getChildCount() ==> "+textInputLayout.getChildCount());
//                                Log.e(TAG, "setRelatedataTeam: textInputLayout.getEditText()==>"+textInputLayout.getEditText());
//                                for(int l=0;l<textInputLayout.getChildCount();l++){
//                                    Log.e(TAG, "setRelatedataTeam:textInputLayout.getChildAt(l)==> "+textInputLayout.getChildAt(l));
                                    if (textInputLayout.getEditText() instanceof TextInputEditText){
//                                        Log.e(TAG, "setRelatedataTeam:TextInputEditText: ");
                                        TextInputEditText textInputEditText= (TextInputEditText) textInputLayout.getEditText();
                                        Log.e(TAG, "setRelatedataTeam:textInputEditText.getTag(R.id.key)==> "+textInputEditText.getTag(R.id.key)+" || type==>" +textInputEditText.getTag(R.id.type)+" || ==>"+key);
                                        if (textInputEditText.getTag(R.id.type).equals("relate")&&textInputEditText.getTag(R.id.key).equals(key)){
                                            Log.e(TAG, "setRelatedata:you got edit text " + name );
                                            textInputEditText.setText(name);
                                            textInputEditText.setTag(R.id.id,id);
                                        }
                                        if (textInputEditText.getTag(R.id.key).equals(key)){
                                            textInputEditText.setText(name);
                                        }
                                    }
//                                }
                            }
                        }
                    }
                    else if(linearLayout1.getChildAt(j) instanceof TextInputLayout){
                        Log.e(TAG, "setRelatedataTeam: I am instance od TextInputLayout==> ");
                        TextInputLayout textInputLayout1= (TextInputLayout) linearLayout1.getChildAt(j);
                        Log.e(TAG, "setRelatedataTeam: this is sublayout you are searching for==>"+linearLayout1.getChildAt(j) );
                        TextInputEditText textInputEditText= (TextInputEditText) textInputLayout1.getEditText();
                        textInputEditText.setText(name);
                    }
                }
            }
        }
    }


    private void setRelatedata(String name, String module,String id) {

        for(int i=0;i<linearLayout.getChildCount();i++){
            if (linearLayout.getChildAt(i) instanceof TextInputLayout) {
                TextInputLayout textInputLayout= (TextInputLayout) linearLayout.getChildAt(i);
                EditText editText= (EditText) textInputLayout.getEditText();
                try {
                    Log.e(TAG, "setRelatedata: "+editText.getTag(R.id.key));
                    if (editText.getTag(R.id.type).equals("relate")&&editText.getTag(R.id.module).equals(module)){
                        editText.setText(name);
                        editText.setTag(R.id.id,id);
                        Log.e(TAG, "setRelatedata: hey you got an edit txt" +editText.getTag(R.id.key)+" || ==:  "+editText.getTag(R.id.id));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void save(MenuItem item) {
        JSONObject jsonObject1=new JSONObject();
        JSONObject jsonObject2=new JSONObject();
        JSONObject jsonObject3=new JSONObject();
        JSONArray relateArray=new JSONArray();
        Boolean flag=false;
        Boolean chekboxflag=false;
        Boolean requireflag=false;

//        JSONArray jsonArray=new JSONArray();
//        Log.e(TAG, "save: you Click the Save...!" );
        try {
            Log.e(TAG, "save:  this is module ==>"+module );
            jsonObject2.put("module_name",module);
//            jsonObject2.put("name_value_list",jsonObject3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i=0;i<linearLayout.getChildCount();i++) {
            //text
            if (linearLayout.getChildAt(i) instanceof TextInputLayout) {
                TextInputLayout textInputLayout = (TextInputLayout) linearLayout.getChildAt(i);
                // for textInputEdittxt
                if (textInputLayout.getEditText() instanceof TextInputEditText){
                    TextInputEditText textInputEditText = (TextInputEditText) textInputLayout.getEditText();
//                Log.e(TAG, "save: "+ textInputEditText.getText()+" || " +textInputEditText.getTag(R.id.name));
                    Log.e(TAG, "save: checking for requred field==>"+textInputEditText.getTag(R.id.required) );
                    if (textInputEditText.getTag(R.id.required).equals("1")){
                        textInputLayout.setError("Required ");
                        requireflag=true;
                        break;
                    }
                    try {
                    jsonObject3.put(textInputEditText.getTag(R.id.name).toString(), textInputEditText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }
                if (textInputLayout.getEditText() instanceof EditText){
                    // for realte field
                    JSONObject jsonObject4=new JSONObject();
                    EditText editText=textInputLayout.getEditText();
                    try {
                        String s=editText.getTag(R.id.name).toString();
                        switch (s){
                            case "assigned_user_id":
//                            case "team_id":
                                String id=fetchTeamId(editText.getText().toString());
                                jsonObject3.put(editText.getTag(R.id.name).toString(),id);
                                break;
                            case "account_id":
//                                Log.e(TAG, "save:editText.getTag(R.id.id) ==> "+editText.getTag(R.id.id) );
//                                jsonObject4.put(editText.getTag(R.id.name).toString(),editText.getTag(R.id.id).toString());
//                                break;
//                            default:
                                if (!editText.getText().toString().isEmpty()){
                                    JSONObject jsonObject5=new JSONObject();
                                    jsonObject5.put("id",editText.getTag(R.id.id));
                                    jsonObject5.put("module_name",editText.getTag(R.id.module));
                                    jsonObject5.put("table_name",editText.getTag(R.id.tableName));
                                    jsonObject5.put("current_module",backEndName);
                                    jsonObject4.put(editText.getTag(R.id.name).toString(), jsonObject5);
                                    relateArray.put(jsonObject4);
                                    flag=true;
                                }
                        }
                        if(flag){
                        jsonObject3.put("related_modules",relateArray);}
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (linearLayout.getChildAt(i) instanceof LinearLayout){
                LinearLayout linearLayout1= (LinearLayout) linearLayout.getChildAt(i);

                JSONArray jsonArray=new JSONArray();
                ArrayList arrayList=new ArrayList();

                JSONObject jsonObject4=new JSONObject();
                for (int j=0;j<linearLayout1.getChildCount();j++) {
//                    Log.e(TAG, "save: linearLayout1.getChildAt(j)==>"+linearLayout1.getChildAt(j)+" || "+linearLayout1.getTag(R.id.key) +" || =>"+linearLayout1.getTag(R.id.name));
                    if (linearLayout1.getChildAt(j) instanceof CheckBox){
                        CheckBox checkBox= (CheckBox) linearLayout1.getChildAt(j);
                        if(checkBox.isChecked()){
                            chekboxflag=true;
                        }

                    }

                    if (linearLayout1.getChildAt(j) instanceof TextInputLayout){
                        TextInputLayout textInputLayout= (TextInputLayout) linearLayout1.getChildAt(j);
                        TextInputEditText textInputEditText= (TextInputEditText) textInputLayout.getEditText();
                        if (textInputEditText.getTag(R.id.type).equals("text-relationship")&& chekboxflag){
                            Log.e(TAG, "save:text-relationship ");
                            if (!textInputEditText.getText().toString().isEmpty()){
                                Log.e(TAG, "save: textInputEditText.getText()==> "+textInputEditText.getText() );
                                JSONObject jsonObject5=new JSONObject();
                                try {
                                    jsonObject5.put("id",textInputEditText.getTag(R.id.id));
                                    jsonObject5.put("module_name",textInputEditText.getTag(R.id.module));
                                    jsonObject5.put("table_name",textInputEditText.getTag(R.id.tableName));
                                    jsonObject5.put("current_module",backEndName);
                                    jsonObject4.put(textInputEditText.getTag(R.id.name).toString(), jsonObject5);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                relateArray.put(jsonObject4);
                                flag=true;
                            }
                        }
                        if (flag){
                            try {
                                Log.e(TAG, "save: relateArray==> "+relateArray );
                                jsonObject3.put("related_modules",relateArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Log.e(TAG, "save: textInputEditText.getTag(R.id.name) ==> "+textInputEditText.getTag(R.id.name) );
                            textInputEditText.setTag(R.id.name,textInputEditText.getTag(R.id.name)+"_name");
                        }

                        try {
                            jsonObject3.put(textInputEditText.getTag(R.id.name).toString(), textInputEditText.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if (linearLayout1.getChildAt(j) instanceof Spinner){
                        //condition for spinner
                        Spinner spinner= (Spinner) linearLayout1.getChildAt(j);
//                        Log.e(TAG, "save: Spinner==>"+ spinner.getTag(R.id.key) );
                        try {
//                            Log.e(TAG, "save:spinner.getTransitionName()==> "+spinner.getTransitionName()+" || "+spinner.getSelectedItem());;
                            String name=domeMoileLayout(spinner.getSelectedItem().toString(),spinner.getTag(R.id.option).toString());
//                            Log.e(TAG, "save:spinner.getTransitionName()==> "+name );;
                            jsonObject3.put(spinner.getTag(R.id.name).toString(),name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (linearLayout1.getChildAt(j) instanceof LinearLayout){
                        // this is for nested Layout
//                        JSONArray jsonArray=new JSONArray();
                        String layoutkey = null;
                        String table_name = null;
                        String related_table_name = null;
                        String primary;
                        String invalid;
                        String unsubscribed;

                        switch (linearLayout1.getTag(R.id.key).toString()){
                            case "Phone":
                                layoutkey="hiddenPhone";
                                table_name="phone_numbers";
                                related_table_name="phone_numbers_rel";
                                break;
                            case "Email":
                                layoutkey="hiddenEmail";
                                table_name="email_addresses";
                                related_table_name="email_address_rel";

                                break;
                            case "Address":
                                layoutkey="hiddenAddress";
                                break;

                            case "Team":
//                                Log.e(TAG, "save: you got the team==> "+ linearLayout1.getChildAt(j) );
//                                Log.e(TAG, "save:linearLayout1.getTag(R.id.key)==> "+linearLayout1.getTag(R.id.key));



                        }

                        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(j);
                        JSONObject jsonObject;

                        for (int k=0;k<linearLayout2.getChildCount();k++){
                            jsonObject = new JSONObject();


//                            Log.e(TAG, "save: linearLayout2.getChildAt(k===> "+linearLayout2.getChildAt(k));
                            if (linearLayout2.getChildAt(k) instanceof TextInputLayout) {
                                TextInputLayout textInputLayout = (TextInputLayout) linearLayout2.getChildAt(k);
                                TextInputEditText textInputEditText = (TextInputEditText) textInputLayout.getEditText();
                                if (linearLayout1.getTag(R.id.key).toString().equals("Team")){
//                                    Log.e(TAG, "save: "+linearLayout2.getChildAt(k));
                                    Log.e(TAG, "save:textInputEditText.getTag(R.id.name)==> "+textInputEditText.getTag(R.id.name));
                                    Log.e(TAG, "save:textInputEditText.getText()==>> "+textInputEditText.getText() +" || "+ textInputEditText.getTag(R.id.id));
                                    arrayList.add(textInputEditText.getTag(R.id.id));
                                }
//                                Log.e(TAG, "save:textInputEditText " + textInputEditText.getTag(R.id.name));
                                if (!textInputEditText.getText().toString().isEmpty()){
                                    try {
                                        jsonObject.put("table_name", table_name);
                                        jsonObject.put("related_table_name", related_table_name);
                                        jsonObject.put(table_name, textInputEditText.getText());
                                        if (k == 0) {
                                            jsonObject.put("primary", true);
                                        } else {
                                            jsonObject.put("primary", false);
                                        }
                                        jsonObject.put("invalid", false);
                                        jsonObject.put("unsubscribed", false);
                                        Log.e(TAG, "save:multifield jsonObject ==> " + jsonObject);
                                        jsonArray.put(jsonObject);
                                        Log.e(TAG, "save:multifield jsonArray==>"+jsonArray );
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        Log.e(TAG, "save: arrayList==>"+arrayList );

                        String abc = commaSepratedString(arrayList);
                        if (abc!=null) {
                            try {
                                jsonObject3.put("teamsSet", abc);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                            try {
                                jsonObject3.put(layoutkey,jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                    }
                }
            }
        }
        Log.e(TAG, "save: jsonObject3==> " + jsonObject3);

        try {
            jsonObject2.put("name_value_list",jsonObject3);
            jsonObject1.put("rest_data",jsonObject2);
            Log.e(TAG, "save:jsonObject1==>> "+jsonObject1 );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!requireflag){
            SubmitRequest(jsonObject3);
        }
    }

    private void SubmitRequest(JSONObject jsonObject) {
        String url=prefrence.getURl()+ variables.version+variables.URL_CREATE;
        String auth=variables.BEARER+prefrence.getToken();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: record created" );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error );
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
//                return super.getHeaders();
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
            @Override
            public byte[] getBody() {
                return jsonObject.toString().getBytes();
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 100,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String commaSepratedString(ArrayList arrayList) {
        return (String) arrayList.stream().collect(Collectors.joining(","));
    }
}