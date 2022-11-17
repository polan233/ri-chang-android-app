package com.example.richang;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends FragmentActivity {
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

    private int currentId=R.id.tv_todo;

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
//        else if(resId==R.id.tv_eat){ //TODO 其他Fragment 的添加
//            if(eatFragment==null){
//                eatFragment=new EatFragment();
//                transaction.add(R.id.container,eatFragment);
//            }else{
//                transaction.show(eatFragment);
//            }
//        }

        transaction.commit();
    }

    //隐藏所有,注意判断非空
    private void hideFragments(FragmentTransaction transaction){
        if(todoFragment!=null)
            transaction.hide(todoFragment);
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