package com.example.myapplication;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.transition.Explode;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.myapplication.adapter.RecyclerBaseAdapter;
import com.example.myapplication.holder.RecyclerItemViewHolder;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list_item_recycler)
    RecyclerView listItemRecycler;

    @BindView(R.id.video_full_container)
    FrameLayout videoFullContainer;

    LinearLayoutManager linearLayoutManager;

    RecyclerBaseAdapter recyclerBaseAdapter;

    List<VideoInfo> dataList;

    GSYVideoHelper smallVideoHelper;

    GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder;

    int lastVisibleItem;

    int firstVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置一个exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();

        listItemRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem   = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Debuger.printfLog("firstVisibleItem " + firstVisibleItem +" lastVisibleItem " + lastVisibleItem);
                //大于0说明有播放,//对应的播放列表TAG
                if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(RecyclerItemViewHolder.TAG)) {
                    //当前播放的位置
                    int position = smallVideoHelper.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果是小窗口就不需要处理
                        if (!smallVideoHelper.isSmall() && !smallVideoHelper.isFull()) {
                            //小窗口
                            int size = CommonUtil.dip2px(MainActivity.this, 150);
                            //actionbar为true才不会掉下面去
                            smallVideoHelper.showSmallVideo(new Point(size, size), true, true);
                        }
                    } else {
                        if (smallVideoHelper.isSmall()) {
                            smallVideoHelper.smallVideoToNormal();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (smallVideoHelper.backFromFull()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smallVideoHelper.releaseVideoPlayer();
        GSYVideoManager.releaseAllVideos();
    }

    private void initData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        apiInterface.getResponse().enqueue(new Callback<List<VideoInfo>>() {
            @Override
            public void onResponse(Call<List<VideoInfo>> call, Response<List<VideoInfo>> response) {
                if (response.body() != null)
                    dataList = response.body();
                initView();
            }

            @Override
            public void onFailure(Call<List<VideoInfo>> call, Throwable t) {
                Log.d("retrofit", t.getMessage());
            }
        });

    }

    private void initView() {
        linearLayoutManager = new LinearLayoutManager(this);
        listItemRecycler.setLayoutManager(linearLayoutManager);

        recyclerBaseAdapter = new RecyclerBaseAdapter(this, dataList);
        listItemRecycler.setAdapter(recyclerBaseAdapter);


        smallVideoHelper = new GSYVideoHelper(this, new NormalGSYVideoPlayer(this));
        smallVideoHelper.setFullViewContainer(videoFullContainer);

        //配置
        gsySmallVideoHelperBuilder = new GSYVideoHelper.GSYVideoHelperBuilder();
        gsySmallVideoHelperBuilder
                .setHideActionBar(true)
                .setHideStatusBar(true)
                .setNeedLockFull(true)
                .setCacheWithPlay(true)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(true)
                .setLockLand(true).setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                Debuger.printfLog("Duration " + smallVideoHelper.getGsyVideoPlayer().getDuration() + " CurrentPosition " + smallVideoHelper.getGsyVideoPlayer().getCurrentPositionWhenPlaying());
            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {
                super.onQuitSmallWidget(url, objects);
                //大于0说明有播放,//对应的播放列表TAG
                if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(RecyclerItemViewHolder.TAG)) {
                    //当前播放的位置
                    int position = smallVideoHelper.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //释放掉视频
                        smallVideoHelper.releaseVideoPlayer();
                        recyclerBaseAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

        smallVideoHelper.setGsyVideoOptionBuilder(gsySmallVideoHelperBuilder);

        recyclerBaseAdapter.setVideoHelper(smallVideoHelper, gsySmallVideoHelperBuilder);

    }
}
