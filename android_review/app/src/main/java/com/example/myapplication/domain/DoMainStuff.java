package com.example.myapplication.domain;

import android.util.Log;


import com.google.gson.Gson;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DoMainStuff {
    private String goodsId;
    private String userId;
    private String categoryId;
    private String goodsTags;
    private String goodsName;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStatus;
    private Timestamp goodsCreateDate;
    private Integer messageNum;
    private String goodsProvince;
    private DoMainUser doMainUser;
    private String goodsCoverImgDir; //封面图片地址

    private String orderId;

    public DoMainStuff initTestData() {
        DoMainStuff goods = new DoMainStuff("userId" + Math.random(),
                "categoryId" + Math.random(), "goodsTags",
                "goodsName", "goodsDetail", 11.45d, 1,
                "江苏", "https://ae01.alicdn.com/kf/Uf8cd7a9ee0054a1c85f95633ccb722fc3.jpg");
        goods.goodsId = "goodsId" + Math.random();
        goods.goodsCreateDate = new Timestamp(System.currentTimeMillis());
        goods.messageNum = 22;
        goods.orderId = "orderId" + Math.random();
        goods.doMainUser = new DoMainUser("userId" + Math.random(), "userLoginId" + Math.random(),
                1, "username", "password", "userPhoneNum",
                "userEmail", new Timestamp(System.currentTimeMillis()), 1);
        return goods;
    }


    @Override
    public String toString() {
        return "IdleGoods{" +
                "goodsId='" + goodsId + '\'' +
                ", userId='" + userId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", goodsTags='" + goodsTags + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsDetail='" + goodsDetail + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsStatus=" + goodsStatus +
                ", goodsCreateDate=" + goodsCreateDate +
                ", messageNum=" + messageNum +
                ", goodsProvince='" + goodsProvince + '\'' +
                ", doMainUser=" + doMainUser +
                ", goodsCoverImgDir='" + goodsCoverImgDir + '\'' +
                ", orderId='" + orderId + '\'' +
                ", isCollected=" + isCollected +
                ", orderStatus=" + orderStatus +
                '}';
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private boolean isCollected; // 在用户登录情况下，当前记录是否被收藏

    private Integer orderStatus; // 在被用户拍下之后，订单处于的状态，在被拍下后才有值

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public DoMainUser getDoMainUser() {
        return doMainUser;
    }

    public void setDoMainUser(DoMainUser doMainUser) {
        this.doMainUser = doMainUser;
    }

    public String getGoodsCoverImgDir() {
        return goodsCoverImgDir;
    }

    public void setGoodsCoverImgDir(String goodsCoverImgDir) {
        this.goodsCoverImgDir = goodsCoverImgDir;
    }

    public String getGoodsProvince() {
        return goodsProvince;
    }

    public void setGoodsProvince(String goodsProvince) {
        this.goodsProvince = goodsProvince;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getGoodsTags() {
        return goodsTags;
    }

    public void setGoodsTags(String goodsTags) {
        this.goodsTags = goodsTags;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Timestamp getGoodsCreateDate() {
        return goodsCreateDate;
    }

    public void setGoodsCreateDate(Timestamp goodsCreateDate) {
        this.goodsCreateDate = goodsCreateDate;
    }

    public Integer getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(Integer messageNum) {
        this.messageNum = messageNum;
    }

    public DoMainStuff(String userId, String categoryId, String goodsTags, String goodsName,
                       String goodsDetail, Double goodsPrice, Integer goodsStatus,
                       String goodsProvince, String goodsCoverImgDir) {
        this.goodsId = createIdleGoodsId();
        this.userId = userId;
        this.categoryId = categoryId;
        this.goodsTags = goodsTags;
        this.goodsName = goodsName;
        this.goodsDetail = goodsDetail;
        this.goodsPrice = goodsPrice;
        this.goodsStatus = goodsStatus;
        this.goodsProvince = goodsProvince;
        this.goodsCoverImgDir = goodsCoverImgDir;
    }


    public DoMainStuff() {

    }

    private String createIdleGoodsId() {
        return "ig_" + (new Date()).getTime();
    }

    public static List<DoMainStuff> parseToList(String result) {
        if (result.equals("[]")) {
            return null;
        }
        List<DoMainStuff> res = new ArrayList<>();
        List<Map<Object, Object>> mpLists = new Gson().fromJson(result, List.class);
        for (Map<Object, Object> mpList : mpLists) {
            res.add(new DoMainStuff(mpList));
        }
        return res;
    }

    public DoMainStuff(Map<Object, Object> mp) {
        this.goodsId = (String) mp.get("goodsId");
        this.goodsProvince = (String) mp.get("goodsProvince");
        this.goodsStatus = (Integer) mp.get("goodsStatus");
        BigDecimal goodsPrice = (BigDecimal) mp.get("goodsPrice");
        this.goodsPrice = goodsPrice.doubleValue();
        this.isCollected = (Boolean) mp.get("collected");
        this.goodsCoverImgDir = (String) mp.get("goodsCoverImgDir");
        this.goodsName = (String) mp.get("goodsName");
        Map<Object, Object> user = (Map<Object, Object>) mp.get("doMainUser");
        Log.d("IdleGoodsInfoList", user.toString());

        this.doMainUser = new DoMainUser();
        this.doMainUser.setUserName((String) user.get("userName"));
    }
}
