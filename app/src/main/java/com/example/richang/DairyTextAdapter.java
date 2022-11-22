package com.example.richang;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class DairyTextAdapter extends ArrayAdapter<DairyText> {
    private List<DairyText> data=null;
    private SQLiteDatabase db;
    private DairyFragment df=null;

    public EditTextListener meditListener;
    public interface EditTextListener{
        public void onTextEdit();
    }
    public void setTextEditListener(EditTextListener eListener){
        this.meditListener=eListener;
    }


    public DairyTextAdapter(@NonNull Context context, int resource, @NonNull List<DairyText> objects){
        super(context,resource,objects);
        data=objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertview,@NonNull ViewGroup parent) {
        DairyText dt = getItem(position);
        View view=null;
        if (dt.type == DairyText.TYPE_ITEM) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.dairy_text_item, parent, false);
            TextView tv=(TextView) view.findViewById(R.id.tv_dairy_text_content);

            tv.setText(dt.content);
        } else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.dairy_add_item, parent, false);
            ImageButton ib=(ImageButton) view.findViewById(R.id.btn_dairy_text_add);
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 按下按钮生成添加文字的界面
                    if(meditListener!=null){
                        meditListener.onTextEdit();
                    }
                }
            });
        }
        return view;
    }
    public void setDb(SQLiteDatabase db){
        this.db=db;
    }
    public void setFragment(DairyFragment fragment){
        this.df=fragment;
    }

}
