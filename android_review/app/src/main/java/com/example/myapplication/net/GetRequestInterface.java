package com.example.myapplication.net;

import com.example.myapplication.req.UserReq;
import com.example.myapplication.resp.UserResp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetRequestInterface {
    @POST("smartward/backend/doctor/search-by-id")
    Call<UserResp> getUserInfo(@Body UserReq req);

}
