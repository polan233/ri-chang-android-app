package com.example.richang;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.TimerTask;

public class TodoAdapter extends ArrayAdapter<Todo> {
    private List<Todo> data=null;
    private SQLiteDatabase db;
    private PopPicDialog popPicDialog=null;

    public TodoAdapter(@NonNull Context context, int resource, @NonNull List<Todo> objects){
        super(context,resource,objects);
        data=objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertview,@NonNull ViewGroup parent){
        Todo todo=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(R.layout.todo_item,parent,false);
        TextView tv_date=view.findViewById(R.id.tv_todo_date);
        TextView tv_name=view.findViewById(R.id.tv_todo_name);
        LinearLayout ll=view.findViewById(R.id.ll_todoitem_root);

        Button btn=view.findViewById(R.id.btn_todo_done);
        tv_date.setText(todo.date);
        tv_name.setText(todo.name);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(ll !=null){
//                    ObjectAnimator animation=ObjectAnimator.ofFloat(ll,"translationX",1000f);
//                    animation.setDuration(100000);
//                    animation.start();
//                    while(animation.isRunning()){};
//                }
                btn.setText("☑");
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
                        deleteTodo(todo);
                        data.remove(position);
                        notifyDataSetChanged();
                    }
                }, 600); // 延时0.6s执行run内代码

            }
        });
        return view;
    }
    public void setDb(SQLiteDatabase db){
        this.db=db;
    }
    public  void deleteTodo(Todo todo){
        if(db!=null) {
            String str = "delete from list where date=? and name=?";
            db.execSQL(str, new String[]{todo.date, todo.name});
        }
    }
}
