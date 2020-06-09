package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class VideoInfo {
    @SerializedName("_id")
    public String _id;

    @SerializedName("feedurl")
    public String feedurl;

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("description")
    public String description;

    @SerializedName("likecount")
    public int likecount;

    @SerializedName("avatar")
    public String avatar;
}
