package com.example.richang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TodoActivity extends AppCompatActivity {
    private TextView nav_eat;
    private TextView nav_habit;
    private TextView nav_dairy;
    private Button btn_delete;
    private Button btn_add;
    private CreateTodoDialog createTodoDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        nav_eat=(TextView) findViewById(R.id.tv_eat);
        nav_habit=(TextView) findViewById(R.id.tv_habit);
        nav_dairy=(TextView) findViewById(R.id.tv_dairy);
        btn_delete=(Button) findViewById(R.id.btn_delete);
        btn_add=(Button) findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    public void showEditDialog(){
        createTodoDialog = new CreateTodoDialog(this,R.style.CustomDialog,
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = createTodoDialog.et_name.getText().toString().trim();
                String date = createTodoDialog.tv_date.getText().toString().trim();


                Log.i("todotest", name + " " + date);

                createTodoDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTodoDialog.dismiss();
            }
        });
        createTodoDialog.show();
    }
}