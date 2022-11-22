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

public class CreateDairyPicDialog extends Dialog {
    private Activity context;
    private View.OnClickListener fileClickListener;
    private View.OnClickListener camClickListener;
    private View.OnClickListener okClickListener;
    private View.OnClickListener cancleClickListener;

    private Button btn_file;
    private Button btn_cam;
    private Button btn_ok;
    private Button btn_cancel;
    public ImageView iv;

    private RelativeLayout root;
    public Drawable image=null;


    public CreateDairyPicDialog(Activity context){
        super(context);
        this.context=context;
    }
    public CreateDairyPicDialog(Activity context, int theme, View.OnClickListener file,View.OnClickListener cam,View.OnClickListener ok, View.OnClickListener cancel){
        super(context,theme);
        this.context=context;
        this.fileClickListener=file;
        this.camClickListener=cam;
        this.okClickListener=ok;
        this.cancleClickListener=cancel;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_dairy_pic_dialog);
        this.image=null;
        root=(RelativeLayout)findViewById(R.id.rl_dairy_pic_dialog_root);

        btn_file=(Button) findViewById(R.id.btn_dairy_pic_from_file);
        btn_cam=(Button) findViewById(R.id.btn_dairy_pic_from_camera);
        btn_cancel=(Button) findViewById(R.id.btn_create_dairy_pic_cancel);
        btn_ok=(Button)findViewById(R.id.btn_create_dairy_pic_ok);
        iv=(ImageView)findViewById(R.id.iv_dairy_create_pic);

        btn_file.setOnClickListener(fileClickListener);
        btn_cam.setOnClickListener(camClickListener);
        btn_cancel.setOnClickListener(cancleClickListener);
        btn_ok.setOnClickListener(okClickListener);
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
        iv.setBackground(image);
    }

}
