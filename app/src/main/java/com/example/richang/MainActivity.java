package com.example.richang;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends FragmentActivity {
    public static final int REQUEST_TAKE_PHOTO=1;


    private TextView nav_todo;
    private TextView nav_eat;
    private TextView nav_habit;
    private TextView nav_dairy;
    private TextView tv_date;
    //private Button btn_delete;

    private Calendar c;//获得日历实例
    private int m_year,m_month,m_day,m_weekday;
    private String[] weekCharactor=new String[]{"一","二","三","四","五","六","日"};

    private TodoFragment todoFragment;
    private EatFragment eatFragment=null;
    private DairyFragment dairyFragment=null;

    private int currentId=R.id.tv_todo;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_TAKE_PHOTO:
                Bitmap bm=(Bitmap) data.getExtras().get("data");
                BitmapDrawable bd=new BitmapDrawable(bm);
                if(eatFragment!=null){
                    eatFragment.setImage(bd);
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

}