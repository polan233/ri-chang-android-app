package com.example.richang;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class CreateEatDialog extends Dialog {
    private Activity context;
    private View.OnClickListener okClickListener;
    private View.OnClickListener cancleClickListener;

    private Button btn_ok;
    private Button btn_cancel;
    private RelativeLayout root;
    public ImageView ib_image;
    public EditText et_detail;
    public EditText et_title;
    private Calendar c;//获得日历实例
    private int m_year,m_month,m_day;
    public static final int REQUEST_TAKE_PHOTO=1;
    public Drawable imageVar=null;

    public CreateEatDialog(Activity context){
        super(context);
        this.context=context;
    }
    public CreateEatDialog(Activity context, int theme, View.OnClickListener ok, View.OnClickListener cancel){
        super(context,theme);
        this.context=context;
        this.okClickListener=ok;
        this.cancleClickListener=cancel;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_eat_dialog);
        root=(RelativeLayout)findViewById(R.id.rl_eat_dialog_root);
        et_detail=(EditText)findViewById(R.id.et_create_eat_detail);
        et_title=(EditText)findViewById(R.id.et_create_eat_title);

        ib_image=(ImageView)findViewById(R.id.ib_create_eat);
        btn_ok=(Button) findViewById(R.id.btn_create_eat_add);
        btn_cancel=(Button) findViewById(R.id.btn_create_eat_cancel);

        ib_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                    //ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.CAMERA},1);
                    if(imageVar!=null) imageVar=null;
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    context.startActivityForResult(intent,REQUEST_TAKE_PHOTO);


                }

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


    public void changeImage(Drawable image) {
        imageVar=image;
        ib_image.setBackground(image);
        imageVar=null;
    }
}
