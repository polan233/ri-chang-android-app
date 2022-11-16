package com.example.richang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodoActivity extends AppCompatActivity {
    private TextView nav_eat;
    private TextView nav_habit;
    private TextView nav_dairy;
    private TextView tv_date;
    //private Button btn_delete;
    private Button btn_add;
    private RadioGroup rg_todo;
    private int todoSelect=R.id.rb_quanbu;
    private CreateTodoDialog createTodoDialog;

    private Calendar c;//获得日历实例
    private int m_year,m_month,m_day,m_weekday;
    private String curdate;
    private String[] weekdays;
    private String[] weekCharactor=new String[]{"一","二","三","四","五","六","日"};

    private ListView lv;
    private TodoAdapter adapter;
    private List<Todo> data=new ArrayList<>();
    private SQliteHelper sqLiteHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        tv_date=(TextView)findViewById(R.id.tv_todo_top_date) ;
        nav_eat=(TextView) findViewById(R.id.tv_eat);
        nav_habit=(TextView) findViewById(R.id.tv_habit);
        nav_dairy=(TextView) findViewById(R.id.tv_dairy);
        //btn_delete=(Button) findViewById(R.id.btn_delete);
        btn_add=(Button) findViewById(R.id.btn_add);

        rg_todo=(RadioGroup)findViewById(R.id.rg_todo);
        rg_todo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                todoSelect=i;
                refreshTodo();
            }
        });

        lv=(ListView)findViewById(R.id.lv_todo);
        sqLiteHelper=new SQliteHelper(TodoActivity.this);
        db=sqLiteHelper.getReadableDatabase();

        c = Calendar.getInstance();
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);
        curdate=m_year+"/"+(m_month+1)+"/"+m_day;
        m_weekday=c.get(Calendar.DAY_OF_WEEK);
        tv_date.setText((m_month+1)+"/"+m_day+" 周"+weekCharactor[m_weekday-1]);
        weekdays=getWeekDays();


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();//展示添加新todo的弹窗并完成添加操作
            }
        });



        adapter=new TodoAdapter(TodoActivity.this,R.layout.todo_item,data);
        adapter.setDb(db);
        //还需要把数据库的引用传给TodoAdapter在完成的时候删除数据
        lv.setAdapter(adapter);
        refreshTodo();
    }


    public void showEditDialog(){
        createTodoDialog = new CreateTodoDialog(this,R.style.CustomDialog,
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = createTodoDialog.et_name.getText().toString().trim();
                String date = createTodoDialog.tv_date.getText().toString().trim();


                Log.i("todotest", name + " " + date);

                Todo item=new Todo(date,name);
                addTodo(item);
                refreshTodo();

                createTodoDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTodoDialog.dismiss();
            }
        });
        createTodoDialog.show();
    }
    public void refreshTodo(){
        data.clear();
        switch (todoSelect){
            case R.id.rb_benri:
                readDayTodo();
                break;
            case R.id.rb_benzhou:
                readWeekTodo();
                break;
            case R.id.rb_benyue:
                readMonthTodo();
                break;
            case R.id.rb_quanbu:
                readAllTodo();
                break;
        }
        adapter.notifyDataSetChanged();
    }
    public void readAllTodo(){
        Cursor cursor=db.query("list",new String[]{"name","date"},null,null,null,null,"id ASC",null);
        while(cursor.moveToNext()){
            String name=cursor.getString(0);
            String date=cursor.getString(1);
            data.add(new Todo(date,name));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
    public void readDayTodo(){
        Cursor cursor=db.query("list",new String[]{"name","date"},
                "date=?" ,
                new String[]{curdate},null,null,"id ASC",null);
        while(cursor.moveToNext()){
            String name=cursor.getString(0);
            String date=cursor.getString(1);
            data.add(new Todo(date,name));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
    public void readWeekTodo(){
        Cursor cursor=db.query("list",new String[]{"name","date"},
                "date = ? OR "+"date = ? OR "+"date = ? OR "+"date = ? OR "+"date = ? OR "+"date = ? OR "+"date = ?",
                weekdays,null,null,"id ASC",null);
        while(cursor.moveToNext()){
            String name=cursor.getString(0);
            String date=cursor.getString(1);
            data.add(new Todo(date,name));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
    public void readMonthTodo(){
        Cursor cursor=db.query("list",new String[]{"name","date"},
                "date like ?" ,
                new String[]{m_year+"/"+(m_month+1)+"/"+"%"},null,null,"id ASC",null);
        while(cursor.moveToNext()){
            String name=cursor.getString(0);
            String date=cursor.getString(1);
            data.add(new Todo(date,name));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
    public void addTodo(Todo todo){
        String str="insert into list values(null,?,?)";
        db.execSQL(str,new String[]{todo.date,todo.name});
    }
    private String[] getWeekDays() {
        Calendar c = Calendar.getInstance();
        // 获取本周的第一天
        int firstDayOfWeek = c.getFirstDayOfWeek();
        String[] list = new String[7];
        for (int i = 0; i < 7; i++) {
            c.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + i);
            int m_year = c.get(Calendar.YEAR);
            int m_month = c.get(Calendar.MONTH);
            int m_day = c.get(Calendar.DAY_OF_MONTH);
            String date = m_year + "/" + (m_month + 1) + "/" + m_day;
            list[i]=date;
        }
        return list;
    }
}