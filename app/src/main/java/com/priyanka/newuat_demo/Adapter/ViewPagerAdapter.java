package com.priyanka.newuat_demo.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.priyanka.newuat_demo.Related_frag;
import com.priyanka.newuat_demo.fragment.AccountFragment;
import com.priyanka.newuat_demo.fragment.Details_frag;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "view pager";
    String id,module;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, String id, String module) {
        super(fm, behavior);
        this.id=id;
        this.module=module;
        Log.e(TAG, "ViewPagerAdapter: "+"viewpager created" +id+"||==>"+module);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new Details_frag(id,module);
        }
        else if (position == 1)
        {
            fragment = new Related_frag(id,module);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
