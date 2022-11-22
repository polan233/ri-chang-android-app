package com.example.richang;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQliteHelper extends SQLiteOpenHelper {
    private static final int VERSION =1;
    private static String db_name="ricahngapp";

    public SQliteHelper(Context context){
        super(context,db_name,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql="create table list(id INTEGER PRIMARY KEY AUTOINCREMENT,date varchar(50),name varchar(255))";
        db.execSQL(sql);
        //TODO 给eat界面也建表,然后在eatadapter里面写相应操作
        sql="create table eat_list(id INTEGER PRIMARY KEY AUTOINCREMENT,date varchar(50),writeTime INTEGER,title varchar(255),detail varchar(255),image blob)";
        db.execSQL(sql);

        sql="create table dairy_text(id INTEGER PRIMARY KEY AUTOINCREMENT,date varchar(50),writeTime INTEGER,content text)";
        db.execSQL(sql);

        sql="create table dairy_pic(id INTEGER PRIMARY KEY AUTOINCREMENT,date varchar(50),writeTime INTEGER,image blob)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        Log.i("SQLiteHelper","Update Database.....");
    }
}
