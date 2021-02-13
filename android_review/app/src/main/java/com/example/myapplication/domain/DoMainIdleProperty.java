package com.example.myapplication.domain;

/**
 * 闲置物实体类
 */
public class DoMainIdleProperty {
    private String id;  //闲置物id
    private String title;  //闲置物标题
    private String describe;  //闲置物商品描述
    private int imgId;  //图片id

    public DoMainIdleProperty(String title, int imgId) {
        this.title = title;
        this.imgId = imgId;
    }

    @Override
    public String toString() {
        return "DoMainIdleProperty{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", imgId=" + imgId +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public DoMainIdleProperty() {
    }

    public DoMainIdleProperty(String id, String title, String describe, int imgId) {
        this.id = id;
        this.title = title;
        this.describe = describe;
        this.imgId = imgId;
    }
}
