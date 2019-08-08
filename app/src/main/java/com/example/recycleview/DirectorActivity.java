package com.example.recycleview;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
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
    RecyclerView mRecyclerView;
    @BindView(R.id.collapsing_toolbar_layout_director)
    CollapsingToolbarLayout mCollapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director);
        ButterKnife.bind(this);

        String director = getIntent().getExtras().getString("dir");

        mCollapsingToolbarLayout.setTitle(director);

        final RecyclerAdapter mRecyclerAdapter = new RecyclerAdapter(
                AppDatabase.getAppDatabase(this).dataBaseDao().findByDirector(director), this);

        ItemDecoration mItemDecoration = new ItemDecoration(80);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return mRecyclerAdapter.getItems().get(i).getType() == ListItem.TYPE_SERIES ? 1 : 2;
            }
        });

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}
