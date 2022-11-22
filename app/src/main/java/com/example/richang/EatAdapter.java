package com.example.richang;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EatAdapter extends ArrayAdapter<Eat> {
    private List<Eat> data=null;
    private SQLiteDatabase db;

    public interface ShowPopPic{
        public void onShow(Drawable image);
    }
    public DairyPicAdapter.ShowPopPic showPopPic;
    public void setOnShow(DairyPicAdapter.ShowPopPic show){
        this.showPopPic=show;
    }

    public EatAdapter(@NonNull Context context, int resource, @NonNull List<Eat> objects){ //TODO EatAdapter这里的代码还没写
        super(context,resource,objects);
        data=objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertview,@NonNull ViewGroup parent){
        Eat eat=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(R.layout.eat_item,parent,false);
        ImageView iv=(ImageView)view.findViewById(R.id.iv_eat_item_image);
        TextView tv_title=(TextView)view.findViewById(R.id.tv_eat_item_title);
        TextView tv_detail=(TextView)view.findViewById(R.id.tv_eat_item_detail);
        ImageButton btn_delete=(ImageButton)view.findViewById(R.id.btn_eat_item_delete);
        LinearLayout ll=(LinearLayout)view.findViewById(R.id.ll_eatitem_root);

        Drawable temp=eat.pic;
        if(temp!=null){
            iv.setBackground(eat.pic);
        }
        tv_title.setText(eat.title);
        tv_detail.setText(eat.detail);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator animation1=ObjectAnimator.ofFloat(ll,"scaleX",0f);
                ObjectAnimator animation2=ObjectAnimator.ofFloat(ll,"translationX",-1000);
                animation1.setDuration(600);
                animation2.setDuration(600);
                animation1.start();
                animation2.start();
                //TODO 这里就可以加一些动画效果啦
                Handler handler = new Handler(); // 如果这个handler是在UI线程中创建的
                handler.postDelayed(new Runnable() {  // 开启的runnable也会在这个handler所依附线程中运行，即主线程
                    @Override
                    public void run() {
                        // 可更新UI或做其他事情
                        // 注意这里还在当前线程，没有开启新的线程
                        // new Runnable(){}，只是把Runnable对象以Message形式post到UI线程里的Looper中执行，并没有新开线程。
                        //TODO 想一个合适的主键,还是需要实时更新数据库的
                        deleteEat(eat);
                        data.remove(position);
                        notifyDataSetChanged();
                    }
                }, 600); // 延时0.6s执行run内代码

            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopPic.onShow(iv.getBackground());
            }
        });
        return view;
    }
    public void setDb(SQLiteDatabase db){
        this.db=db;
    }
    public  void deleteEat(Eat eat){
        if(db!=null) {
            String str = "delete from eat_list where writeTime=?";
            db.execSQL(str, new String[]{String.valueOf(eat.createTime)});
        }
    }

}
