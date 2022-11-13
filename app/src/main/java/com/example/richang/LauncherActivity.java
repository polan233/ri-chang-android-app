package com.example.richang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class LauncherActivity extends FragmentActivity {
    private ViewPager viewPager;
    private LauncherPagerAdapter adapter; //TODO 定义这个类: LauncherPagerAdapter

    private ImageView[] tips;
    private TextView tvStartRiChang;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luancher);

        if(!isFirst()){
            gotoMain();
        }

        tvStartRiChang=(TextView) findViewById(R.id.tv_start_richang);
        tvStartRiChang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMain();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.vp_launcher);
        viewPager.setOffscreenPageLimit(2); //设置缓存页数
        viewPager.setAdapter(adapter=new LauncherPagerAdapter(this));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int index){
                setImageBackground(index);

                if(index==tips.length-1){
                    tvStartRiChang.setVisibility(View.VISIBLE);
                }
                else{
                    tvStartRiChang.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        //初始化底部翻页显示的小横线那堆的控件
        ViewGroup group=(ViewGroup) findViewById(R.id.ll_viewGroup);
        tips=new ImageView[5];
        for(int i=0;i< tips.length;i++){
            ImageView imageView=new ImageView(this);
            if(i==0){
                imageView.setBackgroundResource(R.mipmap.page_indicator_focused);
            }else{
                imageView.setBackgroundResource(R.mipmap.page_indicator_unfocused);
            }
            tips[i]=imageView;
            LinearLayout.LayoutParams layoutParams = new
                    LinearLayout.LayoutParams(new
                    ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin=10;
            layoutParams.rightMargin=10;
            group.addView(imageView,layoutParams);
        }
    }

    private void setImageBackground(int selectItems){
        for(int i=0;i<tips.length;i++){
            if(i==selectItems){
                tips[i].setBackgroundResource(R.mipmap.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
            }
        }
    }

    public void gotoMain(){
        setNotFirst();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isFirst(){
        SharedPreferences setting = getSharedPreferences("richang", Context.MODE_PRIVATE);
        return setting.getBoolean("FIRST",true);
    }

    public void setNotFirst(){
        SharedPreferences setting = getSharedPreferences("richang",Context.MODE_PRIVATE);
        setting.edit().putBoolean("FIRST",false).commit();
    }
}
