package com.priyanka.newuat_demo.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.gson.JsonObject;
import com.priyanka.newuat_demo.Related_frag;
import com.priyanka.newuat_demo.fragment.AccountFragment;
import com.priyanka.newuat_demo.fragment.Details_frag;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "view pager";
    String id,module;
    Detailsadapter detailsadapter;
    ArrayList<String> listOfKeys;
    ArrayList<String> listOfValues;
    ArrayList<HashMap<String, String>> hashMapArrayList;
    private final String[] tabTitles = new String[]{"DETAILS", "RELATED"};
    JSONObject object;
    JSONArray teamData;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, String module, String id, JSONObject object, JSONArray teamData) {
        super(fm, behavior);
        Log.e(TAG, "ViewPagerAdapter: called " );
        this.id=id;
        this.module=module;
        this.object=object;
        this.teamData=teamData;
//        Log.e(TAG, "ViewPagerAdapter: "+"viewpager created" +id+"||==>"+module);
    }

    public ViewPagerAdapter(FragmentManager supportFragmentManager, int i, String module, String id, JSONObject object, ArrayList<HashMap<String, String>> hashMapArrayList) {
        super(supportFragmentManager, i);
        Log.e(TAG, "ViewPagerAdapter: called " );
        this.id=id;
        this.module=module;
        this.object=object;
        this.hashMapArrayList=hashMapArrayList;
    }

//    public ViewPagerAdapter(FragmentManager supportFragmentManager, int i, ArrayList<String> listOfKeys, ArrayList<String> listOfValues, String module) {
//        super(supportFragmentManager, i);
//        this.listOfKeys=listOfKeys;
//        this.listOfValues=listOfValues;
//        this.module=module;
//    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new Details_frag(module,id,object,teamData);
        }
        else if (position == 1)
        {
            fragment = new Related_frag(id,module,object);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//        Log.e(TAG, "getPageTitle: i am here to set yout tab titile " );
        return tabTitles[position];
    }
}
