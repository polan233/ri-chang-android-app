package com.example.richang;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


public class Eat {
//    java.util.Date date = new java.util.Date();
//    long datetime = date.getTime();
    public Drawable pic;
    public String title;
    public String detail;
    public long createTime;
    public Eat(Drawable pic,String title, String detail,long createTime){
        this.pic=pic;
        this.title=title;
        this.detail=detail;
        this.createTime=createTime;
    }

}
