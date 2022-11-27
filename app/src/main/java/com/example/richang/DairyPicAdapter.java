package com.example.richang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
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
    private AlertDialog alertDialog=null;
    private AlertDialog.Builder builder;

    public EditPicListener meditListener;
    public interface EditPicListener{
        public void onPicEdit();
    }
    public void setPicEditListener(EditPicListener eListener){
        this.meditListener=eListener;
    }

    public interface ShowPopPic{
        public void onShow(Drawable image);
    }
    public ShowPopPic showPopPic;
    public void setOnShow(ShowPopPic show){
        this.showPopPic=show;
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
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopPic.onShow(dp.image);
                }
            });
            builder=new AlertDialog.Builder(view.getContext());
            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    builder.setMessage("确定要删除该图片吗?");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除
                            deleteDairyPic(dp);
                            data.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    if(alertDialog==null){
                        alertDialog=builder.create();
                    }
                    alertDialog.show();
                    return true;
                }
            });
        } else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.dairy_add_item, parent, false);
            ImageButton ib=(ImageButton) view.findViewById(R.id.btn_dairy_text_add);
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加图片的代码
                    if(meditListener!=null)
                        meditListener.onPicEdit();
                }
            });
        }
        return view;
    }
//    private void showPopPicDialog(Drawable image){
//        popPicDialog= new PopPicDialog(this.getContext()., R.style.CustomDialog,
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        popPicDialog.dismiss();
//                    }
//                },image);
//        popPicDialog.show();
//    }
    public void setDb(SQLiteDatabase db){
        this.db=db;
    }
    public void setFragment(DairyFragment fragment){
        this.df=fragment;
    }
    public  void deleteDairyPic(DairyPic dp){
        if(db!=null) {
            String str = "delete from dairy_pic where writeTime=?";
            db.execSQL(str, new String[]{String.valueOf(dp.createTime)});
        }
    }

}
