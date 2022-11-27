package com.example.richang;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class CreateDairyTextDialog extends Dialog {
    private Activity context;
    private View.OnClickListener okClickListener;
    private View.OnClickListener cancleClickListener;

    private Button btn_ok;
    private Button btn_cancel;
    private RelativeLayout root;
    public TextView tv_date;
    public TextView tv_weekday;
    public EditText et_content;
    private Calendar c;//获得日历实例
    private int m_year,m_month,m_day,m_weekday;
    private String[] weekCharactor = new String[]{"日","一", "二", "三", "四", "五", "六"};
    private String[] weekdays;

    public Drawable image=null;

    public CreateDairyTextDialog(Activity context){
        super(context);
        this.context=context;
    }
    public CreateDairyTextDialog(Activity context, int theme, View.OnClickListener ok, View.OnClickListener cancel){
        super(context,theme);
        this.context=context;
        this.okClickListener=ok;
        this.cancleClickListener=cancel;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_dairy_text_dialog);
        root=(RelativeLayout)findViewById(R.id.rl_dairy_text_dialog_root);
        tv_date=(TextView)findViewById(R.id.tv_dairy_date);
        tv_weekday=(TextView)findViewById(R.id.tv_dairy_weekday);
        et_content=(EditText)findViewById(R.id.et_dairy_text_context);
        btn_ok=(Button) findViewById(R.id.btn_create_dairy_text_add);
        btn_cancel=(Button) findViewById(R.id.btn_create_dairy_text_cancel);

        c = Calendar.getInstance();
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);
        m_weekday = c.get(Calendar.DAY_OF_WEEK);
        weekdays = DairyFragment.getWeekDays();
        tv_date.setText(MainActivity.getCurDate());
        tv_weekday.setText(MainActivity.getWeekDay());
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
    public void changeImage(Drawable image){
        this.image=image;
    }


}
