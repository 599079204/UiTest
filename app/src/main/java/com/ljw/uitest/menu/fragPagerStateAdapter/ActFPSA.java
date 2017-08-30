package com.ljw.uitest.menu.fragPagerStateAdapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ljw.uitest.BaseActivity;
import com.ljw.uitest.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ljw.uitest.menu.fragPagerStateAdapter.ActFPSA.TestFragment.KEY_TEXT;

/**
 * 测试FragmentPagerStateAdapter的使用
 * <p>
 * Created by ljw on 2017/8/30.
 */

public class ActFPSA extends BaseActivity {

    @BindView(R.id.rv_content)
    ViewPager mVpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fpsa);
        ButterKnife.bind(this);

        ArrayList<Fragment> fragments = new ArrayList<>();
        TestFragment f1 = new TestFragment();
        f1.getArguments().putString(KEY_TEXT, "111111");
        TestFragment f2 = new TestFragment();
        f2.getArguments().putString(KEY_TEXT, "222222");
        TestFragment f3 = new TestFragment();
        f3.getArguments().putString(KEY_TEXT, "333333");
        TestFragment f4 = new TestFragment();
        f4.getArguments().putString(KEY_TEXT, "444444");
        TestFragment f5 = new TestFragment();
        f5.getArguments().putString(KEY_TEXT, "555555");
        fragments.add(f1);
        fragments.add(f2);
        fragments.add(f3);
        fragments.add(f4);
        fragments.add(f5);

        mVpContent.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    public static class TestFragment extends Fragment {

        public static final String KEY_TEXT = "key_text";

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container, @Nullable Bundle bundle) {
            TextView textView = new TextView(getActivity());
            String text = TextUtils.isEmpty(getArguments().getString(KEY_TEXT)) ?
                    "hello world!" : getArguments().getString(KEY_TEXT);
            textView.setText(text);
            return textView;
        }
    }
}
