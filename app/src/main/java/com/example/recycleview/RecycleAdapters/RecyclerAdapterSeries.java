package com.example.recycleview.RecycleAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.recycleview.DataBase.DataBase;
import com.example.recycleview.R;
import java.util.ArrayList;
import java.util.List;



public class RecyclerAdapterSeries extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<String> episodes;
    private Context context;
    private DataBase series;


    public RecyclerAdapterSeries(DataBase series, Context context) {
        this.episodes = new ArrayList<>();
        this.series = series;
        for(int i=0; i<series.number; i++){
            this.episodes.add("Odcinek "+String.valueOf(i+1));
        }
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_item_recycler_view_series,parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder recyclerViewHolder, final int i) {
        recyclerViewHolder.mTextView.setText(episodes.get(i%episodes.size()));
        recyclerViewHolder.setTextViewBackground(context,series.img_file);
    }

    @Override
    public int getItemCount() {
        return episodes == null ? 0 : episodes.size() *2  +1;
    }

    public List<String> getEpisodes() {
        return episodes;
    }
}
