package com.example.richang;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DairyPicAdapter extends ArrayAdapter<DairyPic> {
    private List<DairyPic> data=null;
    private SQLiteDatabase db;
    private DairyFragment df=null;

    public EditPicListener meditListener;
    public interface EditPicListener{
        public void onPicEdit();
    }
    public void setPicEditListener(EditPicListener eListener){
        this.meditListener=eListener;
    }


    public DairyPicAdapter(@NonNull Context context, int resource, @NonNull List<DairyPic> objects){
        super(context,resource,objects);
        data=objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertview,@NonNull ViewGroup parent) {
        DairyPic dp = getItem(position);
        View view=null;
        if (dp.type == DairyPic.TYPE_ITEM) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.dairy_pic_item, parent, false);
            ImageView iv=(ImageView) view.findViewById(R.id.iv_dairy_pic);
            iv.setImageDrawable(dp.image);
        } else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.dairy_add_item, parent, false);
            ImageButton ib=(ImageButton) view.findViewById(R.id.btn_dairy_text_add);
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 添加图片的代码
                    if(meditListener!=null)
                        meditListener.onPicEdit();
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
