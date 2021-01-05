package com.priyanka.newuat_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.priyanka.newuat_demo.Adapter.Adapter;
import com.priyanka.newuat_demo.Adapter.Detailsadapter;
import com.priyanka.newuat_demo.Adapter.ViewPagerAdapter;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.fragment.Details_frag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kotlin.text.UStringsKt;

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
    AlertDialog dialog1;
    AlertDialog dialog;
    ProgressBar progressDialog;
    JSONObject multi_fields = null;
    ArrayList<String> arrayListnumbers,arrayListEmail,arrayListaddress;
    Geocoder geocoder;
    List<Address> addresses;


//    Context context;

//    private FragmentStateAdapter createCardAdapter() {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
//        return (PagerAdapter)adapter;
//    }

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentTitle = new ArrayList<>();
    private String errormessage;
    private String displayname,companyname,displayaddress;
    private Object Activity;

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

        progressDialog=findViewById(R.id.indeterminateBar);

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
        progressDialog.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            JSONObject name_value_list = null;
//            JSONObject multi_fields = null;

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
                multi_fields = object.getJSONObject("multi_fields");
                Log.e(TAG, "detailtabrequest:name_value_list==> " + name_value_list);
                ArrayList<String> keyList = new ArrayList<>();
                ArrayList<String> Valuelist=new ArrayList<>();
                Iterator<String> keys=name_value_list.keys();
                hashMap=new HashMap();
                while (keys.hasNext()){
                    keyList.add(keys.next());
                }
                Log.e(TAG, "detailtabrequest: "+keyList);
                Log.e(TAG, "detailtabrequest: "+name_value_list.length() );
                for (int i = 0; i < name_value_list.length(); i++) {
                    JSONObject jsonObject=name_value_list.getJSONObject(keyList.get(i));
                    hashMap.put(jsonObject.getString("name"),jsonObject.getString("value"));
                    Log.e(TAG, "detailtabrequest:hashMap==> "+hashMap );
                    Valuelist.add(jsonObject.getString("value"));
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
                if (listOfKeys.get(i).equals("industry")){
                    companyname=listOfValues.get(i);
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
            //view pager
            viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),1,listOfKeys,listOfValues,module);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


            //for number
            arrayListnumbers=new ArrayList<>();
            arrayListnumbers=fetchMultiValued("hiddenPhone",arrayListnumbers,"phone_number");
            Log.e(TAG, "detailtabrequest:arrayListnumbers==> "+arrayListnumbers );
            //this is for Email storing in array list
            arrayListEmail=new ArrayList<>();
            arrayListEmail=fetchMultiValued("hiddenEmail",arrayListEmail,"email_address");
            Log.e(TAG, "detailtabrequest:arrayListEmail==> "+arrayListEmail );
