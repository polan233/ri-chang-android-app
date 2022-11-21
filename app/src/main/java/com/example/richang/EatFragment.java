package com.example.richang;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    private Button btn_add;
    private Button btn_random;
    private Button btn_pre;
    private TextView tv_curdate;

    private RandomEatDialog randomEatDialog; //TODO 这个随机抽事物的弹窗没写

    private Calendar c;//获得日历实例
    private int m_year,m_month,m_day,m_weekday;
    private String curdate;
    private String[] weekdays;
    private String[] weekCharactor=new String[]{"一","二","三","四","五","六","日"};

    private ListView lv;
    private EatAdapter adapter; //TODO 这个类也要改
    private List<Eat> data=new ArrayList<>();  //TODO 这个类要改,是listview里的项

    private SQliteHelper sqLiteHelper;
    private SQLiteDatabase db;

    public EatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EatFragment newInstance(String param1, String param2) {
        EatFragment fragment = new EatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_todo, container, false);
        btn_add=(Button) view.findViewById(R.id.btn_eat_add);
        btn_pre=(Button) view.findViewById(R.id.btn_eat_show_pre);
        btn_random=(Button) view.findViewById(R.id.btn_eat_random);
        tv_curdate=(TextView)view.findViewById(R.id.tv_eat_top_cur_date);

        lv=(ListView)view.findViewById(R.id.lv_todo);
        sqLiteHelper=new SQliteHelper(view.getContext());
        db=sqLiteHelper.getReadableDatabase();

        c = Calendar.getInstance();
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);
        m_weekday=c.get(Calendar.DAY_OF_WEEK);
        weekdays=getWeekDays();
        curdate=m_year+"/"+(m_month+1)+"/"+m_day+" 周"+weekCharactor[m_weekday-1];

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEatEditDialog();//展示添加新eat的弹窗并完成添加操作
            }
        });

        adapter=new EatAdapter(view.getContext(),R.layout.eat_item,data); //TODO eat item 的布局
        adapter.setDb(db);
        //还需要把数据库的引用传给EatAdapter
        lv.setAdapter(adapter);
        refreshEat();
        return view;
    }

    public void showEatEditDialog(){ //TODO 对于这个页面只有新增,删除,查询指定日期所有item 的需求

    }
    public void showEatRandomDialog(){ //TODO 完成这个弹窗相关的代码 弹窗随机选择一个食物 内部可以添加删除待选项

    }
    public void showTodayEat(){ //TODO 对于这个页面只有新增,删除,查询指定日期所有item 的需求

    }
    public void readEatByDate(String curdate){ //TODO 对于这个页面只有新增,删除,查询指定日期所有item 的需求

    }
    public void addEat(Eat eat){  //TODO 对于这个页面只有新增,删除,查询指定日期所有item 的需求

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
}