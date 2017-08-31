package com.ljw.uitest.menu.vpConFrag;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.ljw.uitest.BaseActivity;
import com.ljw.uitest.R;
import com.ljw.uitest.customView.PagerSlidingTabStrip;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * a
 * Created by ljw on 2017/8/30.
 */

public class ActVpConFrag extends BaseActivity {

    @BindView(R.id.vp_title)
    PagerSlidingTabStrip mVpTitle;
    @BindView(R.id.vp_content)
    ViewPager mVpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_vp_frag);
        ButterKnife.bind(this);

        // 构建并设置viewpager的适配器
        /*VpConFragAdapter<FragTest1> vpConFragAdapter = new VpConFragAdapter<>(getSupportFragmentManager());
        vpConFragAdapter.addItem("标题1", FragTest1.newInstance("fragment1"));
        vpConFragAdapter.addItem("标题2", FragTest1.newInstance("fragment2"));
        vpConFragAdapter.addItem("标题3", FragTest1.newInstance("fragment3"));
        vpConFragAdapter.addItem("标题4", FragTest1.newInstance("fragment4"));
        vpConFragAdapter.addItem("标题5", FragTest1.newInstance("fragment5"));
        vpConFragAdapter.addItem("标题6", FragTest1.newInstance("fragment6"));
        vpConFragAdapter.addItem("标题7", FragTest1.newInstance("fragment7"));
        vpConFragAdapter.addItem("标题8", FragTest1.newInstance("fragment8"));
        vpConFragAdapter.addItem("标题9", FragTest1.newInstance("fragment9"));
        vpConFragAdapter.addItem("标题10", FragTest1.newInstance("fragment10"));
        mVpContent.setAdapter(vpConFragAdapter);
        mVpContent.setOffscreenPageLimit(2);
        mVpTitle.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        mVpTitle.setViewPager(mVpContent);*/

        VpConFragAdapter2 vpConFragAdapter2 = new VpConFragAdapter2<FragTest1>(getSupportFragmentManager()) {
            @Override
            protected FragTest1 autoCreateFrag(String fragName) {
                return FragTest1.newInstance(fragName);
            }
        };
        mVpContent.setAdapter(vpConFragAdapter2);
        mVpContent.setOffscreenPageLimit(2);
        vpConFragAdapter2.addItem("标题1", "fragment 1")
                .addItem("标题2", "fragment 2")
                .addItem("标题3", "fragment 3")
                .addItem("标题4", "fragment 4")
                .addItem("标题5", "fragment 5")
                .addItem("标题6", "fragment 6")
                .addItem("标题7", "fragment 7")
                .addItem("标题8", "fragment 8")
                .addItem("标题9", "fragment 9")
                .addItem("标题0", "fragment 10");
        vpConFragAdapter2.notifyDataSetChanged();
        mVpTitle.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        mVpTitle.setViewPager(mVpContent);
    }
}
