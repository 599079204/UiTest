package com.ljw.uitest.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ljw.uitest.BaseActivity;
import com.ljw.uitest.R;
import com.ljw.uitest.menu.vpAndFrag.ActVpAndFrag;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.rv_content)
    RecyclerView mRvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, true);
        mRvContent.setLayoutManager(layoutManager);
        RvBtnAdapter rvBtnAdapter = new RvBtnAdapter(this, obtain());
        mRvContent.setAdapter(rvBtnAdapter);
    }

    private ArrayList<JumpEntity> obtain() {
        ArrayList<JumpEntity> list = new ArrayList<>();

        list.add(new JumpEntity("Viewpager中包含fragment", ActVpAndFrag.class));

        return list;
    }
}