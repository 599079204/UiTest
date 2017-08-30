package com.ljw.uitest.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ljw.uitest.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ljw on 2017/8/30.
 */

public class RvBtnAdapter extends RecyclerView.Adapter<RvBtnAdapter.ViewHolder>
        implements View.OnClickListener {

    private Context mContext;
    private ArrayList<JumpEntity> mDataSource;

    public RvBtnAdapter(@NonNull Context context, @NonNull ArrayList<JumpEntity> dataSource) {
        mContext = context;
        mDataSource = dataSource;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_jump_act, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.btnJump.setText(mDataSource.get(position).btnText);
        holder.btnJump.setTag(position);
        holder.btnJump.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mDataSource == null ? 0 : mDataSource.size();
    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        mContext.startActivity(new Intent(mContext, mDataSource.get(position).jumpClass));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btn_jump)
        Button btnJump;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
