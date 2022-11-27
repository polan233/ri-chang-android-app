package com.example.richang;

import android.graphics.drawable.Drawable;


public class DairyText {

    public static final int TYPE_ITEM=0;
    public static final int TYPE_BUTTON=1;
    public int type=TYPE_ITEM;
    public String content;
    public String date;
    public long createTime;
    public DairyText(String content,String date,long createTime){
        this.content=content;
        this.date=date;
        this.type=TYPE_ITEM;
        this.createTime=createTime;
    }
    public DairyText(int type){
        this.type=type;
    }
}
