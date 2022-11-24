package com.example.richang;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.time.Month;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class SQliteHelper extends SQLiteOpenHelper {
    private static final int VERSION =1;
    private static String db_name="ricahngapp";

//    //!!!! 测试数据相关，实际运行时请注释！
//    public static String characters="ab cdefgh ijklm nopqr stuvw xyz ABCD EFG HIJ KLM NO PQR ST U VWX  YZ";
//    public static List<Byte[]> eatTestPics;
//    public static List<Byte[]> dairyTestPics;
//    //!!!!!

    public SQliteHelper(Context context){
        super(context,db_name,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql="create table list(id INTEGER PRIMARY KEY AUTOINCREMENT,date varchar(50),name varchar(255))";
        db.execSQL(sql);

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
//    //TODO 写一个插入测试数据的方法
//
//    public static void insertTestData(SQLiteDatabase db){
//
//    }
//    public static void insertTodoTestData(SQLiteDatabase db){
//        Calendar c=Calendar.getInstance();
//        c.add(Calendar.MONTH,-1);
//        String date;
//        for(int i=0;i<50;i++){
//            c.add(Calendar.DAY_OF_MONTH,1);
//            int m_year = c.get(Calendar.YEAR);
//            int m_month = c.get(Calendar.MONTH);
//            int m_day = c.get(Calendar.DAY_OF_MONTH);
//            date=m_year+"/"+(m_month+1)+"/"+m_day;
//
//            String name="test: "+generateString(new Random(),characters,4);
//
//            String sql="insert into list values(null,?,?)";
//            db.execSQL(sql,new String[]{date,name});
//        }
//        c=Calendar.getInstance();
//        for(int i=0;i<5;i++){
//            int m_year = c.get(Calendar.YEAR);
//            int m_month = c.get(Calendar.MONTH);
//            int m_day = c.get(Calendar.DAY_OF_MONTH);
//            date=m_year+"/"+(m_month+1)+"/"+m_day;
//
//            String name="test: "+generateString(new Random(),characters,4);
//
//            String sql="insert into list values(null,?,?)";
//            db.execSQL(sql,new String[]{date,name});
//        }
//    }
//    public static void insertEatTestData(SQLiteDatabase db){
//        Calendar c=Calendar.getInstance();
//        c.add(Calendar.DAY_OF_MONTH,-11);
//        String date;
//        for(int i=0;i<10;i++){
//            c.add(Calendar.DAY_OF_MONTH,1);
//            for(int j=0;j<3;j++){
//                int m_year = c.get(Calendar.YEAR);
//                int m_month = c.get(Calendar.MONTH);
//                int m_day = c.get(Calendar.DAY_OF_MONTH);
//                date=m_year+"/"+(m_month+1)+"/"+m_day;
//                String title = ;
//                String detail = ;
//                Byte[] pic = eatTestPics[j] ;
//                java.util.Date time = new java.util.Date();
//                long datetime = time.getTime();
//                String name="test: "+generateString(new Random(),characters,4);
//
//                String sql="insert into list values(null,?,?)";
//                db.execSQL(sql,new String[]{date,name});
//            }
//        }
//        c=Calendar.getInstance();
//        for(int i=0;i<5;i++){
//            int m_year = c.get(Calendar.YEAR);
//            int m_month = c.get(Calendar.MONTH);
//            int m_day = c.get(Calendar.DAY_OF_MONTH);
//            date=m_year+"/"+(m_month+1)+"/"+m_day;
//
//            String name="test: "+generateString(new Random(),characters,4);
//
//            String sql="insert into list values(null,?,?)";
//            db.execSQL(sql,new String[]{date,name});
//        }
//    }
//
//    public static String generateString(Random rng, String characters, int length)
//    {
//        char[] text = new char[length];
//        for (int i = 0; i < length; i++)
//        {
//            text[i] = characters.charAt(rng.nextInt(characters.length()));
//        }
//        return new String(text);
//    }
}
