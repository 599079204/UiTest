package com.ljw.uitest.menu.vpConFrag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ljw.uitest.R;

/**
 * c
 * Created by ljw on 2017/8/31.
 */

public class FragTest1 extends Fragment {

    public static final String ARGU_NAME = "fragment_name";

    public static FragTest1 newInstance(String fragName) {
        Log.e("ljw", "创建 : " + fragName);
        FragTest1 fragment = new FragTest1();
        Bundle bundle = fragment.getArguments();
        if (bundle == null)
            bundle = new Bundle();

        bundle.putString(ARGU_NAME, fragName);
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
        textView.setText(TextUtils.isEmpty(getArguments().getString(ARGU_NAME)) ?
                "hello world!" : getArguments().getString(ARGU_NAME));
        return view;
    }
}
