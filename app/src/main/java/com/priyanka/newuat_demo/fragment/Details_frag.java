package com.priyanka.newuat_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
        import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.priyanka.newuat_demo.Adapter.Detailsadapter;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.Detail;
import com.priyanka.newuat_demo.R;
import com.priyanka.newuat_demo.SharedPrefrence;
import com.priyanka.newuat_demo.singletone.variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Details_frag#} factory method to
 * create an instance of this fragment.
 */
public class Details_frag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String module;
    private static String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout linearLayout;
    Context context;
    private View view;
    SharedPrefrence prefrence;
    String url;
    String auth;
    String errormessage=null;
    HashMap record;
    ArrayList<HashMap<String, String>> map;
    private static Detailsadapter adapter;
    Databasehelper databasehelper;
    RequestQueue queue;
    HashMap hashMap;
    ArrayList<String> key;
    ArrayList<String> value;
    JSONObject object;
    JSONObject multi_fields = null;
    JSONObject jsonObject;
    TextView textView,textView1;
    LinearLayout.LayoutParams params,params1,params2;
    ArrayList<String> arrayListnumbers,arrayListEmail,arrayListaddress;
    JSONArray details;
    Detail d;



    public Details_frag(String module, String id, JSONObject object) {
        Log.e(TAG, "hey i am your Details_FRag" );
        this.module=module;
        this.id=id;
        this.object=object;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
            prefrence=new SharedPrefrence(getContext());
            map = new ArrayList<>();
            databasehelper = new Databasehelper(getContext());
//        Log.e(TAG, "onCreate:module "+module );
            mParam2 = databasehelper.getBackendname(module);
            url=prefrence.getURl();
            auth=variables.BEARER+prefrence.getToken();
            Log.e(TAG, "onCreate: of details_frag---> "+url );
        d=new Detail();
        params = new LinearLayout.LayoutParams(150, ActionBar.LayoutParams.MATCH_PARENT);
        params.setMargins(30,20,30,20);

        details= d.selectedfileds(module,databasehelper);
        Log.e(TAG, "onCreate: details:"+details );
        params1 = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT);
        params1.setMargins(30,20,30,20);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_details_frag, container, false);
