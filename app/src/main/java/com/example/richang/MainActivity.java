package com.example.richang;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends FragmentActivity {
    public static final int REQUEST_TAKE_PHOTO_FROM_EAT=1;
    public static final int REQUEST_TAKE_PHOTO_FROM_DAIRY=2;
    public static final int REQUEST_PICK_PHOTO_TO_DAIRY=3;


    private TextView nav_todo;
    private TextView nav_eat;
    private TextView nav_habit;
    private TextView nav_dairy;
    private TextView tv_date;
    //private Button btn_delete;

    private Calendar c;//获得日历实例
    private int m_year,m_month,m_day,m_weekday;
    public static String[] weekCharactor=new String[]{"一","二","三","四","五","六","日"};

    private TodoFragment todoFragment;
    private EatFragment eatFragment=null;
    private DairyFragment dairyFragment=null;

    private int currentId=R.id.tv_todo;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_TAKE_PHOTO_FROM_EAT:
                try{
                    Uri uri=eatFragment.createEatDialog.imageUri;
                    Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    BitmapDrawable bd=new BitmapDrawable(bitmap);
                    eatFragment.setImage(bd);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case REQUEST_TAKE_PHOTO_FROM_DAIRY:
                if(resultCode==RESULT_OK){
                    try{
                        Uri uri=dairyFragment.imageUri;
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        BitmapDrawable bd=new BitmapDrawable(bitmap);
                        dairyFragment.setImage(bd);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_PICK_PHOTO_TO_DAIRY:
                if(resultCode==RESULT_OK){
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tv_date=(TextView)findViewById(R.id.tv_todo_top_date) ;
        nav_todo=(TextView)findViewById(R.id.tv_todo);
        nav_todo.setSelected(true);
        nav_eat=(TextView) findViewById(R.id.tv_eat);
        nav_habit=(TextView) findViewById(R.id.tv_habit);
        nav_dairy=(TextView) findViewById(R.id.tv_dairy);


        c = Calendar.getInstance();
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);
        m_weekday=c.get(Calendar.DAY_OF_WEEK);
        tv_date.setText((m_month+1)+"/"+m_day+" 周"+weekCharactor[m_weekday-1]);

        //默认加载todo
        todoFragment=new TodoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,todoFragment).commit();

        nav_todo.setOnClickListener(tabClickListener);
        nav_eat.setOnClickListener(tabClickListener);
        nav_habit.setOnClickListener(tabClickListener);
        nav_dairy.setOnClickListener(tabClickListener);

    }

    private View.OnClickListener tabClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()!=currentId){
                changeSelect(v.getId());
                changeFragment(v.getId());
                currentId=v.getId();
            }
        }
    };

    private void changeFragment(int resId){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

        hideFragments(transaction);
        if(resId==R.id.tv_todo){
            if(todoFragment==null){
                todoFragment=new TodoFragment();
                transaction.add(R.id.container,todoFragment);
            }else{
                transaction.show(todoFragment);
            }
        }
        else if(resId==R.id.tv_eat){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
            }
            if(eatFragment==null){
                eatFragment=EatFragment.newInstance();
                transaction.add(R.id.container,eatFragment);
            }else{
                transaction.show(eatFragment);
            }
        }
        else if(resId==R.id.tv_dairy){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
            if(dairyFragment==null){
                dairyFragment=DairyFragment.newInstance();
                transaction.add(R.id.container,dairyFragment);
            }else{
                transaction.show(dairyFragment);
            }
        }
        //TODO 其他Fragment 的添加

        transaction.commit();
    }

    //隐藏所有,注意判断非空
    private void hideFragments(FragmentTransaction transaction){
        if(todoFragment!=null)
            transaction.hide(todoFragment);
        if(eatFragment!=null)
            transaction.hide(eatFragment);
        if(dairyFragment!=null)
            transaction.hide(dairyFragment);
        //TODO 其他Fragment
    }

    //改变导航栏被选中状态
    private void changeSelect(int resId){
        nav_todo.setSelected(false);
        nav_eat.setSelected(false);
        nav_habit.setSelected(false);
        nav_dairy.setSelected(false);

        switch (resId){
            case R.id.tv_todo:
                nav_todo.setSelected(true);
                break;
            case R.id.tv_eat:
                nav_eat.setSelected(true);
                break;
            case R.id.tv_habit:
                nav_habit.setSelected(true);
                break;
            case R.id.tv_dairy:
                nav_dairy.setSelected(true);
                break;
        }

    }
    public static String getCurDate(){
        Calendar c = Calendar.getInstance();
        int m_year = c.get(Calendar.YEAR);
        int m_month = c.get(Calendar.MONTH);
        int m_day = c.get(Calendar.DAY_OF_MONTH);
        int m_weekday=c.get(Calendar.DAY_OF_WEEK);
        return (m_year+"/"+(m_month+1)+"/"+m_day);
    }
    public static String getWeekDay(){
        Calendar c = Calendar.getInstance();
        int m_weekday=c.get(Calendar.DAY_OF_WEEK);
        return " 周"+weekCharactor[m_weekday-1];
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        handleImage(imagePath);
    }
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        handleImage(imagePath);
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void handleImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            BitmapDrawable bd=new BitmapDrawable(bitmap);
            dairyFragment.setImage(bd);
        } else {
            Toast.makeText(this, "获取相册图片失败", Toast.LENGTH_SHORT).show();
        }
    }

}