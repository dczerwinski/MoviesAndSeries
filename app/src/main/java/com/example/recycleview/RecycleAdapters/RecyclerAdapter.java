package com.example.recycleview.RecycleAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycleview.CategoryActivity;
import com.example.recycleview.DataBase.AppDatabase;
import com.example.recycleview.DataBase.DataBase;
import com.example.recycleview.MovieActivity;
import com.example.recycleview.R;
import com.example.recycleview.SeriesActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


class RecyclerViewHolder extends RecyclerView.ViewHolder{
    TextView textView;
    ImageView imageView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.txtDescription);
    }

    public void setTextViewBackground(Context context,String id){
        imageView = itemView.findViewById(R.id.imageView_item);
        int resID = context.getResources().getIdentifier(id,"drawable",context.getPackageName());
        Bitmap bitmapFactory = BitmapFactory.decodeResource(context.getResources(),resID);
        RoundedBitmapDrawable rnd = RoundedBitmapDrawableFactory.create(context.getResources(),bitmapFactory);
        rnd.setCornerRadius(90);
        this.imageView.setImageDrawable(rnd);
    }
}


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    public List<ListItem> items = new ArrayList<>();
    private Context context;
    private List<DataBase> dataBases;

    public RecyclerAdapter(Context context) {
        dataBases = AppDatabase.getAppDatabase(context).dataBaseDao().getAll();
        this.context = context;
        init();
    }


    public RecyclerAdapter(List<DataBase> dataBaseList, Context context){
        this.dataBases = dataBaseList;
        this.context = context;
        init_no_main();
    }


    public List<ListItem> getItems() {
        return items;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case ListItem.TYPE_HEADER:
                return new RecyclerViewHolder(inflater.inflate(R.layout.layout_item_recycler_view_category,parent,false));
            case ListItem.TYPE_MOVIE:
                return new RecyclerViewHolder(inflater.inflate(R.layout.layout_item_recycler_view,parent,false));
            case ListItem.TYPE_SERIES:
                return new RecyclerViewHolder(inflater.inflate(R.layout.layout_item_recycler_view_series,parent,false));
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerViewHolder mRecyclerViewHolder, final int position) {
        switch (getItemViewType(position)){
            case ListItem.TYPE_HEADER:
                mRecyclerViewHolder.textView.setText(((List<HeaderItem>)(Object)items).get(position).getCategory());
                break;
            case ListItem.TYPE_MOVIE:
                mRecyclerViewHolder.textView.setText(((List<MovieItem>)(Object)items).get(position).getTitle());
                mRecyclerViewHolder.setTextViewBackground(context,((List<MovieItem>)(Object)items).get(position).getImg_file());
                break;
            case ListItem.TYPE_SERIES:
                mRecyclerViewHolder.textView.setText(((List<SeriesItem>)(Object)items).get(position).getTitle());
                mRecyclerViewHolder.setTextViewBackground(context,((List<SeriesItem>)(Object)items).get(position).getImg_file());
                break;
            default:
                throw new IllegalStateException("unsupported item type");
        }

        mRecyclerViewHolder.textView.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getItemViewType(position)){
                    case ListItem.TYPE_MOVIE:
                        Intent intent = new Intent(v.getContext(), MovieActivity.class);
                        intent.putExtra("elo",AppDatabase.getAppDatabase(context).dataBaseDao().findByName(((List<MovieItem>)(Object)items).get(position).getTitle()).uid);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        v.getContext().startActivity(intent);
                        break;
                    case ListItem.TYPE_SERIES:
                        Intent intent2 = new Intent(v.getContext(), SeriesActivity.class);
                        intent2.putExtra("elo",AppDatabase.getAppDatabase(context).dataBaseDao().findByName(((List<SeriesItem>)(Object)items).get(position).getTitle()).uid);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        v.getContext().startActivity(intent2);
                        break;
                    case ListItem.TYPE_HEADER:
                        Intent intent3 = new Intent(v.getContext(), CategoryActivity.class);
                        intent3.putExtra("elo",((List<HeaderItem>)(Object)items).get(position).getCategory());
                        intent3.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        v.getContext().startActivity(intent3);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position){
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void init(){
        Collections.sort(dataBases, new Comparator<DataBase>() {
            @Override
            public int compare(DataBase t0, DataBase t1) {
                return t0.title.compareTo(t1.title);
            }
        });

        Collections.reverse(dataBases);

        List<String> categories = new ArrayList<>();
        for(DataBase dataBase: dataBases){
            switch (dataBase.type){
                case "movie":
                    items.add(new MovieItem(dataBase.title,dataBase.director,dataBase.img_file,dataBase.category));
                    break;
                case "series":
                    items.add(new SeriesItem(dataBase.title,dataBase.director,dataBase.img_file,dataBase.number,dataBase.category));
                    break;
            }
            if(!categories.contains(dataBase.category)){
                categories.add(dataBase.category);
            }
        }
        Collections.sort(items, new Comparator<ListItem>() {
            @Override
            public int compare(ListItem t0, ListItem t1) {
                return t0.getCategory().compareTo(t1.getCategory());
            }
        });

        for(String category: categories){
            items.add(new HeaderItem(category));
        }
        Collections.reverse(items);
        Collections.sort(items, new Comparator<ListItem>() {
            @Override
            public int compare(ListItem t0, ListItem t1) {
                return t0.getCategory().compareTo(t1.getCategory());
            }
        });
    }

    private void init_no_main(){
        Collections.sort(dataBases, new Comparator<DataBase>() {
            @Override
            public int compare(DataBase t0, DataBase t1) {
                return t0.title.compareTo(t1.title);
            }
        });
        for(DataBase dataBase: dataBases){
            switch (dataBase.type){
                case "movie":
                    items.add(new MovieItem(dataBase.title,dataBase.director,dataBase.img_file,dataBase.category));
                    break;
                case "series":
                    items.add(new SeriesItem(dataBase.title,dataBase.director,dataBase.img_file,dataBase.number,dataBase.category));
                    break;
            }
        }
    }

    public boolean canDropOver(int current,int target){
        if(!items.get(current).category.equals(items.get(target).category) )return false;
        if(items.get(target).getType() == ListItem.TYPE_HEADER)return false;
        return true;
    }
}
