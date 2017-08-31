package com.ljw.uitest.menu.vpConFrag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ViewPager包含Fragment时,使用的适配器
 * Created by ljw on 2017/8/30.
 */
public abstract class VpConFragAdapter2<Frag extends Fragment> extends FragmentStatePagerAdapter {

    // 保存viewpager中的标题与Fragment对
    private ArrayList<TitleFragPair<Frag>> mTitleAndFrag = new ArrayList<>();

    VpConFragAdapter2(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleAndFrag.get(position).title;
    }

    @Override
    public Frag getItem(int position) {
        TitleFragPair<Frag> pair = mTitleAndFrag.get(position);

        if (!TextUtils.isEmpty(pair.title) && pair.frag == null)
            pair.frag = autoCreateFrag(pair.fragName);

        return pair.frag;
    }

    protected abstract Frag autoCreateFrag(String fragName);

    @Override
    public int getCount() {
        return mTitleAndFrag.size();
    }

    public final VpConFragAdapter2 addItem(String vpTitle, String fragName) {
        if (!TextUtils.isEmpty(vpTitle))
            mTitleAndFrag.add(new TitleFragPair<>(vpTitle, fragName));
        return this;
    }

    public final boolean isEmpty() {
        return mTitleAndFrag.size() == 0;
    }

    public final void clear() {
        mTitleAndFrag.clear();
        notifyDataSetChanged();
    }

    public static class TitleFragPair<T> {
        String title;

        T frag;

        String fragName = "";

        TitleFragPair(String title, String fragName) {
            this.title = title;
            this.fragName = fragName;
        }
    }
}
