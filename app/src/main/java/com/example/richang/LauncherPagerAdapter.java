package com.example.richang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class LauncherPagerAdapter extends PagerAdapter {

    private List<View> views;

    private int[] images=new int[]{R.mipmap.tutorial_1,R.mipmap.tutorial_2,
            R.mipmap.tutorial_3,R.mipmap.tutorial_4,R.mipmap.tutorial_5};

    public LauncherPagerAdapter(Context context){
        views=new ArrayList<View>();

        for(int i=0;i<images.length;i++){
            View item= LayoutInflater.from(context).inflate(R.layout.activity_luancher_pager_item,null);
            ImageView imageview=(ImageView) item.findViewById(R.id.imageview);
            imageview.setImageResource(images[i]);
            views.add(item);
        }
    }

    @Override
    public int getCount() {
        return views==null? 0:views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(views.get(position),0);
        return views.get(position);
    }
}
