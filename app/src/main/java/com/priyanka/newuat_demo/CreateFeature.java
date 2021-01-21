package com.priyanka.newuat_demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.priyanka.newuat_demo.Adapter.Adapter;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.fragment.Details_frag;
import com.priyanka.newuat_demo.fragment.RelatefieldCreate;
import com.priyanka.newuat_demo.fragment.SelectDateFragment;
import com.priyanka.newuat_demo.fragment.SelectTimeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CreateFeature extends AppCompatActivity  {

    private static final String TAG = "CreateFeature";

    // Variables
    String module;
    String name=null;

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

        //Class
        detail=new Detail();

        //fragmant
//        String a=databasehelper.getBackendname(module);
        details_frag=new Details_frag(module);

        //variables initialization
        array=detail.selectedfileds(module,databasehelper,"edit");
        linearLayout=findViewById(R.id.activty_create_linear_layout);



        //functions calling
        createLayout(array,getApplicationContext(),linearLayout);

    }
    public void createLayout(JSONArray details, Context context, LinearLayout linearLayout){
        // this function is for creating over all Dynamic Layout with
        String key=null;

        params = new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT);
        params.setMargins(5,5,10,5);

        TextInputLayout.LayoutParams textinputparams = new TextInputLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        textinputparams.setMargins(10,5,10,5);
        TextInputLayout.LayoutParams edittxtparams = new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT);
        edittxtparams.setMargins(10,5,0,5);

        for(int i=0;i<details.length();i++){
            try {
                key=details_frag.getDisplayNames(details.getString(i),"display_label",context);
                if (key!=null){
                    String typr=details_frag.getDisplayNames(details.getString(i),"type",context);
                    String required=details_frag.getDisplayNames(details.getString(i),"required",context);

//                    Log.e(TAG, "createLayout: this is the type you search for===> "+typr );
                    TextInputLayout textInputLayout;
                    LinearLayout linearLayout1=new LinearLayout(CreateFeature.this);
                    linearLayout1.setOrientation(LinearLayout.VERTICAL);
//                    linearLayout1.setLayoutParams();
                    switch(typr){
                        case "text":
                        case "url":
                            textInputLayout= new TextInputLayout(CreateFeature.this);
                            textInputLayout.setLayoutParams(textinputparams);
                            textInputLayout.setPadding(10,5,10,5);
                            TextInputEditText textInputEditText=new TextInputEditText(textInputLayout.getContext());
                            textInputEditText.setTag(R.id.type,typr);
                            textInputEditText.setTag(R.id.key,key);
                            textInputEditText.setTag(R.id.required,required);
                            textInputEditText.setLayoutParams(textinputparams);
                            textInputLayout.setHint(key);
//                            textInputLayout.setBackground(R.color.material_on_background_emphasis_high_type);
                            textInputLayout.addView(textInputEditText);
                            linearLayout.addView(textInputLayout);
                            break;
                        case "relate":
                            String module=details_frag.getDisplayNames(details.getString(i),"module",context);
                            Log.e(TAG, "createLayout: I am your relate field==> "+key+" || "+details.getString(i)+" || "+ module);
                            relateData(linearLayout,key,typr,required,module,details.getString(i));
                            break;
                        case "multi-phone":
                            linearLayout.addView(linearLayout1);
                            createMultiField(linearLayout1,key,typr,required,InputType.TYPE_CLASS_NUMBER);
                            break;
                        case "multi-email":
                            linearLayout.addView(linearLayout1);
                            createMultiField(linearLayout1,key,typr,required,InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                            break;
                        case "select":
                            String options=details_frag.getDisplayNames(details.getString(i),"options",context);
                            selectField(linearLayout,key,typr,required,textinputparams,edittxtparams,options);
                            break;
                        case "textarea":
                        case "comment":
                            EditText textView=new EditText(linearLayout.getContext());
                            textView.setHint(key);
                            textView.setTag(R.id.key,key);
                            textView.setTag(R.id.type,typr);
                            module="";
                            textView.setTag(R.id.module,module);
                            textView.setTag(R.id.required,required);
                            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT);
                            params.setMargins(15,10,15,10);
                            textView.setLayoutParams(params);
                            //textView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                            textView.setMaxLines(5);
//                            textView.
                            textView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            linearLayout.addView(textView);
                            break;
                        case "multi-address":
//                        case "multi-tag":
                            linearLayout.addView(linearLayout1);
//                            createMultiField(linearLayout1,key,typr,required,InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS,textinputparams,edittxtparams);

                            break;
                        case "datetime":
                            createDate(linearLayout,key,typr,required);
                            break;
                        case "text-relationship":
                            Log.e(TAG, "createLayout: i am your text relationship"+key );
                            break;
                        default:

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void relateData(LinearLayout linearLayout, String key, String typr, String required, String module, String string) {

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT);
        params.setMargins(15,10,15,10);



        switch (string) {
            case "assigned_user_id":
                //this is for user
                AutoCompleteTextView autoCompleteTextView=new AutoCompleteTextView(linearLayout.getContext());
                autoCompleteTextView.setLayoutParams(params);
                autoCompleteTextView.setHint(key);
                autoCompleteTextView.setTag(R.id.key,key);
                autoCompleteTextView.setTag(R.id.type,typr);
                // fetch names of team members
                ArrayList<String> arrayList = databasehelper.fetchAllMemberNames();
                Log.e(TAG, "relateData: "+arrayList);
                ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(arrayAdapter);
                linearLayout.addView(autoCompleteTextView);
                break;
            case "team_id":
                //this is for team members
                LinearLayout linearLayout1=new LinearLayout(CreateFeature.this);
                linearLayout1.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(linearLayout1);
                createMultiField(linearLayout1,key,typr,required,InputType.TYPE_CLASS_TEXT);

                break;
            default:
                EditText editText=new EditText(linearLayout.getContext());
                editText.setLayoutParams(params);
                editText.setHint(key);
                editText.setTag(R.id.key,key);
                editText.setTag(R.id.type,typr);
                editText.setTag(R.id.required,required);
                editText.setTag(R.id.module,module);
                linearLayout.addView(editText);
                editText.setFocusable(false);
                if (mlistener!=null){
                    editText.setText(name);
                }
                editText.setOnClickListener(v -> {
                    Log.e(TAG, "onClick: this is default case of fraagmnet==>"+module);
                    Intent intent=new Intent(CreateFeature.this,RelateFieldSelection.class);
                    intent.putExtra("module",module);
                    startActivityForResult(intent,10);
                });
            }

    }
    public void loadfragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame1,fragment);
        transaction.commit();
    }
    private void createDate(LinearLayout linearLayout, String key, String typr, String required) {
        //Date and time picker here
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.setMargins(30,20,30,20);
        LinearLayout linearLayout1=new LinearLayout(linearLayout.getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        EditText editText=new EditText(linearLayout1.getContext());
        params.weight=1;
        editText.setLayoutParams(params);
        editText.setHint("Time");
//        editText.setTag(R.id.tag,"");
        editText.setFocusable(false);
        editText.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        EditText editText1=new EditText(linearLayout1.getContext());
        editText1.setLayoutParams(params);
        editText1.setFocusable(false);
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

    private void selectField(LinearLayout linearLayout, String key, String typr, String required, LinearLayout.LayoutParams textinputparams, LinearLayout.LayoutParams edittxtparams, String options) {
        // Create a spinner from data selected from options field (dom_list)
        //fetch options from mobile_layout
        //fetch dom_list from mobile layout request
        Log.e(TAG, "selectField:  these are your options==>"+options +" Key==>"+key);
        String value = databasehelper.fetchDomeValue(options);
        Log.e(TAG, "selectField: "+value);
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
        Spinner spinner=new Spinner(linearLayout1.getContext());
        ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList);
        spinner.setAdapter(adapter);

        linearLayout1.addView(spinner);
        linearLayout.addView(linearLayout1);
        Log.e(TAG, "selectField: linearLayout.getChildCount()==>>  "+linearLayout.getChildCount() );

    }

    private void createMultiField(LinearLayout linearLayout, String key, String typr, String required, int typeClass) {
        LinearLayout linearLayout1=new LinearLayout(linearLayout.getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT));
        TextInputLayout.LayoutParams textinputparams = new TextInputLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        textinputparams.setMargins(10,5,10,5);
        TextInputLayout.LayoutParams edittxtparams = new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT);
        edittxtparams.setMargins(10,5,0,5);
//                            Log.e(TAG, "createLayout: linearLayout.getWidth(): "+ linearLayout.getWidth());
        textInputLayout=new TextInputLayout(linearLayout1.getContext());
        textinputparams.weight=1;
        textInputLayout.setLayoutParams(textinputparams);
        textInputLayout.setPadding(10,5,10,5);
        TextInputEditText textInputEditText=new TextInputEditText(textInputLayout.getContext());
        textInputEditText.setTag(R.id.type,typr);
        textInputEditText.setTag(R.id.key,key);
        textInputEditText.setTag(R.id.required,required);
        textInputEditText.setFocusable(true);
        if (key.equals("Team")){
            textInputEditText.setFocusable(false);
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
//                            ImageView imageView=new ImageView(linearLayout1.getContext());
//                            imageView.setImageResource(R.drawable.ic_baseline_more_vert_24);
//                            imageView.setLayoutParams(params);
//                            linearLayout1.addView(imageView);

        ImageView imageView1=new ImageView(linearLayout1.getContext());
        imageView1.setImageResource(R.drawable.ic_baseline_add_24);
        imageView1.setLayoutParams(params);
        imageView1.setTag(R.id.tag,"main");
        linearLayout1.addView(imageView1);


        int indexOfMyView = linearLayout.getChildCount();
        Log.e(TAG, "createMultiField: "+indexOfMyView );
        if (indexOfMyView>0){
            imageView1.setImageResource(R.drawable.ic_baseline_clear_24);
            imageView1.setTag(R.id.tag,"sub");
        }
//        Log.e(TAG, "createMultiField: "+indexOfMyView );
        linearLayout.addView(linearLayout1,indexOfMyView);

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
        try {
            if (imageView1.getTag(R.id.tag).equals("main")) {
                Log.e(TAG, "createMultiField: you clicked first one===:");
                Log.e(TAG, "createMultiField:these are my child's linear Layout==> "+linearLayout.getChildCount());
                if (!textInputEditText.getText().toString().isEmpty()) {
                    Log.e(TAG, "onClick: this is your edit text text+++>> " + textInputEditText.getText());
                    createMultiField(linearLayout, key, typr, required, typeClass);
                } else {
                    textInputEditText.setError("Empty");
                    textInputEditText.setFocusable(true);
                }
            } else {
                //here write the logic for removing view
                Log.e(TAG, "createMultiField: else block called" );
                linearLayout.removeView(linearLayout1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    });
    textInputEditText.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(CreateFeature.this,RelateFieldSelection.class);
            intent.putExtra("module","Team");
            startActivityForResult(intent,11);
        }
    });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult:ohh yeah activity came back " );
        if (requestCode ==10){
            if (resultCode==RESULT_OK){
                name=data.getStringExtra("name");
                module=data.getStringExtra("module");
                Log.e(TAG, "onActivityResult: "+ name+"|| "+module);
                // loop through the existing layout and set text at relate field
                String module1=databasehelper.getBackendname(module);
                setRelatedata(name, module1);

            }
        }else if (requestCode==11){
            if (resultCode==RESULT_OK){
                name=data.getStringExtra("name");
                module=data.getStringExtra("module");
                Log.e(TAG, "onActivityResult: "+ name+"|| "+module);
                // loop through the existing layout and set text at relate field
                setRelatedataTeam(name, "Accounts");

            }
        }

    }

    private void setRelatedataTeam(String name, String accounts) {
        for (int i=0;i<linearLayout.getChildCount();i++){
            if (linearLayout.getChildAt(i) instanceof LinearLayout){
                LinearLayout linearLayout1= (LinearLayout) linearLayout.getChildAt(i);
                for (int j=0;j<linearLayout1.getChildCount();j++){
                    Log.e(TAG, "setRelatedataTeam:linearLayout1 "+linearLayout1.getChildCount() );
                    if (linearLayout1.getChildAt(j) instanceof LinearLayout){
//                        LinearLayout linearLayout2= (LinearLayout) linearLayout1.getChildAt(j);
//                        for (int k=0;k<linearLayout2.getChildCount();k++){
//                            Log.e(TAG, "setRelatedataTeam: linearLayout2"+linearLayout2.getChildCount());
                            if (linearLayout1.getChildAt(j) instanceof TextInputLayout){
                                TextInputLayout textInputLayout= (TextInputLayout) linearLayout1.getChildAt(j);
                                for(int l=0;l<textInputLayout.getChildCount();l++){
                                    if (textInputLayout.getChildAt(l) instanceof TextInputEditText){
                                        TextInputEditText textInputEditText= (TextInputEditText) textInputLayout.getChildAt(l);
                                        if (textInputEditText.getTag(R.id.type).equals("relate")){
                                            Log.e(TAG, "setRelatedata:you got edit text "+name );
                                            textInputEditText.setText(name);
                                        }
                                    }
                                }
                            }
//                        }
                    }
                }
            }
        }
    }


    private void setRelatedata(String name, String module) {

        for(int i=0;i<linearLayout.getChildCount();i++){
           if (linearLayout.getChildAt(i) instanceof EditText){
               EditText editText= (EditText) linearLayout.getChildAt(i);
               try {
                   Log.e(TAG, "setRelatedata: "+editText.getTag(R.id.key));
                   if (editText.getTag(R.id.type).equals("relate")&&editText.getTag(R.id.module).equals(module)){
                       editText.setText(name);
                       Log.e(TAG, "setRelatedata: hey you got an edit txt" +editText.getTag(R.id.key));
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
        }
    }
}