package com.example.myapplication.holder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.VideoInfo;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerItemViewHolder extends RecyclerItemBaseHolder {

    public final static String TAG = "RecyclerViewList";

    protected Context context = null;

    @BindView(R.id.list_item_container)
    FrameLayout listItemContainer;

    @BindView(R.id.list_item_btn)
    ImageView listItemBtn;

    ImageView imageView;

    private GSYVideoHelper smallVideoHelper;

    private GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder;

    public RecyclerItemViewHolder(Context context, View v) {
        super(v);
        this.context = context;
        ButterKnife.bind(this, v);
        imageView = new ImageView(context);
    }

    public void onBind(final int position, VideoInfo info) {

        //增加封面
        Glide.with(context)
                .load(info.avatar)
                .placeholder(R.drawable.a)
                .error(R.drawable.b)
                .into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


        smallVideoHelper.addVideoPlayer(position, imageView, TAG, listItemContainer, listItemBtn);

        listItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallVideoHelper.setPlayPositionAndTag(position, TAG);
                getRecyclerBaseAdapter().notifyDataSetChanged();
                //listVideoUtil.setLoop(true);
                String url = info.feedurl;
                //listVideoUtil.setCachePath(new File(FileUtils.getPath()));

                gsySmallVideoHelperBuilder.setVideoTitle("title " + position).setUrl(url);

                smallVideoHelper.startPlay();

                //必须在startPlay之后设置才能生效
                //listVideoUtil.getGsyVideoPlayer().getTitleTextView().setVisibility(View.VISIBLE);
            }
        });
    }


    public void setVideoHelper(GSYVideoHelper smallVideoHelper, GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder) {
        this.smallVideoHelper = smallVideoHelper;
        this.gsySmallVideoHelperBuilder = gsySmallVideoHelperBuilder;
    }
}