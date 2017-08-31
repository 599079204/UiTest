package com.ljw.uitest.menu.fragPagerStateAdapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ljw.uitest.BaseActivity;
import com.ljw.uitest.R;
import com.ljw.uitest.customView.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 测试FragmentPagerStateAdapter的使用
 * 刘勇的PagerSlidingTabStrip有点小bug,滑动时，tab会出现跳动和闪烁
 * 本应该变暗变小的tab，出现先变亮再变暗，先变大再变小。
 * 本应该变亮变大的tab，出现先变暗再变亮，先变小再变大
 * <p>
 * Created by ljw on 2017/8/30.
 */

public class ActFPSA extends BaseActivity {

    @BindView(R.id.vp_title)
    PagerSlidingTabStrip mVpTitle;
    @BindView(R.id.vp_content)
    ViewPager mVpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fpsa);
        ButterKnife.bind(this);

        ArrayList<Fragment> fragments = new ArrayList<>();
        TestFragment f1 = TestFragment.newInstane("标题1", "fragment1");
        TestFragment f2 = TestFragment.newInstane("标题2", "fragment2");
        TestFragment f3 = TestFragment.newInstane("标题3", "fragment3");
        TestFragment f4 = TestFragment.newInstane("标题4", "fragment4");
        TestFragment f5 = TestFragment.newInstane("标题5", "fragment5");
        fragments.add(f1);
        fragments.add(f2);
        fragments.add(f3);
        //fragments.add(f4);
        //fragments.add(f5);
        mVpContent.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragments.get(position).getArguments().getString(TestFragment.TITLE);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
                Log.e("ljw", "销毁第 " + (position + 1) + " 个frag");
            }
        });
        mVpTitle.setViewPager(mVpContent);
    }

    public static class TestFragment extends Fragment {

        public static final String TITLE = "title";
        public static final String NAME = "name";

        public static TestFragment newInstane(String title, String name) {
            TestFragment fragment = new TestFragment();
            Bundle bundle = fragment.getArguments();
            if (bundle == null)
                bundle = new Bundle();

            bundle.putString(TITLE, title);
            bundle.putString(NAME, name);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container, @Nullable Bundle bundle) {

            View view = LayoutInflater.
                    from(getActivity()).
                    inflate(R.layout.frag_fpsa, container, false);
            TextView textView = view.findViewById(R.id.tv_frag_name);
            textView.setText(TextUtils.isEmpty(getArguments().getString(NAME)) ?
                    "hello world!" : getArguments().getString(NAME));
            return view;
        }
    }
}
