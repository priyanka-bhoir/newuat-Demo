package com.priyanka.newuat_demo.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.Models.User;
import com.priyanka.newuat_demo.R;
import com.priyanka.newuat_demo.fragment.Account;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.namespace.QName;

import static android.content.ContentValues.TAG;

public class Adapter extends ArrayAdapter<HashMap<String, String>> {

    String TAG="Adapter";
    Account account;
    Databasehelper databasehelper;

    public ArrayList<HashMap<String,String>> hashMapArrayList;
    Activity activity;
    Context context;
    String mParam1;
   
    public Adapter(@NonNull Context context, int resource,ArrayList<HashMap<String, String>> map,String s) {
        super(context, resource);
        this.context=context;
        this.hashMapArrayList=map;
        this.mParam1=s;
    }

    @Override
    public int getCount() {
        Log.e(TAG, "getCount: "+hashMapArrayList.size() );
        return hashMapArrayList.size();
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        convertView =layoutInflater.inflate(R.layout.recycler_holder,parent,false);
        Log.e(TAG, "getView: ==> Layout created" );
        ItemViewHolder viewHolder = new ItemViewHolder(convertView);
        databasehelper=new Databasehelper(context);
        account=new Account(mParam1);
        Log.e(TAG,"position=>"+position);
        HashMap<String,String> map=hashMapArrayList.get(position);
        JSONArray object1= new JSONArray();
        object1=account.selectedfield(databasehelper,mParam1);
        Log.e(TAG, "getView:object1==> "+object1 );

        try {
            viewHolder.textView1.setText(object1.getString(0)+":"+map.get(object1.getString(0)));
            viewHolder.textView2.setText(object1.getString(1)+":"+map.get(object1.getString(1)));
            viewHolder.textView3.setText(object1.getString(2)+":"+map.get(object1.getString(2)));
            viewHolder.textView4.setText(object1.getString(3)+":"+map.get(object1.getString(3)));
            viewHolder.textView5.setText(object1.getString(4)+":"+map.get(object1.getString(4)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "getView: you got a textview at postion==> "+position);
        return  convertView;
    }

    private class ItemViewHolder extends View {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;

        public ItemViewHolder(@NonNull View itemView) {
            super(context);
             textView1=itemView.findViewById(R.id.text);
             textView2=itemView.findViewById(R.id.text2);
             textView3=itemView.findViewById(R.id.text3);
             textView4=itemView.findViewById(R.id.text4);
             textView5= itemView.findViewById(R.id.text5);
        }
    }
}
