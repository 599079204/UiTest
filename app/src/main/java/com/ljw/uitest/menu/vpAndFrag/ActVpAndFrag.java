package com.ljw.uitest.menu.vpAndFrag;

import android.os.Bundle;

import com.ljw.uitest.BaseActivity;
import com.ljw.uitest.R;

import butterknife.ButterKnife;

/**
 * Created by ljw on 2017/8/30.
 */

public class ActVpAndFrag extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        ButterKnife.bind(this);
    }
}