//            this is for Address
            arrayListaddress=new ArrayList<>();
            arrayListaddress=fetchAddress("hiddenAddress",arrayListaddress);
            Log.e(TAG, "detailtabrequest: "+arrayListaddress );
            //Setting OnClickListeners
            call_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayListnumbers.isEmpty()){
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
                            try {
                                JSONArray array= (JSONArray) multi_fields.get("hiddenPhone");
                                Log.e(TAG, "onClick:hiddenPhone "+array );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Dialogbox
                            Pickanumber(arrayListnumbers,1);
//                            String num = "tel:" + number;
//                            final Intent intent = new Intent(Intent.ACTION_CALL);
//                            intent.setData(Uri.parse(num));
//                            startActivity(intent);
                        }
                    }
                }
            });
            sms_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayListnumbers.isEmpty()){
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
                        Pickanumber(arrayListnumbers,2);
                    }
                }
                }
            });
            wp_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayListnumbers.isEmpty()){
                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }else {
                        Pickanumber(arrayListnumbers,3);
                    }
                }
            });
            email_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayListEmail.isEmpty()){
                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }else {
                        PickaEmail(arrayListEmail);
                    }
                }
            });
            addr_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayListaddress.isEmpty()){
                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Pickanumber(arrayListaddress,5);
                    }
                }
            });
            export_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayListnumbers.isEmpty()){
                        Toast.makeText(getApplicationContext(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }else {
                        if (arrayListnumbers.isEmpty()){
                            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                        }else {
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                                Log.e(TAG, "Sms Permission: not granted ");
                                ActivityCompat.requestPermissions(Detail.this, new String[]{Manifest.permission.WRITE_CONTACTS}, 10);
                            } else if (ActivityCompat.shouldShowRequestPermissionRationale(Detail.this, Manifest.permission.WRITE_CONTACTS)) {
                                new AlertDialog.Builder(Detail.this)
                                        .setMessage("permission required to send messages")
                                        .setPositiveButton("OK", null).show();
//                    ActivityCompat.requestPermissions(Recycler.this,
//                            new String[]{Manifest.permission.SEND_SMS},
//                            9);
                            } else {
                                ContentValues values = new ContentValues();
                                values.put(ContactsContract.CommonDataKinds.Phone.SEARCH_DISPLAY_NAME_KEY, displayname);
//                        arrayListnumbers.get(0);
                                values.put(ContactsContract.CommonDataKinds.Phone.SEARCH_PHONE_NUMBER_KEY, arrayListnumbers.get(0));
                                getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
                                Toast.makeText(getApplicationContext(), "Your Data got inserted.....!", Toast.LENGTH_SHORT).show();
//                        getContentResolver().insert(addContactsUri, values);
                            }
                        }
                    }
                }
            });

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

    private void Pickanumber(ArrayList<String> arrayListnumbers,int flag) {

        Log.e(TAG, "Pickanumber: your dialog function called " );
        AlertDialog.Builder builder= new AlertDialog.Builder(Detail.this);
        LayoutInflater inflater=getLayoutInflater();
        AlertDialog dialog;
        View view=inflater.inflate(R.layout.pickadata,null);
        LinearLayout linearLayout=view.findViewById(R.id.pickadatalayout);
        TextView textView=view.findViewById(R.id.pick_a_num_title);
        ListView listView=view.findViewById(R.id.pickanum_list);
        ArrayAdapter<String> adapterlist;
        adapterlist=new ArrayAdapter<String>(Detail.this,android.R.layout.simple_list_item_1,arrayListnumbers);
        listView.setAdapter(adapterlist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemClick: postion of Pickanumber list:"+arrayListnumbers.get(position) );
                if (flag==1){

                    String num = "tel:" + arrayListnumbers.get(position);
                    final Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(num));
                    startActivity(intent);
                }else if (flag==2){
//                    adapter1=new Adapter(getApplicationContext(),20);
                    //this is for sms
                    OpenDialog(arrayListnumbers.get(position), 1, getApplicationContext(),null);
                }else if (flag==3){
                    //this is for WP
                    OpenDialog(arrayListnumbers.get(position),3,getApplicationContext(),null);
                }else if (flag==4){
                    //this is for Email
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    OpenDialog(arrayListnumbers.get(position),2,getApplicationContext(),null);
                }
                else if (flag==5){
                    // this if for map
                    textView.setText("Pick a Address");
//                    i=new Intent(getApplicationContext(),MapsActivity.class);
//                    i.putExtra("address",arrayListnumbers.get(position));
//                    startActivity(i);
                    double latitude = 0;
                    double logitude = 0;

                    try {
                        geocoder=new Geocoder(getApplicationContext());
                        addresses=new ArrayList<>();
                        addresses=geocoder.getFromLocationName(arrayListnumbers.get(position),5);

                        Address locaton=addresses.get(0);
                        latitude=locaton.getLatitude();
                        logitude=locaton.getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String add="http://maps.google.com/maps?q="+latitude+","+logitude+"(Hey you are here!)&iwloc=A&hl=es";
                    Log.e(TAG, "onItemClick:this is your selected address==> "+add );
                    Uri gmmIntentUri = Uri.parse(add);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);


                }
            }
        });

        builder.setNegativeButton("cancel",(dialog1, which) -> {dialog1.cancel();});
        dialog=builder.create();
        dialog.setView(view);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }
    private void PickaEmail(ArrayList<String> arrayofemails){
        Log.e(TAG, "PickaEmail: ==>"+arrayofemails );
        AlertDialog.Builder builder= new AlertDialog.Builder(Detail.this);
        LayoutInflater inflater=getLayoutInflater();

        View view=inflater.inflate(R.layout.pickadata,null);
        LinearLayout linearLayout=view.findViewById(R.id.pickadatalayout);
        TextView textView=view.findViewById(R.id.pick_a_num_title);
        textView.setText("Select Email");
        ListView listView=view.findViewById(R.id.pickanum_list);
        ArrayAdapter<String> adapterlist;
        adapterlist=new ArrayAdapter<String>(Detail.this,android.R.layout.simple_list_item_multiple_choice,arrayofemails);
        listView.setAdapter(adapterlist);
        ArrayList<String> arrayList = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position>-1) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                OpenDialog(arrayListnumbers.get(position),2,getApplicationContext());
