package com.priyanka.newuat_demo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.R;
import com.priyanka.newuat_demo.fragment.Details_frag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Detailsadapter extends ArrayAdapter<ArrayList<String>>{

    private static final String TAG = "Detailsadapter";
    Context context;
    ArrayList<String> keyset;
    ArrayList<String> values;
    String module;
    Databasehelper db;
    Details_frag.NameValue nameValue;


    public Detailsadapter(String module,@NonNull Context context, int resource, ArrayList<String> keySet, ArrayList<String> listOfValues) {
        super(context, resource);
        this.context=context;
//        this.hashMapArrayList=map;
        this.keyset=keySet;
        this.values=listOfValues;
        this.module=module;
//        Log.e(TAG, "Detailsadapter:you are in these module====> "+module );
        Log.e(TAG, "Detailsadapter: "+"Details_Adapter created" );
    }

    @Override
    public int getCount() {
        Log.e(TAG, "getCount:keysetsize==> "+keyset.size() +"||valueset||"+values.size());
        return keyset.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.e(TAG, "getView: ");
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        convertView=layoutInflater.inflate(R.layout.details_list_holder,parent,false);
        ItemViewHlder viewHlder=new ItemViewHlder(convertView);
        db=new Databasehelper(context);
//        String map=hashMapArrayList.get("name");
//        Set<String> keySet = hashMapArrayList.keySet();
//        Log.e(TAG, "getView: map===> "+hashMapArrayList);

        //Sending value to display in card
        if (keyset.get(position).equals("name")){
            //nameValue.getname(values.get(position));
        }


        Log.e(TAG, "getView:position==> "+position );
        String key= null;
        try {
            key = getDisplayNames(keyset.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        viewHlder.key.setText(key);
        viewHlder.value.setText(values.get(position));

        return convertView;
    }
    private class ItemViewHlder extends View{
        TextView key;
        TextView value;

        public ItemViewHlder(@NonNull View itemView) {
            super(context);
            key=itemView.findViewById(R.id.card_key);
            value=itemView.findViewById(R.id.card_value);
        }
    }
    private String getDisplayNames(String key) throws JSONException {
        String fielddefs=db.getFielddefs(module);
        String displayname = null;
//        Log.e(TAG, "getDisplayNames:===> "+fielddefs );
        JSONArray jsonArray=new JSONArray(fielddefs);
        for (int i=0;i<jsonArray.length();i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object.get("name").equals(key)) {
                Log.e(TAG, "getDisplayNames:key "+key );
                Log.e(TAG, "getDisplayNames:object===> " + object.get("display_label"));

                displayname=object.optString("display_label");
            }
        }
        return displayname;
    }
}
