package com.example.recycleview;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.recycleview.DataBase.AppDatabase;
import com.example.recycleview.DataBase.DataBase;
import com.example.recycleview.RecycleAdapters.ItemDecoration;
import com.example.recycleview.RecycleAdapters.RecyclerAdapterSeries;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SeriesActivity extends AppCompatActivity{
    @BindView(R.id.collapsing_toolbar_layout_series) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.gora_series) ImageView imageView;
    @BindView(R.id.recycle_series) RecyclerView recyclerView;
    @BindView(R.id.rezyser_series) TextView tv_rezyser;
    @BindView(R.id.app_bar_series) AppBarLayout appBarLayout;
    @BindView(R.id.toolbar_series) Toolbar toolbar;
    private DataBase series;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);
        ButterKnife.bind(this);


        int uid = getIntent().getExtras().getInt("elo");
        series = AppDatabase.getAppDatabase(this).dataBaseDao().findById(uid);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == -collapsingToolbarLayout.getHeight() + toolbar.getHeight()) {
                    collapsingToolbarLayout.setTitle(series.title);
                }
                else{
                    collapsingToolbarLayout.setTitle("");
                }
            }
        });

        tv_rezyser.setText("Rezyser: " + series.director);
        imageView.setImageResource(this.getResources().getIdentifier(series.img_file,"drawable",this.getPackageName()));

        final RecyclerAdapterSeries recyclerAdapterSeries = new RecyclerAdapterSeries(series,this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDecoration(80));
        recyclerView.setAdapter(recyclerAdapterSeries);

        final List<String> itemList = recyclerAdapterSeries.getEpisodes();

        recyclerView.scrollToPosition(1);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("elo",String.valueOf(dx)+"  "+String.valueOf(dy));
                int firstItemVisible = layoutManager.findFirstVisibleItemPosition();
                if (firstItemVisible != 1 && firstItemVisible % itemList.size() == 1) {
                    layoutManager.scrollToPosition(1);
                }
                int firstCompletelyItemVisible = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyItemVisible == 0) {
                    layoutManager.scrollToPositionWithOffset(itemList.size(), 0);
                }
            }

        });

        tv_rezyser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DirectorActivity.class);
                intent.putExtra("dir",series.director);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                v.getContext().startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy(){
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}
