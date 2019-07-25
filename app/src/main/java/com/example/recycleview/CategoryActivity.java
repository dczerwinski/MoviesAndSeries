package com.example.recycleview;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.TextView;
import com.example.recycleview.DataBase.AppDatabase;
import com.example.recycleview.DataBase.DataBase;
import com.example.recycleview.RecycleAdapters.ItemDecoration;
import com.example.recycleview.RecycleAdapters.ListItem;
import com.example.recycleview.RecycleAdapters.MyItemTouchHelperCallback;
import com.example.recycleview.RecycleAdapters.RecyclerAdapter;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity  extends AppCompatActivity {
    @BindView(R.id.recycle_category) RecyclerView mRecyclerView;
    @BindView(R.id.collapsing_toolbar_layout_category) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.app_bar_category) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar_category) Toolbar mToolbar;
    @BindView(R.id.title_category) TextView tv_title;
    ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        final String title = getIntent().getExtras().getString("elo");
        tv_title.setText("\n"+title);
        tv_title.setTextSize(50);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == -mCollapsingToolbarLayout.getHeight() + mToolbar.getHeight()) {
                    mCollapsingToolbarLayout.setTitle(title);
                }
                else{
                    mCollapsingToolbarLayout.setTitle("");
                }
            }
        });


        List<DataBase> dataBases = AppDatabase.getAppDatabase(this).dataBaseDao().findByCategory(title);

        final RecyclerAdapter mRecyclerAdapter = new RecyclerAdapter(dataBases,this);
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

        mItemTouchHelper = new ItemTouchHelper(new MyItemTouchHelperCallback(mRecyclerAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(mRecyclerAdapter);
    }


    @Override
    protected void onDestroy(){
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}
