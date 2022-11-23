package com.example.richang;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
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

    //侧面菜单相关
    private DrawerLayout drawerLayout;
    private RelativeLayout rlLook,rlAbout;
    private int currentSelectedLeftItem=-1;
    private RelativeLayout leftLookContent;
    private LinearLayout leftAboutContent;
    private ImageView lookStatus;
    private ImageView aboutStatus;
    private Bitmap leftSelectedBM = null;
    private Bitmap leftUnSelectedBM = null;

    public int[] themeColors={0xFF6200EE};
    public int[] themeColorsLights={0xFFBB86FC};
    public int curThemeColor=0;
    private Button btnThemeColor1;
    private Button btnThemeColor2;
    private Button btnThemeColor3;
    private Button btnThemeColor4;
//    private



    private class HandlePicIOTaskForEat extends AsyncTask<String,BitmapDrawable,Long>{
        @Override
        protected Long doInBackground(String... paths) {
            try{
                Uri uri=eatFragment.createEatDialog.imageUri;
                String path=UriUtils.getFileAbsolutePath(MainActivity.this,uri);
                Bitmap bitmap= BitmapHelper.getBitmapFromUri(MainActivity.this,uri);
                bitmap=BitmapHelper.rotateBitmap(bitmap,BitmapHelper.getBitmapDegree(path));
                BitmapDrawable bd=new BitmapDrawable(bitmap);
                publishProgress(bd);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(BitmapDrawable... bd) {
            eatFragment.setImage(bd[0]);
        }
    }
    private class HandlePicIOTaskForDairy extends AsyncTask<Uri,BitmapDrawable,Long>{
        @Override
        protected Long doInBackground(Uri... uris) {
            try {
                Uri uri = dairyFragment.imageUri;
                String path=UriUtils.getFileAbsolutePath(MainActivity.this,uri);
                Bitmap bitmap= BitmapHelper.getBitmapFromUri(MainActivity.this,uri);
                bitmap=BitmapHelper.rotateBitmap(bitmap,BitmapHelper.getBitmapDegree(path));
                BitmapDrawable bd = new BitmapDrawable(bitmap);
                publishProgress(bd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(BitmapDrawable... bd) {
            dairyFragment.setImage(bd[0]);
        }
    }
    @TargetApi(19)
    private  void handleImageOnKitKat(Intent data) {
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
        if (imagePath != null) {
           new HandlePickPicIOTaskForDairy().execute(imagePath);
        } else {
            Toast.makeText(this, "获取相册图片失败", Toast.LENGTH_SHORT).show();
        }
    }
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        if (imagePath != null) {
            new HandlePickPicIOTaskForDairy().execute(imagePath);
        } else {
            Toast.makeText(this, "获取相册图片失败", Toast.LENGTH_SHORT).show();
        }
    }
    private class HandlePickPicIOTaskForDairy extends AsyncTask<String, Drawable,Long>{

        @Override
        protected Long doInBackground(String... objects) {
            String imagePath=objects[0];
            if (imagePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                BitmapDrawable bd=new BitmapDrawable(bitmap);
                publishProgress(bd);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Drawable... values) {
            dairyFragment.setImage(values[0]);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_TAKE_PHOTO_FROM_EAT:
                if(resultCode==RESULT_OK)
                    new HandlePicIOTaskForEat().execute();
                break;
            case REQUEST_TAKE_PHOTO_FROM_DAIRY:
                if(resultCode==RESULT_OK){
                    new HandlePicIOTaskForDairy().execute();
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

    private void unSelectAllLeftItem(){
        rlLook.setSelected(false);
        rlAbout.setSelected(false);

        leftLookContent.setVisibility(View.GONE);
        leftAboutContent.setVisibility(View.GONE);

        lookStatus.setImageBitmap(leftUnSelectedBM);
        aboutStatus.setImageBitmap(leftUnSelectedBM);
    }
    private View.OnClickListener onLeftMenuClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(currentSelectedLeftItem!=v.getId()){
                currentSelectedLeftItem=v.getId();
                unSelectAllLeftItem(); //TODO 取消选中,隐藏内容菜单,设置箭头图片
                switch(v.getId()){
                    case R.id.rl_look:
                        rlLook.setSelected(true);
                        leftLookContent.setVisibility(View.VISIBLE);
                        lookStatus.setImageBitmap(leftSelectedBM);
                        break;
                    case R.id.rl_about:
                        rlAbout.setSelected(true);
                        leftAboutContent.setVisibility(View.VISIBLE);
                        aboutStatus.setImageBitmap(leftSelectedBM);
                        break;
                }
            }else{
                currentSelectedLeftItem=-1;
                unSelectAllLeftItem();
            }
        }
    };
    private View.OnClickListener onThemeColorListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.left_menu_btn_theme_color1:

                    break;
                case R.id.left_menu_btn_theme_color2:

                    break;
                case R.id.left_menu_btn_theme_color3:

                    break;
                case R.id.left_menu_btn_theme_color4:

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leftSelectedBM = BitmapFactory.decodeResource(getResources(), R.mipmap.left_menu_item_selected);
        leftUnSelectedBM = BitmapFactory.decodeResource(getResources(), R.mipmap.left_menu_item_unselected);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
        ||ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ||ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }


        drawerLayout=(DrawerLayout)findViewById(R.id.dd_main_root) ;
        rlLook=(RelativeLayout)findViewById(R.id.rl_look);
        rlAbout=(RelativeLayout)findViewById(R.id.rl_about);
        rlLook.setOnClickListener(onLeftMenuClickListener);
        rlAbout.setOnClickListener(onLeftMenuClickListener);
        leftLookContent=(RelativeLayout)findViewById(R.id.left_menu_look_content);
        leftAboutContent=(LinearLayout)findViewById(R.id.left_menu_about_content);
        lookStatus=(ImageView)findViewById(R.id.iv_look_status);
        aboutStatus=(ImageView)findViewById(R.id.iv_about_status);

//        btnThemeColor1.setOnClickListener(onThemeColorListener);
//        btnThemeColor2.setOnClickListener(onThemeColorListener);
//        btnThemeColor3.setOnClickListener(onThemeColorListener);
//        btnThemeColor4.setOnClickListener(onThemeColorListener);

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
//            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
//            }
            if(eatFragment==null){
                eatFragment=EatFragment.newInstance();
                transaction.add(R.id.container,eatFragment);
            }else{
                transaction.show(eatFragment);
            }
        }
        else if(resId==R.id.tv_dairy){
//            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
//            }
//            if (ContextCompat.checkSelfPermission(MainActivity.this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
//            }
//            if (ContextCompat.checkSelfPermission(MainActivity.this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
//            }
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
//        nav_todo.setBackgroundColor(themeColors[curThemeColor]);
//        nav_eat.setBackgroundColor(themeColors[curThemeColor]);
//        nav_habit.setBackgroundColor(themeColors[curThemeColor]);
//        nav_dairy.setBackgroundColor(themeColors[curThemeColor]);

        switch (resId){
            case R.id.tv_todo:
                nav_todo.setSelected(true);
               // nav_todo.setBackgroundColor(0xFFCC00);
                break;
            case R.id.tv_eat:
                nav_eat.setSelected(true);
               // nav_eat.setBackgroundColor(0x7ded9f);
                break;
            case R.id.tv_habit:
                nav_habit.setSelected(true);
               // nav_habit.setBackgroundColor(0x33CCFF);
                break;
            case R.id.tv_dairy:
                nav_dairy.setSelected(true);
               // nav_dairy.setBackgroundColor(0xfd4b4b);
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


}