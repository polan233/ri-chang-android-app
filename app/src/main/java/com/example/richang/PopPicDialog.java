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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

public class PopPicDialog extends Dialog {
    private Activity context;
    private View.OnClickListener cancleClickListener;

    private RelativeLayout root;

    public ImageView iv;
    public Drawable image=null;

    public PopPicDialog(Activity context){
        super(context);
        this.context=context;
    }
    public PopPicDialog(Activity context, int theme, View.OnClickListener cancel,Drawable image){
        super(context,theme);
        this.context=context;
        this.cancleClickListener=cancel;
        this.image=image;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.pop_pic_dialog);
        root=(RelativeLayout)findViewById(R.id.pop_pic_dialog_root);
        iv=(ImageView)findViewById(R.id.iv_pop_pic);
        root.setOnClickListener(cancleClickListener);
        iv.setImageDrawable(image);


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
