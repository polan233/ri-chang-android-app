package com.example.richang;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

public class CreateTodoDialog extends AlertDialog {
    private Activity context;
    private View.OnClickListener okClickListener;
    private View.OnClickListener cancleClickListener;
    private Button btn_ok;
    private Button btn_cancel;
    private RelativeLayout root;
    public EditText et_name;
    public TextView tv_date;
    private Calendar c;//获得日历实例
    private int m_year,m_month,m_day;

    public CreateTodoDialog(Activity context){
        super(context);
        this.context=context;
    }
    public CreateTodoDialog(Activity context,int theme, View.OnClickListener ok, View.OnClickListener cancel){
        super(context,theme);
        this.context=context;
        this.okClickListener=ok;
        this.cancleClickListener=cancel;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_todo_dialog);
        root=(RelativeLayout)findViewById(R.id.rl_todo_dialog_root);
        et_name=(EditText) findViewById(R.id.et_todo_name);
        tv_date=(TextView) findViewById(R.id.tv_todo_date);
        btn_ok=(Button) findViewById(R.id.btn_todo_add);
        btn_cancel=(Button) findViewById(R.id.btn_todo_cancel);

        c = Calendar.getInstance();
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);
        tv_date.setText("点击设置待办日期");//设置TextView里的内容为日期
        tv_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateDialog(context,0).show();
            }
        });
        btn_ok.setOnClickListener(okClickListener);
        btn_cancel.setOnClickListener(cancleClickListener);
        root.setOnClickListener(cancleClickListener);




        Window dialogWindow=this.getWindow();
        WindowManager m=context.getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.height=((int)(d.getHeight()*1));
        p.width=((int)(d.getWidth()*1));
        dialogWindow.setAttributes(p);
        dialogWindow.setDimAmount(0.1f);
        this.setCancelable(true);
    }
    //调用Activity的showDialog( )方法显示进对话框
    protected Dialog CreateDialog(Context context, int id) {
        return new DatePickerDialog(context,l1,m_year, m_month, m_day);
    }
    //设置日期监听器
    private DatePickerDialog.OnDateSetListener l1 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            m_year = year;
            m_month = monthOfYear;
            m_day = dayOfMonth;
            tv_date.setText(m_year+"年"+(m_month+1)+"月"+m_day+"日");//为TextView设置文本内容，重新显示日期
        }
    };

}
