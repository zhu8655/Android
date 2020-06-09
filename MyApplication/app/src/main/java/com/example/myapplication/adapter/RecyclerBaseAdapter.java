package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.VideoInfo;
import com.example.myapplication.holder.RecyclerItemViewHolder;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerBaseAdapter extends RecyclerView.Adapter {

    private final static String TAG = "RecyclerBaseAdapter";

    private List<VideoInfo> dataList = null;

    private Context context = null;

    private GSYVideoHelper smallVideoHelper;

    private GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder;

    public RecyclerBaseAdapter(Context context, List<VideoInfo> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_video_item, parent, false);
        final RecyclerView.ViewHolder holder = new RecyclerItemViewHolder(context, v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        RecyclerItemViewHolder recyclerItemViewHolder = (RecyclerItemViewHolder) holder;
        recyclerItemViewHolder.setVideoHelper(smallVideoHelper, gsySmallVideoHelperBuilder);
        recyclerItemViewHolder.setRecyclerBaseAdapter(this);
        recyclerItemViewHolder.onBind(position, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    public void setListData(List<VideoInfo> data) {
        dataList = data;
        notifyDataSetChanged();
    }

    public GSYVideoHelper getVideoHelper() {
        return smallVideoHelper;
    }

    public void setVideoHelper(GSYVideoHelper smallVideoHelper, GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder) {
        this.smallVideoHelper = smallVideoHelper;
        this.gsySmallVideoHelperBuilder = gsySmallVideoHelperBuilder;
    }
}
