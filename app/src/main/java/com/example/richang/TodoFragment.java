package com.example.richang;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

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

    public TodoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodoFragment newInstance(String param1, String param2) {
        TodoFragment fragment = new TodoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_todo, container, false);
        btn_add=(Button) view.findViewById(R.id.btn_add);
        rg_todo=(RadioGroup)view.findViewById(R.id.rg_todo);
        rg_todo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                todoSelect=i;
                refreshTodo();
            }
        });

        lv=(ListView)view.findViewById(R.id.lv_todo);
        sqLiteHelper=new SQliteHelper(view.getContext());
        db=sqLiteHelper.getReadableDatabase();

        c = Calendar.getInstance();
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);
        m_weekday=c.get(Calendar.DAY_OF_WEEK);
        curdate=m_year+"/"+(m_month+1)+"/"+m_day;
        weekdays=getWeekDays();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();//展示添加新todo的弹窗并完成添加操作
            }
        });

        adapter=new TodoAdapter(view.getContext(),R.layout.todo_item,data);
        adapter.setDb(db);
        //还需要把数据库的引用传给TodoAdapter在完成的时候删除数据
        lv.setAdapter(adapter);
        refreshTodo();
        return view;
    }

    public void showEditDialog(){
        createTodoDialog = new CreateTodoDialog((MainActivity)getActivity(),R.style.CustomDialog,
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