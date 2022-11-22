package com.example.richang;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.content.FileProvider;

import java.io.File;
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
    public Drawable imageVar=null;
    public Uri imageUri;

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

                    // 创建File对象，用于存储拍照后的图片
                    //存放在手机SD卡的应用关联缓存目录下
                    File outputImage = new File(context.getExternalCacheDir(), "output_image.jpg");
               /* 从Android 6.0系统开始，读写SD卡被列为了危险权限，如果将图片存放在SD卡的任何其他目录，
                  都要进行运行时权限处理才行，而使用应用关联 目录则可以跳过这一步
                */
                    try {
                        if (outputImage.exists()) {
                            outputImage.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                /*
                   7.0系统开始，直接使用本地真实路径的Uri被认为是不安全的，会抛 出一个FileUriExposedException异常。
                   而FileProvider则是一种特殊的内容提供器，它使用了和内容提供器类似的机制来对数据进行保护，
                   可以选择性地将封装过的Uri共享给外部，从而提高了应用的安全性
                 */
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //大于等于版本24（7.0）的场合
                        imageUri = FileProvider.getUriForFile(context, "com.example.richang.fileprovider", outputImage);
                    } else {
                        //小于android 版本7.0（24）的场合
                        imageUri = Uri.fromFile(outputImage);
                    }

                    //启动相机程序
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //MediaStore.ACTION_IMAGE_CAPTURE = android.media.action.IMAGE_CAPTURE
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    context.startActivityForResult(intent, MainActivity.REQUEST_TAKE_PHOTO_FROM_EAT);
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
