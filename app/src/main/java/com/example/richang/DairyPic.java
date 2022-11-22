package com.example.richang;


import android.graphics.drawable.Drawable;

public class DairyPic {
//    java.util.Date date = new java.util.Date();
//    long datetime = date.getTime();
    public static final int TYPE_ITEM=0;
    public static final int TYPE_BUTTON=1;
    public int type=TYPE_ITEM;
    public Drawable image;
    public String date;
    public long createTime;
    public DairyPic(Drawable image,String date, long createTime){
        this.image=image;
        this.date=date;
        this.type=TYPE_ITEM;
        this.createTime=createTime;
    }
    public DairyPic(int type){
        this.type=type;
    }
}
