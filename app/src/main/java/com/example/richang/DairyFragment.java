package com.example.richang;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
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

    private Calendar c;//获得日历实例
    private int m_year, m_month, m_day, m_weekday;

    private String curdate;
    private String[] weekdays;
    private String[] weekCharactor = new String[]{"一", "二", "三", "四", "五", "六", "日"};

    private ListView lv_pic;
    private ListView lv_text;
    private DairyTextAdapter textAdapter; //TODO 这个类也要改
    private DaityPicAdapter picAdapter;
    private List<DairyText> textData = new ArrayList<>();
    private List<DairyPic> picData = new ArrayList<>();

    private SQliteHelper sqLiteHelper;
    private SQLiteDatabase db;
    public static final int REQUEST_CODE_TAKE_PICTURE = 1;


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
        view = inflater.inflate(R.layout.fragment_eat, container, false);

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
        curdate = m_year + "/" + (m_month + 1) + "/" + m_day + " 周" + weekCharactor[m_weekday - 1];

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

        lv_text.setAdapter(textAdapter);
        lv_pic.setAdapter(picAdapter);
        refreshDairy();
        return view;
    }




    public void refreshDairy() {
        picData.clear();
        textData.clear();
        readPicByDate(curdate);
        readTextByDate(curdate);
    }

    public void readPicByDate(String date) {
        Cursor cursor = db.query("eat_list", new String[]{"title", "detail", "image", "writeTime"},
                "date = ?",
                new String[]{date}, null, null, "id ASC", null);
        while (cursor.moveToNext()) {

        }
        cursor.close();
        picAdapter.notifyDataSetChanged();
    }

    public void readTextByDate(String date) {
        Cursor cursor = db.query("eat_list", new String[]{"title", "detail", "image", "writeTime"},
                "date = ?",
                new String[]{date}, null, null, "id ASC", null);
        while (cursor.moveToNext()) {

            textData.add(new Eat(image, title, detail, createTime));
        }
        cursor.close();
        textAdapter.notifyDataSetChanged();
    }

    private void setCurdateByCalendar(Calendar c){
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);
        m_weekday = c.get(Calendar.DAY_OF_WEEK);
        weekdays = getWeekDays();
        curdate = m_year + "/" + (m_month + 1) + "/" + m_day + " 周" + weekCharactor[m_weekday - 1];
        tv_curdate.setText(curdate);
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

    private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }
}