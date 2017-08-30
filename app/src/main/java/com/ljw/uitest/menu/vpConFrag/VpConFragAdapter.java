package com.ljw.uitest.menu.vpConFrag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.ArrayMap;
import android.view.ViewGroup;

import com.ljw.uitest.utils.KeyValuePair;

import java.util.ArrayList;

/**
 * ViewPager包含Fragment时,使用的适配器
 * Created by ljw on 2017/8/30.
 */

public abstract class VpConFragAdapter<T, Frag extends Fragment> extends FragmentStatePagerAdapter {

    private ArrayList<KeyValuePair<String, T>> mItems = new ArrayList<>();

    private ArrayMap<Integer, Frag> mFragments = new ArrayMap<>();

    public VpConFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }


}
