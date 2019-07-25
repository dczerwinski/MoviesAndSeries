package com.example.recycleview;

import android.annotation.SuppressLint;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;
import com.example.recycleview.RecycleAdapters.ItemDecoration;
import com.example.recycleview.RecycleAdapters.ListItem;
import com.example.recycleview.RecycleAdapters.MyItemTouchHelperCallback;
import com.example.recycleview.RecycleAdapters.RecyclerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity{
    @BindView(R.id.recycle) RecyclerView recyclerView;
    @BindView(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar) AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)Toolbar toolbar;
    ItemTouchHelper helper;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this);


        appBarLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(), "Edit Mode ON", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == -collapsingToolbarLayout.getHeight() + toolbar.getHeight()) {
                    collapsingToolbarLayout.setTitle("Filmy i seriale");
                }
                else{
                    collapsingToolbarLayout.setTitle("");
                }
            }
        });

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
        helper = new ItemTouchHelper(new MyItemTouchHelperCallback(recyclerAdapter));
        helper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(recyclerAdapter);
    }
}


