package com.example.recycleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.recycleview.DataBase.AppDatabase;
import com.example.recycleview.DataBase.DataBase;
import com.example.recycleview.RecycleAdapters.ListItem;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        int uid = getIntent().getIntExtra("edit",-1);

        DataBase mDataBase =  AppDatabase.getAppDatabase(this).dataBaseDao().findById(uid);

        switch (mDataBase.category){
            case "":
                break;
            case "1":
                break;
            case "2":
                break;
        }

        findViewById(R.id.edit_btn_save).setOnClickListener(v -> {
            Toast.makeText(this,"Saved!",Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
