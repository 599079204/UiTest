package com.ljw.uitest.menu.vpConFrag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ljw.uitest.utils.KeyValuePair;

import java.util.ArrayList;

/**
 * ViewPager包含Fragment时,使用的适配器
 * Created by ljw on 2017/8/30.
 */
public class VpConFragAdapter<Frag extends Fragment> extends FragmentStatePagerAdapter {

    // 保存viewpager中的标题与Fragment对
    private ArrayList<KeyValuePair<String, Frag>> mTitleAndFrag = new ArrayList<>();

    VpConFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleAndFrag.get(position).key;
    }

    @Override
    public Frag getItem(int position) {
        return mTitleAndFrag.get(position).value;
    }

    @Override
    public int getCount() {
        return mTitleAndFrag.size();
    }

    public final void addItem(String vpTitle, Frag frag) {
        mTitleAndFrag.add(new KeyValuePair<>(vpTitle, frag));
    }

    public final boolean isEmpty() {
        return mTitleAndFrag.size() == 0;
    }

    public final void clear() {
        mTitleAndFrag.clear();
        notifyDataSetChanged();
    }
}
