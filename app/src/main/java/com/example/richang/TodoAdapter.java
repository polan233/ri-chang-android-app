package com.example.richang;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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

public class TodoAdapter extends ArrayAdapter<Todo> {
    private List<Todo> data=null;
    private SQLiteDatabase db;

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

                deleteTodo(todo);
                data.remove(position);
                notifyDataSetChanged();
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
