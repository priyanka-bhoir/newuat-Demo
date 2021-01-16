package com.priyanka.newuat_demo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.fragment.Details_frag;

import org.json.JSONArray;
import org.json.JSONException;

public class CreateFeature extends AppCompatActivity {

    private static final String TAG = "CreateFeature";

    // Variables
    String module;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feature);
        Log.e(TAG, "onCreate: " );

        Intent  i=getIntent();
        module=i.getStringExtra("module");

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

        TextInputLayout.LayoutParams textinputparams = new TextInputLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        textinputparams.setMargins(10,5,10,5);
        for(int i=0;i<details.length();i++){
            try {
                key=details_frag.getDisplayNames(details.getString(i),"display_label",context);
                if (key!=null){
                    String typr=details_frag.getDisplayNames(details.getString(i),"type",context);
                    String required=details_frag.getDisplayNames(details.getString(i),"required",context);

//                    Log.e(TAG, "createLayout: this is the type you search for===> "+typr );

                    switch(typr){
                        case "text":
                        case "url":
                            TextInputLayout textInputLayout= new TextInputLayout(CreateFeature.this);
                            textInputLayout.setLayoutParams(textinputparams);
                            TextInputEditText textInputEditText=new TextInputEditText(textInputLayout.getContext());
                            textInputEditText.setTag(R.id.type,typr);
                            textInputEditText.setTag(R.id.key,key);
                            textInputEditText.setTag(R.id.required,required);
                            textInputLayout.setPadding(10,5,10,5);
                            textInputEditText.setLayoutParams(textinputparams);
                            textInputLayout.setHint(key);
//                            textInputLayout.setBackground(R.color.material_on_background_emphasis_high_type);
                            textInputLayout.addView(textInputEditText);
                            linearLayout.addView(textInputLayout);
                            break;
                        case "relate":
                            String options=details_frag.getDisplayNames(details.getString(i),"options",context);
                            break;
                        case "multi-phone":

                            break;
                        case "multi-email":

                            break;
                        case "select":

                            break;
                        case "textarea":
                        case "comment":

                            break;
                        case "multi-address":
//                        case "multi-tag":

                            break;
                        case "datetime":

                            break;
                        default:

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    private void createLayout() {
//        // this is for creatation of structure
////        Log.e(TAG, "createLayout: fontend name+a );
//        Log.e(TAG, "createLayout: "+array);
//        String key=null;
//        for (int i=0;i<array.length();i++){
//            try {
//
//            }
//        }
//    }
}