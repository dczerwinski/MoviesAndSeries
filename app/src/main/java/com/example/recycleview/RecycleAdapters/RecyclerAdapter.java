package com.example.recycleview.RecycleAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

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


class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView mTextView;
    ImageView mImageView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.txtDescription);
    }

    public void setTextViewBackground(Context context, String id) {
        mImageView = itemView.findViewById(R.id.imageView_item);
        int resID = context.getResources().getIdentifier(id, "drawable", context.getPackageName());
        Bitmap bitmapFactory = BitmapFactory.decodeResource(context.getResources(), resID);
        RoundedBitmapDrawable rnd =
                RoundedBitmapDrawableFactory.create(context.getResources(), bitmapFactory);
        rnd.setCornerRadius(90);
        this.mImageView.setImageDrawable(rnd);
    }
}


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    public List<ListItem> items = new ArrayList<>();
    private Context context;
    private List<DataBase> dataBases;
    private ItemTouchHelper mItemTouchHelper;
    private PopupMenu popupMenu;

    public void hideMenu(){
        if(popupMenu!=null) popupMenu.dismiss();
    }

    public void setmItemTouchHelper(ItemTouchHelper mItemTouchHelper) {
        this.mItemTouchHelper = mItemTouchHelper;
    }

    public RecyclerAdapter(Context context) {
        popupMenu = null;
        mItemTouchHelper=null;
        dataBases = AppDatabase.getAppDatabase(context).dataBaseDao().getAll();
        this.context = context;
        init();
    }


    public RecyclerAdapter(List<DataBase> dataBaseList, Context context) {
        popupMenu = null;
        mItemTouchHelper=null;
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
        switch (viewType) {
            case ListItem.TYPE_HEADER:
                return new RecyclerViewHolder(
                        inflater.inflate(R.layout.layout_item_recycler_view_category, parent,
                                false));
            case ListItem.TYPE_MOVIE:
                return new RecyclerViewHolder(
                        inflater.inflate(R.layout.layout_item_recycler_view, parent, false));
            case ListItem.TYPE_SERIES:
                return new RecyclerViewHolder(
                        inflater.inflate(R.layout.layout_item_recycler_view_series, parent, false));
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder mRecyclerViewHolder,
            final int position) {
        switch (getItemViewType(position)) {
            case ListItem.TYPE_HEADER:
                mRecyclerViewHolder.mTextView
                        .setText(((List<HeaderItem>) (Object) items).get(position).getCategory());
                break;
            case ListItem.TYPE_MOVIE:
                mRecyclerViewHolder.mTextView
                        .setText(((List<MovieItem>) (Object) items).get(position).getTitle());
                mRecyclerViewHolder.setTextViewBackground(context,
                        ((List<MovieItem>) (Object) items).get(position).getImg_file());
                break;
            case ListItem.TYPE_SERIES:
                mRecyclerViewHolder.mTextView
                        .setText(((List<SeriesItem>) (Object) items).get(position).getTitle());
                mRecyclerViewHolder.setTextViewBackground(context,
                        ((List<SeriesItem>) (Object) items).get(position).getImg_file());
                break;
            default:
                throw new IllegalStateException("unsupported item type");
        }


        mRecyclerViewHolder.mTextView.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                if(getItemViewType(position) != ListItem.TYPE_HEADER) {
                    popupMenu = new PopupMenu(context, view);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.item_menu, popupMenu.getMenu());
                    // popupMenu.setGravity();
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getTitle().equals("Edit")){
                                Intent intent = new Intent(view.getContext(), EditItemActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                view.getContext().startActivity(intent);
                            }
                            else{
                                //TODO
                            }
                            return true;
                        }
                    });
                }
                if(mItemTouchHelper!=null) mItemTouchHelper.startDrag(mRecyclerViewHolder);
                return true;
            }
        });

        mRecyclerViewHolder.mTextView.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getItemViewType(position)) {
                    case ListItem.TYPE_MOVIE:
                        Intent intent = new Intent(v.getContext(), MovieActivity.class);
                        intent.putExtra("elo", AppDatabase.getAppDatabase(context).dataBaseDao()
                                .findByName(((List<MovieItem>) (Object) items).get(position)
                                        .getTitle()).uid);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        v.getContext().startActivity(intent);
                        break;
                    case ListItem.TYPE_SERIES:
                        Intent intent2 = new Intent(v.getContext(), SeriesActivity.class);
                        intent2.putExtra("elo", AppDatabase.getAppDatabase(context).dataBaseDao()
                                .findByName(((List<SeriesItem>) (Object) items).get(position)
                                        .getTitle()).uid);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        v.getContext().startActivity(intent2);
                        break;
                    case ListItem.TYPE_HEADER:
                        Intent intent3 = new Intent(v.getContext(), CategoryActivity.class);
                        intent3.putExtra("elo",
                                ((List<HeaderItem>) (Object) items).get(position).getCategory());
                        intent3.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        v.getContext().startActivity(intent3);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void init() {
        Collections.sort(dataBases, new Comparator<DataBase>() {
            @Override
            public int compare(DataBase t0, DataBase t1) {
                return t0.title.compareTo(t1.title);
            }
        });

        Collections.reverse(dataBases);

        List<String> categories = new ArrayList<>();
        for (DataBase dataBase : dataBases) {
            switch (dataBase.type) {
                case "movie":
                    items.add(new MovieItem(dataBase.title, dataBase.director, dataBase.img_file,
                            dataBase.category));
                    break;
                case "series":
                    items.add(new SeriesItem(dataBase.title, dataBase.director, dataBase.img_file,
                            dataBase.number, dataBase.category));
                    break;
            }
            if (!categories.contains(dataBase.category)) {
                categories.add(dataBase.category);
            }
        }
        Collections.sort(items, new Comparator<ListItem>() {
            @Override
            public int compare(ListItem t0, ListItem t1) {
                return t0.getCategory().compareTo(t1.getCategory());
            }
        });

        for (String category : categories) {
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

    private void init_no_main() {
        Collections.sort(dataBases, new Comparator<DataBase>() {
            @Override
            public int compare(DataBase t0, DataBase t1) {
                return t0.title.compareTo(t1.title);
            }
        });
        for (DataBase dataBase : dataBases) {
            switch (dataBase.type) {
                case "movie":
                    items.add(new MovieItem(dataBase.title, dataBase.director, dataBase.img_file,
                            dataBase.category));
                    break;
                case "series":
                    items.add(new SeriesItem(dataBase.title, dataBase.director, dataBase.img_file,
                            dataBase.number, dataBase.category));
                    break;
            }
        }
    }

    public boolean canDropOver(int current, int target) {
        if (!items.get(current).category.equals(items.get(target).category)) {
            return false;
        }
        if (items.get(target).getType() == ListItem.TYPE_HEADER) {
            return false;
        }
        return true;
    }
}
