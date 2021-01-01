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

import com.priyanka.newuat_demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Detailsadapter extends ArrayAdapter{

    private static final String TAG = "Detailsadapter";
    public HashMap<String,String> hashMapArrayList;
    Context context;
    ArrayList<String> keyset;
    ArrayList<String> values;

    public Detailsadapter(@NonNull Context context, int resource, ArrayList<String> keySet, ArrayList<String> listOfValues) {
        super(context, resource);
        this.context=context;
//        this.hashMapArrayList=map;
        this.keyset=keySet;
        this.values=listOfValues;
        Log.e(TAG, "Detailsadapter: "+"Details_Adapter created" );
    }

    @Override
    public int getCount() {
//        Log.e(TAG, "getCount:hashmapsize "+hashMapArrayList.size() );
        return keyset.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        convertView=layoutInflater.inflate(R.layout.details_list_holder,parent,false);
        ItemViewHlder viewHlder=new ItemViewHlder(convertView);
//        String map=hashMapArrayList.get("name");
//        Set<String> keySet = hashMapArrayList.keySet();
//        Log.e(TAG, "getView: map===> "+hashMapArrayList);
        Log.e(TAG, "getView:position==> "+position );
        viewHlder.key.setText(keyset.get(position));
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
}
