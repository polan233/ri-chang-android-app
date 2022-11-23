package com.example.richang;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

public class HelpDialog extends Dialog {
    private Activity context;

    private View.OnClickListener cancleClickListener;

    private Button btn_next;
    private Button btn_cancel;
    private RelativeLayout root;
    public TextView tv_help;

    static private String helpMessage[]={
            "待办事项界面顶部的按钮可以选择当前显示待办的日期范围",
            "要完成待办,只需要点击左边的方框",
            "日记界面中长按可以删除相应图片和文字",
            "饮食界面可以记录你的每一顿饭,不觉得这很酷吗,完全符合一个理科生对生活的想象",
            "饮食界面顶部的按钮可以快速跳转查看指定日期,只需要点击按钮中间日期选择,然后点击跳转按钮",
            "要添加日记的图片或者文字只需要点击+号按钮",
            "这个app是我的期末大作业,所以有很多不足,在此表示我的歉意",
            "日记界面中长按可以删除相应图片和文字",
            "我的审美确实比较不理想",
            "实际上我还没想到怎么实现切换主题色,抱歉"
    };
    static private int helpIndex=0;

    public Drawable image=null;

    public HelpDialog(Activity context){
        super(context);
        this.context=context;
    }
    public HelpDialog(Activity context, int theme, View.OnClickListener cancleClickListener){
        super(context,theme);
        this.context=context;
        this.cancleClickListener=cancleClickListener;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.help_dialog);
        root=(RelativeLayout)findViewById(R.id.rl_help_dialog_root);
        btn_next=(Button)findViewById(R.id.btn_help_next) ;
        btn_cancel=(Button) findViewById(R.id.btn_help_cancel);
        tv_help=(TextView)findViewById(R.id.tv_help_content) ;
        helpIndex=0;
        tv_help.setText(helpMessage[helpIndex]);


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpIndex=(helpIndex+1)%helpMessage.length;
                tv_help.setText(helpMessage[helpIndex]);
            }
        });
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


}
