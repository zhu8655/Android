package com.example.myapplication;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.Call;

public interface ApiInterface {
    @GET("api/invoke/video/invoke/video")
    Call<List<VideoInfo>> getResponse();
}
