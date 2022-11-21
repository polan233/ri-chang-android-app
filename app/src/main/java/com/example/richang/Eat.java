package com.example.richang;

import android.graphics.Bitmap;


public class Eat {
//    java.util.Date date = new java.util.Date();
//    long datetime = date.getTime();
//    TODO 可以根据这个唯一区别项
    public Bitmap imageBM;
    public String title;
    public String detail;
    public Eat(Bitmap imageBM,String title, String detail){
        this.imageBM=imageBM;
        this.title=title;
        this.detail=detail;
    }

}