//        detailtabrequest(url+variables.version+variables.URL_DETAIL);
        linearLayout=view.findViewById(R.id.account_frag_linear_layout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        // Here we are creating the layout of fragmnent create the structure

        createLayout();

        //this is for adding data
        fillData();

        return view;
        // Inflate the layout for this fragment

    }

    @SuppressLint("ResourceAsColor")
    private void fillData() {
        JSONObject name_value_list = null;
        try {
            String modulename = object.getString("module_name");
            name_value_list = object.getJSONObject("name_value_list");
            JSONArray relationship_list = object.getJSONArray("relationship_list");
            multi_fields = object.getJSONObject("multi_fields");
            ArrayList<String> keyList = new ArrayList<>();
            Iterator<String> keys=name_value_list.keys();
            hashMap=new HashMap();
            while (keys.hasNext()){
                keyList.add(keys.next());
            }
            Log.e(TAG, "Details_frag: "+keyList);
            Log.e(TAG, "Details_frag: "+name_value_list.length());

            String key= null;
            try {
                Log.e(TAG, "onCreateView:this is field form db:"+details );
                for (int i=0;i<name_value_list.length();i++) {
                    jsonObject = name_value_list.getJSONObject(keyList.get(i));
                    Log.e(TAG, "onCreateView: "+jsonObject.getString("name"));
                    Log.e(TAG, "onCreateView:linearLayout.getChildCount()"+linearLayout.getChildCount());
                    for (int j=0;j<linearLayout.getChildCount();j++){
//                        Log.e(TAG, "onCreateView: "+linearLayout.getChildAt(j) );
                        if (linearLayout.getChildAt(j) instanceof LinearLayout){
                            LinearLayout linearLayoutVertical = (LinearLayout) linearLayout.getChildAt(j);
                            for (int k=0;k<linearLayoutVertical.getChildCount();k++){
                                if (linearLayoutVertical.getChildAt(k) instanceof TextView ) {
                                    TextView textView = (TextView) linearLayoutVertical.getChildAt(k);
//                                    Log.e(TAG, "fillData: this is gat tag("+textView.getTag(R.id.key));
                                        if (textView.getTag(R.id.key)==null)
                                        {
//                                            Log.e(TAG, "fillData: this text view has no tag asingned value" );
//                                            textView.setText(jsonObject.getString("value"));
                                        }else if (textView.getTag(R.id.key)!=null)
                                        {
                                            Object tag = textView.getTag(R.id.key);
                                            if ("account_id".equals(tag)) {
                                                Log.e(TAG, "fillData:textView.getTag(R.id.id) "+textView.getTag(R.id.id));
//                                                textView.setTag(R.id.id,jsonObject.getString("value"));
                                                textView.setTag(R.id.key, "account_name");
                                            } else if ("assigned_user_id".equals(tag)) {
//                                                textView.setTag(R.id.id,jsonObject.getString("value"));
                                                textView.setTag(R.id.key, "assigned_user_name");
                                            }
                                            if(textView.getTag(R.id.key).equals(jsonObject.getString("name"))) {
                                                if (textView.getTag(R.id.key).equals("team_set_id")) {
                                                    String name = databasehelper.fetchTeamName(jsonObject.getString("value"));
                                                    Log.e(TAG, "fillData: your fetched db value is:" + name);
                                                    textView.setText(name);
                                                } else {
//                                                    Log.e(TAG, "onCreateView: textview:" + textView.getText().toString() + " tag=>" + textView.getTag(R.id.key));
                                                    textView.setText(jsonObject.getString("value"));
                                                }
                                            }if (textView.getTag(R.id.type)!=null){
                                                if (textView.getTag(R.id.type).equals("relate")){
//                                                    Log.e(TAG, "fillData: you got a relate Data field" );
                                                    textView.setTextColor(R.color.purple_200);
                                                    textView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Log.e(TAG, "onClick: id:"+textView.getTag(R.id.id)+" module:"+textView.getTag(R.id.module));
                                                            Log.e(TAG, "onClick: you Clicked on realte field text view" );
                                                            Intent i=new Intent(getContext(),Detail.class);
                                                            String id= (String) textView.getTag(R.id.id);
                                                            String module= (String) textView.getTag(R.id.module);
                                                            Log.e(TAG, "onClick:this is sending another req id"+id+" module:"+module);
                                                            i.putExtra("id",id);
                                                            i.putExtra("module_name",module);
                                                            startActivity(i);
                                                        }
                                                    });
                                                }
                                            }
                                        }

                                }else if (linearLayoutVertical.getChildAt(k) instanceof LinearLayout){
                                    LinearLayout linearLayouthorizontal= (LinearLayout) linearLayoutVertical.getChildAt(k);
//                                    Log.e(TAG, "fillData: you got a  horizontal linear Layout =>" +linearLayouthorizontal.getTag(R.id.key));
                                    if (linearLayouthorizontal.getTag(R.id.key)!=null){
                                        if (linearLayouthorizontal.getTag(R.id.key).equals(jsonObject.getString("name"))){
                                            Log.e(TAG, "fillData: you found a  match=:"+linearLayouthorizontal.getTag(R.id.key)+" of type:"+linearLayouthorizontal.getTag(R.id.type));
//                                           // place here swich case based on type
                                            //cases: multi-phone, multi-email, textarea,comment,multi-address,multi-tag
                                            switch (linearLayouthorizontal.getTag(R.id.type).toString()){
                                                case "multi-phone":
                                                    MultiLayoutfields("hiddenPhone",linearLayouthorizontal);
                                                    break;
                                                case "multi-email":
                                                    Log.e(TAG, "fillData: we are solving multi-email case==>" );
                                                    MultiLayoutfields("hiddenEmail",linearLayouthorizontal);
                                                    break;
                                                case "textarea":
                                                    textArea(linearLayouthorizontal,jsonObject.getString("value"));
                                                    break;
                                                case "comment":
                                                case "multi-tag":

//                                                    JSONObject jsonObject1=jsonObject.getJSONObject("value");
////                                                    Log.e(TAG, "fillData: "+jsonObject1 );
//                                                    for (int l=0;l<jsonObject1.length();l++){
//
//                                                    }
                                                    TextView textView=new TextView(getContext());
                                                    textView.setLayoutParams(params1);
                                                    textView.setGravity(Gravity.CENTER);
                                                    textView.setText(jsonObject.getString("value"));
                                                    linearLayouthorizontal.addView(textView);
                                                    break;
                                                case "multi-address":
                                                    MultiLayoutfields("hiddenAddress",linearLayouthorizontal);
                                                    break;
//                                                case "multi-tag":
//                                                    break;
                                                default:
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
//                Log.e(TAG, "key:"+key+":"+jsonObject.getString("value"));
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void textArea(LinearLayout linearLayouthorizontal, String value) {

        ReadMoreTextView readMoreTextView=new ReadMoreTextView(getContext());
        readMoreTextView.setText(value);
        readMoreTextView.setGravity(Gravity.CENTER);
        linearLayouthorizontal.addView(readMoreTextView);
    }

    private void MultiLayoutfields(String hidden, LinearLayout linearLayouthorizontal) {

        ArrayList<String> arraylist = new ArrayList<>();

        switch (hidden) {
            case "hiddenEmail":
                arraylist = fetchMultiValued(hidden, arraylist, "email_address");
                break;
            case "hiddenPhone":
                arraylist = fetchMultiValued(hidden, arraylist, "phone_number");
                break;
            case "hiddenAddress":
                arraylist=fetchAddress("hiddenAddress",arraylist);
        }
        linearLayouthorizontal.setOrientation(LinearLayout.VERTICAL);
        for (int i=0;i<arraylist.size();i++){
            TextView textView= new TextView(getContext());
//            textView.setLayoutParams(params1);
//            textView.setPadding(10,10,10,10);
            textView.setGravity(Gravity.CENTER);
            textView.setText(arraylist.get(i));
            linearLayouthorizontal.addView(textView);
        }
    }

    private void createLayout(){
        // this function is for creating over all Dynamic Layout with
       String key=null;
        for(int i=0;i<details.length();i++){
            try {
                key=getDisplayNames(details.getString(i),"display_label");
                if (key!=null){
                    String typr=getDisplayNames(details.getString(i),"type");

                    switch(typr){
                        case "text":
                            // here 1 layout that contain two textviews
//                            LinearLayout linearLayout2 = new LinearLayout(getContext());
//                            linearLayout2.setOrientation(LinearLayout.VERTICAL);
//                            Log.e(TAG, "createLayout:key "+key);
                            LinearLayout linearLayout1 = new LinearLayout(getContext());
                            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                            textView=new TextView(getContext());
                            textView.setLayoutParams(params);
                            textView.setText(key);
                            textView.setTag(null);
                            textView.setGravity(Gravity.CENTER);
                            textView1=new TextView(getContext());
                            textView1.setLayoutParams(params1);
//                            textView1.setText(jsonObject.getString("value"));
                            textView1.setGravity(Gravity.CENTER);
                            textView1.setTag(R.id.key,details.getString(i));
                            linearLayout1.addView(textView);
                            linearLayout1.addView(textView1);
                            linearLayout.addView(linearLayout1);
                            break;
                        case "relate":
                            String module=getDisplayNames(details.getString(i),"module");
                            LinearLayout linearLayout2 = new LinearLayout(getContext());
                            linearLayout2.setOrientation(LinearLayout.VERTICAL);
                            linearLayout1 = new LinearLayout(getContext());
                            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                            textView=new TextView(getContext());
                            textView.setLayoutParams(params);
                            textView.setText(key);
                            textView.setTag(null);
                            textView.setGravity(Gravity.CENTER);
                            textView1=new TextView(getContext());
                            textView1.setLayoutParams(params1);
                            textView1.setGravity(Gravity.CENTER);
                            textView1.setTag(R.id.key,details.getString(i));
                            textView1.setTag(R.id.type,"relate");
                            textView1.setTag(R.id.id,id);
                            textView1.setTag(R.id.module,module);
                            //FETCH NAME FORM DATABASE
//                                String name=databasehelper.fetchTeamName(jsonObject.getString("value"));
//                                Log.e(TAG, "onCreateView: fetched Name from db"+name );
//                                textView1.setText(name);

//                            textView1.setText(jsonObject.getString("value"));
//                                textView1.setOnClickListener();
                            linearLayout1.addView(textView);
                            linearLayout1.addView(textView1);
                            linearLayout.addView(linearLayout1);
                            break;
                        case "multi-phone":
                        case "multi-email":
                            linearLayout1=new LinearLayout(getContext());
                            textView=new TextView(getContext());
                            textView.setText(key);
                            textView.setTag(null);
                            textView.setLayoutParams(params);
                            linearLayout1.addView(textView);
                            linearLayout2=new LinearLayout(getContext());
                            linearLayout2.setTag(R.id.key,details.getString(i));
                            linearLayout2.setTag(R.id.type,typr);
                            linearLayout2.setLayoutParams(params1);
                            linearLayout1.addView(linearLayout2);
                            linearLayout.addView(linearLayout1);
//                            Multifields("hiddenPhone","phone_number",key,Linkify.PHONE_NUMBERS);
                            break;
                        case "select":
                            linearLayout1=new LinearLayout(getContext());
                            textView=new TextView(getContext());
                            textView.setText(key);
                            textView.setLayoutParams(params);
                            textView.setTag(null);
                            linearLayout1.addView(textView);
                            textView1=new TextView(getContext());
                            textView1.setTag(R.id.key,details.getString(i));
                            textView1.setGravity(Gravity.CENTER);
                            textView1.setLayoutParams(params1);
                            linearLayout1.addView(textView1);
                            linearLayout.addView(linearLayout1);
                            break;
                        case "url":
                            linearLayout1=new LinearLayout(getContext());
                            textView=new TextView(getContext());
                            textView.setLayoutParams(params);
                            textView.setTag(null);
                            textView.setGravity(Gravity.CENTER);
                            textView.setText(key);
                            linearLayout1.addView(textView);
                            textView1=new TextView(getContext());
                            textView1.setLayoutParams(params1);
                            textView1.setTag(R.id.key,details.getString(i));
                            textView1.setGravity(Gravity.CENTER);
                            Linkify.addLinks(textView1,Linkify.WEB_URLS);
                            textView1.setMovementMethod(LinkMovementMethod.getInstance());
//                            textView1.setText(jsonObject.getString("value"));
                            linearLayout1.addView(textView1);
                            linearLayout.addView(linearLayout1);
                            break;
                        case "textarea":
                        case "comment":
                        case "multi-address":
                        case "multi-tag":
                            linearLayout1=new LinearLayout(getContext());
                            textView=new TextView(getContext());
                            textView.setText(key);
                            textView.setTag(null);
                            textView.setLayoutParams(params);
                            linearLayout1.addView(textView);
                            linearLayout2=new LinearLayout(getContext());
                            linearLayout2.setTag(R.id.key,details.getString(i));
                            linearLayout2.setLayoutParams(params1);
                            linearLayout2.setTag(R.id.type,typr);
                            linearLayout1.addView(linearLayout2);
                            linearLayout.addView(linearLayout1);
                            break;
                        case "datetime":
                            linearLayout1=new LinearLayout(getContext());
                            textView=new TextView(getContext());
                            textView.setText(key);
                            textView.setTag(null);
                            textView.setLayoutParams(params);
                            linearLayout1.addView(textView);
                            textView1=new TextView(getContext());
                            textView1.setLayoutParams(params1);
                            textView1.setGravity(Gravity.CENTER);
                            textView1.setTag(R.id.key,details.getString(i));
                            textView1.setTag(R.id.type,typr);
                            linearLayout1.addView(textView1);
                            linearLayout.addView(linearLayout1);
                            break;
                        default:

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void MultiFieldAddressLayout(String key, ArrayList<String> arrayListaddress) {
        LinearLayout linearLayout1;
        TextView textView2;
        if (arrayListnumbers.size()>0){
            linearLayout1=new LinearLayout(getContext());
//            linearLayout1.setGravity(Gravity.CENTER);
            textView=new TextView(getContext());

            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            textView.setText(key);
            linearLayout1.addView(textView);
            LinearLayout linearLayout2=new LinearLayout(getContext());
            linearLayout2.setOrientation(LinearLayout.VERTICAL);
            linearLayout2.setGravity(Gravity.CENTER);
            for (int j=0;j<arrayListaddress.size();j++){
                textView2=new TextView(getContext());
                Pattern pattern = Pattern.compile(".*", Pattern.DOTALL);
                Linkify.addLinks(textView2,pattern,"geo:0,0?q=");
                textView2.setMovementMethod(LinkMovementMethod.getInstance());
                textView2.setGravity(Gravity.CENTER);
                textView2.setLayoutParams(params1);
                textView2.setText(arrayListaddress.get(j));
                linearLayout2.addView(textView2);
            }
            linearLayout1.addView(linearLayout2);
            linearLayout.addView(linearLayout1);
        }
    }

    private ArrayList<String> fetchAddress(String hiddenAddress, ArrayList<String> arrayListaddress) {
        JSONArray array= null;
//        Log.e(TAG, "fetchAddress: this functon called...! ");
        try {
            array = (JSONArray) multi_fields.get(hiddenAddress);
//            Log.e(TAG, "fetchAddress:array==> "+array );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject object= null;
        for (int i=0;i<array.length();i++){
            try {
                object = array.getJSONObject(i);
//                Log.e(TAG, "fetchAddress: "+object );
                String add=object.getString("street")+" "+object.get("city")+", "+object.get("state")+", "+object.get("country")+", "+object.get("postal_code");
//                Log.e(TAG, "fetchAddress:the address is==> "+add );
                arrayListaddress.add(add);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayListaddress;
    }
    void MultiFieldAddLayout(String key,int phoneNumbers,ArrayList<String> arrayList){
        LinearLayout linearLayout1;
        TextView textView2;
        if (arrayList.size()>0){
            linearLayout1=new LinearLayout(getContext());
            textView=new TextView(getContext());
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            textView.setText(key);
            linearLayout1.addView(textView);
            LinearLayout linearLayout2=new LinearLayout(getContext());
            linearLayout2.setOrientation(LinearLayout.VERTICAL);
            linearLayout2.setGravity(Gravity.CENTER);
//            params2= new LinearLayout.LayoutParams(ActionBar)
            for (int j=0;j<arrayList.size();j++){
                textView2=new TextView(getContext());
                textView2.setLayoutParams(params1);
                textView2.setGravity(Gravity.CENTER);
                Linkify.addLinks(textView2,phoneNumbers);
                textView2.setMovementMethod(LinkMovementMethod.getInstance());
                textView2.setText(arrayList.get(j));
                linearLayout2.addView(textView2);
            }
            linearLayout1.addView(linearLayout2);
            linearLayout.addView(linearLayout1);
        }
    }
    void Multifields(String s, String valuetoadd, String key,int phoneNumbers){

        arrayListnumbers=new ArrayList<>();
        arrayListnumbers=fetchMultiValued(s,arrayListnumbers,valuetoadd);
        Log.e(TAG, "detailtabrequest:arrayListnumbers==> "+arrayListnumbers );
        MultiFieldAddLayout(key,phoneNumbers,arrayListnumbers);
     }
    private String getDisplayNames(String key,String field) throws JSONException {
        String fielddefs=databasehelper.getFielddefs(module);
        String fieldvalue = null;
//        Log.e(TAG, "getDisplayNames:===> "+fielddefs );
        JSONArray jsonArray=new JSONArray(fielddefs);
        for (int i=0;i<jsonArray.length();i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object.get("name").equals(key)) {
//                Log.e(TAG, "getDisplayNames:key "+key );
//                Log.e(TAG, "getDisplayNames:object===> " + object.get("display_label"));
                fieldvalue=object.optString(field);
            }
        }
        return fieldvalue;
    }
    private ArrayList<String> fetchMultiValued(String hiddenfield,ArrayList<String> arrayList,String valetoadd){
        try {
            JSONArray array= (JSONArray) multi_fields.get(hiddenfield);
            for (int i=0;i<array.length();i++) {
                JSONObject object = array.getJSONObject(i);
//                Log.e(TAG, "detailtabrequest:json object in for loop==> "+object+"||===>"+ object.get(valetoadd) );
                arrayList.add(object.getString(valetoadd));
            }
//            Log.e(TAG, "onClick:hiddenPhone "+array );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

}