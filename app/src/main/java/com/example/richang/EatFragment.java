package com.example.richang;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.ConversationActions;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EatFragment extends Fragment {





    private String mParam1;
    private String mParam2;

    private View view;

    private Button btn_add;
    private Button btn_random;
    private RadioGroup rg_eat;
    private int eatSelected=R.id.rb_eat_now;
    private TextView tv_curdate;
    private PopPicDialog popPicDialog=null;

    //private RandomEatDialog randomEatDialog; //TODO 这个随机抽事物的弹窗没写
    public CreateEatDialog createEatDialog=null;

    private Calendar c;//获得日历实例
    private int m_year,m_month,m_day,m_weekday;
    private String todaydate;
    private String curdate;
    private String[] weekdays;
    private String[] weekCharactor=new String[]{"一","二","三","四","五","六","日"};

    private ListView lv;
    private EatAdapter adapter; //TODO 这个类也要改
    private List<Eat> data=new ArrayList<>();

    private SQliteHelper sqLiteHelper;
    private SQLiteDatabase db;



    public EatFragment() {
        // Required empty public constructor
    }

    public static EatFragment newInstance() {
        EatFragment fragment = new EatFragment();
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
        view=inflater.inflate(R.layout.fragment_eat, container, false);
        rg_eat=(RadioGroup)view.findViewById(R.id.rg_eat);
        btn_random=(Button) view.findViewById(R.id.btn_eat_random);
        tv_curdate=(TextView)view.findViewById(R.id.tv_eat_curdate);
        btn_add=(Button)view.findViewById(R.id.btn_eat_add) ;


        tv_curdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialog(getContext(),0).show();
            }
        });
        rg_eat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                eatSelected=checkedId;
                refreshEat();
            }
        });

        lv=(ListView)view.findViewById(R.id.lv_eat);
        sqLiteHelper=new SQliteHelper(view.getContext());
        db=sqLiteHelper.getReadableDatabase();

        c = Calendar.getInstance();
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);
        m_weekday=c.get(Calendar.DAY_OF_WEEK);
        weekdays=getWeekDays();
        curdate=m_year+"/"+(m_month+1)+"/"+m_day;
        todaydate=curdate;
        tv_curdate.setText(curdate);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEatEditDialog();//展示添加新eat的弹窗并完成添加操作
            }
        });

        adapter=new EatAdapter(view.getContext(),R.layout.eat_item,data);
        adapter.setDb(db);
        adapter.setOnShow(new DairyPicAdapter.ShowPopPic() {
            @Override
            public void onShow(Drawable image) {
                showPopPicDialog(image);
            }
        });
        //还需要把数据库的引用传给EatAdapter
        lv.setAdapter(adapter);
        refreshEat();
        return view;
    }


    public void showEatEditDialog(){
        createEatDialog = new CreateEatDialog((MainActivity) getActivity(), R.style.CustomDialog,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = createEatDialog.et_title.getText().toString().trim();
                        String detail = createEatDialog.et_detail.getText().toString().trim();
                        Drawable pic = createEatDialog.ib_image.getBackground();
                        java.util.Date date = new java.util.Date();
                        long datetime = date.getTime();

                        Eat item = new Eat(pic, title, detail, datetime);
                        addEat(item);
                        refreshEat();

                        createEatDialog.dismiss();
                    }
                }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEatDialog.dismiss();
            }
        });
        createEatDialog.show();

    }



    public void showEatRandomDialog(){ //TODO 完成这个弹窗相关的代码 弹窗随机选择一个食物 内部可以添加删除待选项

    }
    public void refreshEat(){
        data.clear();
        switch (eatSelected){
            case R.id.rb_eat_now:
                tv_curdate.setText(todaydate);
                readEatByDate(todaydate);
                break;
            case R.id.rb_eat_pre:
                readEatByDate(curdate);
                break;
        }
    }

    public void readEatByDate(String date){
        Cursor cursor=db.query("eat_list",new String[]{"title","detail","image","writeTime"},
                "date = ?",
                new String[]{date},null,null,"id ASC",null);
        while(cursor.moveToNext()){
            String title=cursor.getString(0);
            String detail=cursor.getString(1);
            byte[] blob=cursor.getBlob(2);
            Bitmap bmp= BitmapFactory.decodeByteArray(blob,0,blob.length);
            BitmapDrawable image=new BitmapDrawable(bmp);
            long createTime=cursor.getLong(3);
            data.add(new Eat(image,title,detail,createTime));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
    public void addEat(Eat eat){
        ContentValues values=new ContentValues();
        values.put("date",todaydate);
        values.put("writeTime",eat.createTime);
        values.put("title",eat.title);
        values.put("detail",eat.detail);
        Bitmap bmp=getBitmapFromDrawable(eat.pic);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,os);
        values.put("image",os.toByteArray());
        db.insert("eat_list","id",values);
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
            curdate=m_year+"/"+(m_month+1)+"/"+m_day;
            tv_curdate.setText(curdate);

            refreshEat();

        }
    };
    private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    public void setImage(Drawable image) {
        if(createEatDialog!=null){
            createEatDialog.changeImage(image);
        }
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