package com.example.myapplication.net;

import com.example.myapplication.bean.Stuff;
import com.example.myapplication.req.AnnounceReq;
import com.example.myapplication.req.StuffReq;
import com.example.myapplication.req.UserReq;
import com.example.myapplication.resp.AnnounceResp;
import com.example.myapplication.resp.OrderResp;
import com.example.myapplication.resp.StuffResp;
import com.example.myapplication.resp.UserResp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetRequestInterface {
    @POST("smartward/backend/doctor/search-by-id")
    Call<StuffResp> getStuffList(@Body StuffReq req);

    Call<AnnounceResp> getAnnouncement(@Body AnnounceReq req);

    Call<StuffResp> getStuffDetail(@Body StuffReq req);

    Call<UserResp> register(@Body UserReq req);

    Call<UserResp> login(@Body UserReq req);

    Call<List<Stuff>> getShoppingCar(@Body UserReq req);

    Call<OrderResp> getOrderList(@Body UserReq req);

    Call<UserResp> getUserInfo(@Body UserReq req);

    Call<UserResp> updateUserInfo(@Body UserReq req);

    Call<List<String>> getStuffCategory();

    Call<StuffResp> updateStuffCategory(@Body StuffReq req);

    Call<StuffResp> deleteStuffCategory(@Body StuffReq req);

    Call<StuffResp> insertStuffCategory(@Body StuffReq req);

    Call<AnnounceResp> insertAnnouncement(@Body AnnounceReq req);

    Call<AnnounceResp> deleteAnnouncement(@Body AnnounceReq req);

}
