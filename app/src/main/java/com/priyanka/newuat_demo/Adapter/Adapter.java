package com.priyanka.newuat_demo.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.priyanka.newuat_demo.Detail;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.R;
import com.priyanka.newuat_demo.SubModule.RelateFieldSelection;
import com.priyanka.newuat_demo.fragment.AccountFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class Adapter extends ArrayAdapter<HashMap<String, String>> implements ActivityCompat.OnRequestPermissionsResultCallback {

    String TAG = "Adapter";
    AccountFragment account;
    Databasehelper databasehelper;
    JSONArray object1;
    AlertDialog dialog;
    Activity activity;
    int resource;
    OnItemClickLister mlistener;
    String flag;
    String key;

    public Adapter(Context applicationContext, int i, ArrayList<HashMap<String, String>> map, String mParam1, Activity activity, String flag) {
        // used for multi relate field

        super(applicationContext, i);
        this.context = applicationContext;
        this.hashMapArrayList = map;
        this.mParam1 = mParam1;
        this.activity = activity;
        this.resource=i;
        this.flag=flag;

    }


    //interfaces
    public interface OnItemClickLister{
        void onItemClicked(String name);
    }


    public ArrayList<HashMap<String, String>> hashMapArrayList;
    Context context;
    String mParam1;
    String name,id;
    String displaytext1, displaytext2, displaytext3, displaytext4, displaytext5;

    public Adapter(@NonNull Context context, int e, ArrayList<HashMap<String, String>> map, String mParam1, Activity relateFieldSelection, String name, String key){
        super(context,e);
        this.context=context;
        this.hashMapArrayList = map;
        this.mParam1 = mParam1;
        this.activity = relateFieldSelection;
        this.resource=e;
        this.name=name;
        this.key=key;

    }

    public Adapter(@NonNull Context context, int resource, ArrayList<HashMap<String, String>> map, String s, Activity activity) {
        super(context, resource);
        this.context = context;
        this.hashMapArrayList = map;
        this.mParam1 = s;
        this.activity = activity;
        this.resource=resource;
    }

    @Override
    public int getCount() {
//        Log.e(TAG, "getCount: " + hashMapArrayList.size());
        return hashMapArrayList.size();
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.recycler_holder, parent, false);
//        Log.e(TAG, "getView: ==> Layout created");
        ItemViewHolder viewHolder = new ItemViewHolder(convertView);

        databasehelper = new Databasehelper(context);
        object1 = new JSONArray();
        account = new AccountFragment(mParam1);
//        Log.e(TAG, "position=>" + position);
        HashMap<String, String> map = hashMapArrayList.get(position);
        viewHolder.id=map.get("id");
//        Log.e(TAG, "getView: this is id which is toubling uhhh:"+id);
        object1 = account.selectedfield(databasehelper, mParam1);
//        Log.e(TAG, "getView:object1==> " + object1 + " param: " + mParam1);
        try {
            displaytext1 = getdisplayname(object1.getString(0));
            displaytext2 = getdisplayname(object1.getString(1));
            displaytext3 = getdisplayname(object1.getString(2));
            displaytext4 = getdisplayname(object1.getString(3));
            displaytext5 = getdisplayname(object1.getString(4));
//            Log.e(TAG, "getView:displaytext1: " + displaytext1);
//            Log.e(TAG, "getView:object1.getString(4): " + object1.getString(4));
            String user = null;
            try {
                user = object1.getString(4);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (user.endsWith("_id")) {
                try {
                    object1.put(4, "assigned_user_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            viewHolder.textView1.setText(displaytext1 + ": " + (map.containsKey(object1.getString(0)) ? map.get(object1.getString(0)) : ""));
            viewHolder.textView2.setText(displaytext2 + ": " + (map.containsKey(object1.getString(1)) ? map.get(object1.getString(1)) : ""));
            viewHolder.textView3.setText(displaytext3 + ": " + (map.containsKey(object1.getString(2)) ? map.get(object1.getString(2)) : ""));
            viewHolder.textView4.setText(displaytext4 + ": " + (map.containsKey(object1.getString(3)) ? map.get(object1.getString(3)) : ""));
            viewHolder.textView5.setText(displaytext5 + ": " + (map.containsKey(object1.getString(4)) ? map.get(object1.getString(4)) : ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.e(TAG, "getView: you got a textview at postion==> " + position);

        viewHolder.imageView1.setOnClickListener(v -> {
            try {
                if (object1.getString(3).equals("phone")) {
//                    Log.e(TAG, "getView:map.get(object1.getString(3))=> " + map.get(object1.getString(3)));
                    if (!(map.get(object1.getString(3)).equals(""))) {
                        String num1 = map.get(object1.getString(3));
                        if (checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Log.e(TAG, "call permission: not granted ");
                            requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 10);
                        } else if (shouldShowRequestPermissionRationale((Activity) activity, Manifest.permission.CALL_PHONE)) {
                            requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},
                                    10);
                        } else {
                            String num = "tel:" + num1;
                            final Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse(num));
                            activity.startActivity(intent);
                        }
                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        viewHolder.imageView2.setOnClickListener(v -> {
            try {
                if (object1.getString(2).equals("email")) {
                    if (!(map.get(object1.getString(2)).equals(""))) {
                        String email = map.get(object1.getString(2));
                        Log.e(TAG, "getView:email " + email);
                        OpenDialog(email, 2, context,activity);
                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        viewHolder.imageView3.setOnClickListener(v -> {
            try {
                if (object1.getString(3).equals("phone")) {
                    if (!(map.get(object1.getString(3)).equals(""))) {
                        String num1 = map.get(object1.getString(3));
                        OpenDialog(num1, 3, context,activity);
                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        viewHolder.imageView4.setOnClickListener(v -> {
            try {
                if (object1.getString(3).equals("phone")) {
                    if (!(map.get(object1.getString(3)).equals(""))) {
                        String num = object1.getString(3);
                        if (checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED &&
                                shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)) {
                            Log.e(TAG, "Sms Permission: not granted ");
                            requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, 11);
                        } else if (shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)) {
                            new AlertDialog.Builder(context)
                                    .setMessage("permission required to send messages")
                                    .setPositiveButton("OK", null).show();
                        } else {
                            OpenDialog(num, 1, context,activity);
                        }
                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        viewHolder.imageView5.setOnClickListener(v -> {
            //code for editiong
            
        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resource==20){
                    Log.e(TAG, "onClick: requestcode 20" );
                    Log.e(TAG, "onClick: hey you clicked card view " );
                    Log.e(TAG, "onClick: this is the id you search for ===>"+viewHolder.id);
                    Intent i =new Intent(activity, Detail.class);
                    i.putExtra("id",viewHolder.id);
                    Log.e(TAG, "this is the module i was searching for :"+mParam1);
                    i.putExtra("module_name",mParam1);
                    context.startActivity(i);
                }
                else if (resource==10){

                    //default case of relate field
                    Log.e(TAG, "onClick: request code 10" );
                    Log.e(TAG, "onClick: hey you clicked me==:)"+ viewHolder.textView1.getText().toString());
                    Intent intent=new Intent();
                    String s=viewHolder.textView1.getText().toString().substring(5);
                    intent.putExtra("name",s);
                    intent.putExtra("id",viewHolder.id);
                    intent.putExtra("module",mParam1);
                    intent.putExtra("flag",name);
                    intent.putExtra("key",key);

//                    startActivityForResult(activity,intent,10,null);
                    activity.setResult(Activity.RESULT_OK,intent);
                    activity.finish();

                    if (mlistener != null){
                        mlistener.onItemClicked( viewHolder.textView1.getText().toString());
                    }
                }else if (resource==12){
                    Log.e(TAG, "onClick: request code 12" );
                    Intent intent=new Intent();
                    String s=viewHolder.textView1.getText().toString().substring(5);
                    intent.putExtra("name",s);
                    intent.putExtra("module",mParam1);
                    intent.putExtra("id",viewHolder.id);

                    activity.setResult(Activity.RESULT_OK,intent);
                    activity.finish();
                }
                else if (resource==20){
                    Intent intent=new Intent();
                }
            }
        });


        return convertView;
    }

    private String getdisplayname(String string) throws JSONException {
        String displayname = "";
        String fielddefs = databasehelper.getdisplaylabel(mParam1);
//        Log.e(TAG, "getdisplayname: " + fielddefs);
        JSONArray object = new JSONArray(fielddefs);
//        Log.e(TAG, "getdisplayname:object " + object);
        for (int i = 0; i < object.length(); i++) {
            JSONObject object1 = object.getJSONObject(i);
//            Log.e(TAG, "getdisplayname:object1 " + object1);

            String name = object1.optString("name");
//            Log.e(TAG, "getdisplayname:name " + name);
            if (name.equals(string)) {
                displayname = object1.optString("display_label");
            }
        }
        return displayname;
    }

    private class ItemViewHolder extends View {
        String id;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        ImageView imageView5;
        ImageView imageView6;
        CardView cardView;

        public ItemViewHolder(@NonNull View itemView) {
            super(context);
            textView1 = itemView.findViewById(R.id.text);
            textView2 = itemView.findViewById(R.id.text2);
            textView3 = itemView.findViewById(R.id.text3);
            textView4 = itemView.findViewById(R.id.text4);
            textView5 = itemView.findViewById(R.id.text5);
            imageView1 = itemView.findViewById(R.id.image1);
            imageView2 = itemView.findViewById(R.id.image2);
            imageView3 = itemView.findViewById(R.id.image3);
            imageView4 = itemView.findViewById(R.id.image4);
            imageView5 = itemView.findViewById(R.id.image5);
            imageView6 = itemView.findViewById(R.id.image6);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }

    public void OpenDialog(String position, int flag, Context context,Activity activity) {

        TextInputEditText editText, subject;
        TextInputLayout subject_layout;
        Button button;
        String title = "";
        String title2 = "Send";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();
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
                if (s.length() == 0) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    button.setEnabled(false);
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    button.setEnabled(true);
                }
            }

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
            context.startActivity(intent);
        });
        builder.setPositiveButton(title2, (dialog, which) -> {
            Boolean wantToCloseDialog = false;
            String s = editText.getText().toString();
            Log.e(TAG, "onClick: s==>" + s);
            if (s.isEmpty()) {
                Log.e(TAG, "onClick: s==>" + s);
                editText.setError("Empty");
                editText.setFocusable(true);
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                Toast.makeText(context, "Give the input", Toast.LENGTH_LONG).show();
            } else {
                wantToCloseDialog = true;

                if (flag == 1) {
                    //sms
                    String num = "sms:" + position;
                    Log.e(TAG, "onClick: sms" + position);
                    smsBySmsManager(position, s);
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
                    intent.putExtra(intent.EXTRA_EMAIL, new String[]{position});
                    intent.putExtra(intent.EXTRA_SUBJECT, subjectm);
                    intent.putExtra(intent.EXTRA_TEXT, message);
                    context.startActivity(intent);
                } else if (flag == 3) {
                    String text = editText.getText().toString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    PackageManager pm = context.getPackageManager();
                    try {
                        String url = "https://api.whatsapp.com/send?phone=+91" + position + "&text=" + URLEncoder.encode(text, "UTF-8");
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        if (i.resolveActivity(pm) != null) {
                            context.startActivity(i);
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
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    private void smsBySmsManager(String phone, String message) {


// --sends an SMS message to another device---
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);

//---when the SMS has been sent---
        activity.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

//---when the SMS has been delivered---
        activity.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, message, sentPI, deliveredPI);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult: this function called");
        onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length > 0) {

            for (int i = 0; i < permissions.length; i++) {

                Log.e("parthi", "permissions===>" + permissions[i]);
                Log.e("parthi", "grantResults===>" + grantResults[i]);
                switch (permissions[i]) {

                    case Manifest.permission.SEND_SMS:
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        } else if (shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)) {
                            new AlertDialog.Builder(activity)
                                    .setMessage("permission required to send messages")
                                    .setPositiveButton("OK", null).show();
                        } else {

                            toRedirectToPermissionPage();
                        }
                        break;


                    case Manifest.permission.CALL_PHONE:
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                            doCall(phoneNo);
                        } else if (shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
                            new AlertDialog.Builder(activity)
                                    .setMessage("permission required to call")
                                    .setPositiveButton("OK", null).show();
                        } else {

                            toRedirectToPermissionPage();
                        }
                        break;
                    default:
                        break;

                }
            }

        }
    }

    private void toRedirectToPermissionPage() {

        new AlertDialog.Builder(activity)
                .setMessage("permission required to send messages")
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    Log.e(TAG, "onRequestPermissionsResult: " + activity.getPackageName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(uri);
                    activity.startActivity(intent);
                }).show();
    }
}
