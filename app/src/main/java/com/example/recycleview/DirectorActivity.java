package com.example.recycleview;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.recycleview.DataBase.AppDatabase;
import com.example.recycleview.RecycleAdapters.ItemDecoration;
import com.example.recycleview.RecycleAdapters.ListItem;
import com.example.recycleview.RecycleAdapters.RecyclerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DirectorActivity extends AppCompatActivity {
    @BindView(R.id.recycle_director)
    RecyclerView recyclerView;
    @BindView(R.id.collapsing_toolbar_layout_director)
    CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director);
        ButterKnife.bind(this);

        String director = getIntent().getExtras().getString("dir");

        collapsingToolbarLayout.setTitle(director);

        final RecyclerAdapter recyclerAdapter = new RecyclerAdapter(AppDatabase.getAppDatabase(this).dataBaseDao().findByDirector(director), this);

        ItemDecoration itemDecoration = new ItemDecoration(80);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return recyclerAdapter.getItems().get(i).getType() == ListItem.TYPE_SERIES ? 1 : 2;
            }
        });

        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(recyclerAdapter);
    }
    @Override
    protected void onDestroy(){
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}