//            }
//        });
        builder.setNegativeButton("cancel",(dialog1, which) -> {dialog1.cancel();});

        builder.setPositiveButton("submit",(dialog1, which) -> {
            Log.e(TAG, "PickaEmail: " );
            SparseBooleanArray checked=listView.getCheckedItemPositions();
            if (checked.size()>0){
                for(int i=0;i<checked.size();i++){
                    if (checked.valueAt(i)==true){
                        arrayList.add(arrayofemails.get(i));
                    }
                }
            }
            Log.e(TAG, "PickaEmail: we are inside of submit button "+arrayList);
            OpenDialog("",2,getApplicationContext(),arrayList);
        });
        dialog=builder.create();
        dialog.setView(view);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
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

    private ArrayList<String> fetchAddress(String hiddenAddress, ArrayList<String> arrayListaddress) {
        JSONArray array= null;
        Log.e(TAG, "fetchAddress: this functon called...! ");
        try {
            array = (JSONArray) multi_fields.get(hiddenAddress);
            Log.e(TAG, "fetchAddress:array==> "+array );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject object= null;
        for (int i=0;i<array.length();i++){
            try {
                object = array.getJSONObject(i);
                Log.e(TAG, "fetchAddress: "+object );
                String add=object.getString("street")+" "+object.get("city")+", "+object.get("state")+", "+object.get("country")+", "+object.get("postal_code");
                Log.e(TAG, "fetchAddress:the address is==> "+add );
                arrayListaddress.add(add);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayListaddress;
    }
    private ArrayList<String> fetchMultiValued(String s,ArrayList<String> arrayList,String valetoadd){
        try {
            JSONArray array= (JSONArray) multi_fields.get(s);
            for (int i=0;i<array.length();i++) {
                JSONObject object = array.getJSONObject(i);
                Log.e(TAG, "detailtabrequest:json object in for loop==> "+object+"||===>"+ object.get(valetoadd) );
                arrayList.add(object.getString(valetoadd));
            }
            Log.e(TAG, "onClick:hiddenPhone "+array );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
    private void relatetabreqest(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, response -> {

        }, error -> {

        }) {
        };
    }
    public void OpenDialog(String position, int flag, Context context, ArrayList<String> arrayList) {
        /*here flag
            1 for Sms
            2 for Email
            3. for Wp
         */

        TextInputEditText editText, subject;
        TextInputLayout subject_layout;
        Button button;
        String title = "";
        String title2 = "Send";
        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.send_message_dialog, null);
        editText = view.findViewById(R.id.Messageedit);
        subject = view.findViewById(R.id.subjecttext);
        subject_layout = view.findViewById(R.id.emaildialogmessage);
        button = view.findViewById(R.id.dialog_button);
        Log.e(TAG, "onCreateDialog: >>> flag" + flag);
        builder.setView(view);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    button.setEnabled(false);
                }else {
                    dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    button.setEnabled(true);
                }}

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText.addTextChangedListener(textWatcher);
        if (flag == 1) {
            subject_layout.setVisibility(View.GONE);
            title = "Message";
            title2 = "Send via Manager";
        } else if (flag == 2) {
            button.setVisibility(View.GONE);
            title = "Email";
        } else if (flag == 3) {
            subject_layout.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            title = "Whatsapp";
        }
        builder.setTitle(title);
        button.setEnabled(false);
        button.setOnClickListener(v -> {
            String num = "sms:" + position;
            String s = editText.getText().toString();
            final Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra("sms_body", s);
            intent.setType("vnd.android-dir/mms-sms");
            intent.setData(Uri.parse(num));
            startActivity(intent);
        });
        builder.setPositiveButton(title2, (dialog, which) -> {
            Boolean wantToCloseDialog = false;
            String s = editText.getText().toString();
            Log.e(TAG, "onClick: s==>" + s);
            if (s.isEmpty()) {
                Log.e(TAG, "onClick: s==>" + s);
                editText.setError("Empty");
                editText.setFocusable(true);
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                Toast.makeText(context, "Give the input", Toast.LENGTH_LONG).show();
            } else {
                wantToCloseDialog = true;

                if (flag == 1) {
                    //sms
                    String num = "sms:" + position;
                    Log.e(TAG, "onClick: sms" + position);
                    smsBySmsManager(position,s);
                } else if (flag == 2) {
                    //Email
                    String subjectm = subject.getText().toString();
                    String message = editText.getText().toString();
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("`message/rfc822`");
                    final PackageManager pm = context.getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
                    ResolveInfo best = null;
                    for (final ResolveInfo info : matches)
                        if (info.activityInfo.packageName.endsWith(".gm") ||
                                info.activityInfo.name.toLowerCase().contains("gmail"))
                            best = info;
                    if (best != null)
                        intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

//                    String [] strings=new String[arrayList.size()];
//                    for (int a=0 ;a<arrayList.size();a++){
//                        strings[a]=arrayList.get(a);
//                    }
                    Log.e(TAG, "OpenDialog: "+arrayList);

                    StringBuilder out = new StringBuilder();
                    for (Object o:arrayList){
                        out.append(o.toString());
                        out.append(",");
                    }
                    Log.e(TAG, "OpenDialog: ==> "+out);
                    intent.putExtra(intent.EXTRA_EMAIL, new String[]{out.toString()});
                    intent.putExtra(intent.EXTRA_SUBJECT, subjectm);
                    intent.putExtra(intent.EXTRA_TEXT, message);
                    startActivity(intent);
                } else if (flag == 3) {
                    String text = editText.getText().toString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    PackageManager pm = context.getPackageManager();
                    try {
                        String url = "https://api.whatsapp.com/send?phone=+91" + position + "&text=" + URLEncoder.encode(text, "UTF-8");
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        if (i.resolveActivity(pm) != null) {
                            startActivity(i);
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
            Log.e("parthi", "--->" + wantToCloseDialog);
            if (wantToCloseDialog) {
                dialog.dismiss();
            }

        });
        dialog1 = builder.create();
        dialog1.show();
        dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }
    private void smsBySmsManager(String phone,String message){


// --sends an SMS message to another device---
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

//---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

//---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, message, sentPI, deliveredPI);

/***************************************/

    }
}