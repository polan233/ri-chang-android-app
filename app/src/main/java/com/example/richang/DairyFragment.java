package com.example.richang;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DairyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DairyFragment extends Fragment {


    private String mParam1;
    private String mParam2;

    private View view;

    private Button btn_pre;
    private Button btn_next;
    private Button btn_choose;


    private TextView tv_curdate;


    private CreateDairyTextDialog createDairyTextDialog = null;
    private CreateDairyPicDialog createDairyPicDialog = null;
    private PopPicDialog   popPicDialog=null;


    private Calendar c;//获得日历实例
    private int m_year, m_month, m_day, m_weekday;

    private String curdate;
    private String curweekday;
    private String curdate_full;
    private String todayDate=MainActivity.getCurDate();
    private String[] weekdays;
    private String[] weekCharactor = new String[]{"一", "二", "三", "四", "五", "六", "日"};

    private ListView lv_pic;
    private ListView lv_text;
    private DairyTextAdapter textAdapter;
    private DairyPicAdapter picAdapter;
    private List<DairyText> textData = new ArrayList<>();
    private List<DairyPic> picData = new ArrayList<>();

    private SQliteHelper sqLiteHelper;
    private SQLiteDatabase db;
    public static final int REQUEST_CODE_TAKE_PICTURE = 1;

    public Uri imageUri=null;


    public DairyFragment() {
        // Required empty public constructor
    }

    public static DairyFragment newInstance() {
        DairyFragment fragment = new DairyFragment();
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
        view = inflater.inflate(R.layout.fragment_dairy, container, false);

        btn_pre = (Button) view.findViewById(R.id.btn_dairy_pre);
        btn_choose = (Button) view.findViewById(R.id.btn_dairy_choose);
        btn_next = (Button) view.findViewById(R.id.btn_dairy_next);

        tv_curdate = (TextView) view.findViewById(R.id.tv_dairy_curdate);

        lv_pic = (ListView) view.findViewById(R.id.lv_dairy_pic);
        lv_text = (ListView) view.findViewById(R.id.lv_dairy_text);


        sqLiteHelper = new SQliteHelper(view.getContext());
        db = sqLiteHelper.getReadableDatabase();

        c = Calendar.getInstance();
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);
        m_weekday = c.get(Calendar.DAY_OF_WEEK);
        weekdays = getWeekDays();
        curdate = m_year + "/" + (m_month + 1) + "/" + m_day;
        curweekday="周" + weekCharactor[m_weekday - 1];
        curdate_full=curdate+" "+curweekday;

        tv_curdate.setText(curdate);

        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DAY_OF_MONTH,-1);
                setCurdateByCalendar(c);
                refreshDairy();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DAY_OF_MONTH,1);
                setCurdateByCalendar(c);
                refreshDairy();
            }
        });
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialog(getContext(),0).show();
            }
        });

        textAdapter = new DairyTextAdapter(view.getContext(), R.layout.dairy_text_item, textData);
        picAdapter = new DairyPicAdapter(view.getContext(), R.layout.dairy_pic_item, picData);

        textAdapter.setDb(db);
        picAdapter.setDb(db);

        picAdapter.setPicEditListener(new DairyPicAdapter.EditPicListener() {
            @Override
            public void onPicEdit() {
                showPicEditDialog();
            }
        });
        textAdapter.setTextEditListener(new DairyTextAdapter.EditTextListener() {
            @Override
            public void onTextEdit() {
                showTextEditDialog();
            }
        });
        picAdapter.setOnShow(new DairyPicAdapter.ShowPopPic() {
            @Override
            public void onShow(Drawable image) {
                showPopPicDialog(image);
            }
        });


        lv_text.setAdapter(textAdapter);
        lv_pic.setAdapter(picAdapter);
        textAdapter.df=this;
        refreshDairy();
        return view;
    }


    public void showTextEditDialog(){
        createDairyTextDialog = new CreateDairyTextDialog((MainActivity) getActivity(), R.style.CustomDialog,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String context ="    "+createDairyTextDialog.et_content.getText().toString().trim();
                        String mdate=createDairyTextDialog.tv_date.getText().toString().trim();
                        java.util.Date date = new java.util.Date();
                        long datetime = date.getTime();
                        DairyText item = new DairyText(context,mdate,datetime);
                        addDairyText(item);
                        refreshDairyText();
                        createDairyTextDialog.dismiss();
                    }
                }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDairyTextDialog.dismiss();
            }
        });
        createDairyTextDialog.show();

    }
    public void showPicEditDialog(){
        createDairyPicDialog = new CreateDairyPicDialog((MainActivity) getActivity(), R.style.CustomDialog,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //从相册获取图片
                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            //Intent.ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT"
                            intent.setType("image/*");
                            getActivity().startActivityForResult(intent, MainActivity.REQUEST_PICK_PHOTO_TO_DAIRY); // 打开相册
                        }
                    }
                }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //从相机获取图片
                // 创建File对象，用于存储拍照后的图片
                //存放在手机SD卡的应用关联缓存目录下
                File outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
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
                    imageUri = FileProvider.getUriForFile(getActivity(), "com.example.richang.fileprovider", outputImage);
                } else {
                    //小于android 版本7.0（24）的场合
                    imageUri = Uri.fromFile(outputImage);
                }

                //启动相机程序
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //MediaStore.ACTION_IMAGE_CAPTURE = android.media.action.IMAGE_CAPTURE
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                getActivity().startActivityForResult(intent, MainActivity.REQUEST_TAKE_PHOTO_FROM_DAIRY);

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(createDairyPicDialog!=null){
                    String mdate=MainActivity.getCurDate();
                    Drawable pic = createDairyPicDialog.iv.getBackground();
                    java.util.Date date = new java.util.Date();
                    long datetime = date.getTime();

                    DairyPic item=new DairyPic(pic,mdate,datetime);
                    addDairyPic(item);
                    //refreshDairyPic(); 异步刷新

                    createDairyPicDialog.dismiss();
                }
            }
        }
                , new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDairyPicDialog.dismiss();
            }
        });
        createDairyPicDialog.show();

    }
    public void addDairyText(DairyText dairyText){
        ContentValues values=new ContentValues();
        values.put("date",dairyText.date);
        values.put("writeTime",dairyText.createTime);
        values.put("content",dairyText.content);
        db.insert("dairy_text","id",values);
    }
    public void addDairyPic(DairyPic dairyPic){
        new InsetDairyPicTask().execute(dairyPic);
    }
    private class InsetDairyPicTask extends AsyncTask<DairyPic,Integer,Integer>{
        @Override
        protected Integer doInBackground(DairyPic[] objects) {
            DairyPic dairyPic=objects[0];
            ContentValues values=new ContentValues();
            values.put("date",dairyPic.date);
            values.put("writeTime",dairyPic.createTime);
            Bitmap bm=getBitmapFromDrawable(dairyPic.image);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG,100,os);
            values.put("image",os.toByteArray());
            db.insert("dairy_pic","id",values);
            publishProgress(1);
            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... i) {
           refreshDairyPic();
        }

    }
    public void refreshDairy() {
        picData.clear();
        textData.clear();
        readPicByDate(curdate);
        readTextByDate(curdate);  //判断日期是不是今天,是的话就把添加按钮加进去
    }
    public void refreshDairyText() {
        textData.clear();
        readTextByDate(curdate);
    }
    public void refreshDairyPic() {
        picData.clear();
        readPicByDate(curdate);
    }

    public void readPicByDate(String date) {
        Cursor cursor = db.query("dairy_pic", new String[]{"date", "writeTime", "image"},
                "date = ?",
                new String[]{date}, null, null, "id ASC", null);
        while (cursor.moveToNext()) {
            String mdate=cursor.getString(0);
            Long writeTime=cursor.getLong(1);
            byte[] blob=cursor.getBlob(2);
            Bitmap bmp=BitmapFactory.decodeByteArray(blob,0,blob.length);
            BitmapDrawable image=new BitmapDrawable(bmp);
            picData.add(new DairyPic(image,mdate,writeTime));
        }
        cursor.close();
        if(date.equals(todayDate)){
            picData.add(new DairyPic(DairyPic.TYPE_BUTTON));
        }
        picAdapter.notifyDataSetChanged();
    }

    public void readTextByDate(String date) {
        Cursor cursor = db.query("dairy_text", new String[]{"content","writeTime"},
                "date = ?",
                new String[]{date}, null, null, "id ASC", null);
        while (cursor.moveToNext()) {
            String str=cursor.getString(0);
            long createTime=cursor.getLong(1);
            textData.add(new DairyText(str,date,createTime));
        }
        cursor.close();
        if(date.equals(todayDate)){
            textData.add(new DairyText(DairyText.TYPE_BUTTON));
        }
        textAdapter.notifyDataSetChanged();
    }

    private void setCurdateByCalendar(Calendar c){
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);
        m_weekday = c.get(Calendar.DAY_OF_WEEK);
        weekdays = getWeekDays();
        curdate = m_year + "/" + (m_month + 1) + "/" + m_day ;
        curweekday="周" + weekCharactor[m_weekday - 1];
        curdate_full=curdate+" "+curweekday;
        tv_curdate.setText(curdate);
    }
    public static String[] getWeekDays() {
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
            list[i] = date;
        }
        return list;
    }

    //调用Activity的showDialog( )方法显示进对话框
    protected Dialog CreateDialog(Context context, int id) {
        return new DatePickerDialog(context, l1, m_year, m_month, m_day);
    }

    //设置日期监听器
    private DatePickerDialog.OnDateSetListener l1 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            m_year = year;
            m_month = monthOfYear;
            m_day = dayOfMonth;
            c.set(Calendar.YEAR,m_year);
            c.set(Calendar.MONTH,m_month);
            c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            setCurdateByCalendar(c);
            refreshDairy();

        }
    };
    public void setImage(Drawable image){
        if(createDairyPicDialog!=null){
            createDairyPicDialog.changeImage(image);
        }
    }
    private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }
    private void showPopPicDialog(Drawable image){
        popPicDialog= new PopPicDialog((MainActivity)getActivity(), R.style.CustomDialog,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popPicDialog.dismiss();
                    }
                },image);
        popPicDialog.show();
    }
}