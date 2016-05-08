package com.king.applock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.applock.R;
import com.king.applock.bean.AppInfo;
import com.king.applock.dao.AppLockDao;

import java.util.List;


public class AppLockReAdapter extends RecyclerView.Adapter<AppLockReAdapter.ViewHolder> {

    private Context context;
    private static List<AppInfo> data;
    private AppLockDao mDao;
    private boolean isLock = false;

    public AppLockReAdapter(Context context, List<AppInfo> infos) {
        this.context = context;
        this.data = infos;
        mDao = new AppLockDao(context);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivIcon, ivLock;
        RelativeLayout itemRl;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.item_applock_icon);
            ivLock = (ImageView) itemView.findViewById(R.id.item_applock_lock);
            tvName = (TextView) itemView.findViewById(R.id.item_applock_name);
            itemRl = (RelativeLayout) itemView.findViewById(R.id.item_rl);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_app_lock, parent, false));
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final AppInfo info = data.get(position);

        if (info == null) {
            return;
        }

        holder.ivIcon.setImageDrawable(info.icon);
        holder.tvName.setText(info.lable);

        if (isLock) {
            holder.ivLock.setImageResource(R.drawable.app_unlock_item_selector);
        } else {
            holder.ivLock.setImageResource(R.drawable.app_lock_item_selector);
        }

        // 点击动画
        holder.ivLock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = null;
                if (animation == null)
                    animation = AnimationUtils.loadAnimation(context, R.anim.item_lock_out);

                holder.itemRl.startAnimation(animation);

                animation.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        holder.ivLock.setEnabled(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {


                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        data.remove(info);
                        if (isLock) {// 已加锁app
                            //删除数据库
                            System.out.print(info.packageName);

                            if (mDao.delete(info.packageName)) {
                                mListener.onDataChange(true, info);
                            }
                        } else {//未加锁
                            // 加入数据库
                            if (mDao.insert(info.packageName)) {
                                mListener.onDataChange(false, info);
                            }
                        }
                        // UI
                        holder.ivLock.setEnabled(true);
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {

        return data == null ? 0 : data.size();
    }

    public void showData(List<AppInfo> infos, boolean isLock) {
        this.isLock = isLock;
        data = infos;
    }

    OnDataChangeListener mListener;

    public void setOnDataChangeListener(OnDataChangeListener listener) {
        this.mListener = listener;
    }


    public interface OnDataChangeListener {
        void onDataChange(boolean isLock, AppInfo info);
    }
}
