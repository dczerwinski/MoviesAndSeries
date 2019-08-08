package com.example.recycleview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;
import com.example.recycleview.RecycleAdapters.ItemDecoration;
import com.example.recycleview.RecycleAdapters.ListItem;
import com.example.recycleview.RecycleAdapters.MyItemTouchHelperCallback;
import com.example.recycleview.RecycleAdapters.RecyclerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    ItemTouchHelper mItemTouchHelper;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final RecyclerAdapter mRecyclerAdapter = new RecyclerAdapter(this);


        mAppBarLayout.setOnLongClickListener(view -> {
            Toast.makeText(view.getContext(), "Edit Mode ON", Toast.LENGTH_LONG).show();
            return true;
        });

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, i) -> {
            if (i == -mCollapsingToolbarLayout.getHeight() + mToolbar.getHeight()) {
                mCollapsingToolbarLayout.setTitle("Filmy i seriale");
            } else {
                mCollapsingToolbarLayout.setTitle("");
            }
        });

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
        mRecyclerAdapter.setmItemTouchHelper(mItemTouchHelper);
        mRecyclerView.setAdapter(mRecyclerAdapter);




    }
}


