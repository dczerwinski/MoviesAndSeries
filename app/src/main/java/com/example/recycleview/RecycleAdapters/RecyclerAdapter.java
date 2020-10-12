package com.example.recycleview.RecycleAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.recycleview.CategoryActivity;
import com.example.recycleview.DataBase.AppDatabase;
import com.example.recycleview.DataBase.DataBase;
import com.example.recycleview.EditItemActivity;
import com.example.recycleview.MovieActivity;
import com.example.recycleview.R;
import com.example.recycleview.SeriesActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView mTextView;
    ImageView mImageView;
    View mView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.txtDescription);
        mView = itemView;
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
    private PopupWindow popupWindow;

    public void hideMenu(){
        if(popupWindow !=null) popupWindow.dismiss();
    }

    public void setmItemTouchHelper(ItemTouchHelper mItemTouchHelper) {
        this.mItemTouchHelper = mItemTouchHelper;
    }

    public RecyclerAdapter(Context context) {
        popupWindow = null;
        mItemTouchHelper=null;
        dataBases = AppDatabase.getAppDatabase(context).dataBaseDao().getAll();
        this.context = context;
        init();
    }


    public RecyclerAdapter(List<DataBase> dataBaseList, Context context) {
        popupWindow = null;
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
                        inflater.inflate(R.layout.layout_item_recycler_view_category, parent, false));
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



        mRecyclerViewHolder.mView.setOnLongClickListener(view -> {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.popup_layout,null);
            popupView.setAlpha(1f);
            popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            Button mButtonEdit =  popupView.findViewById(R.id.btn_Edit);
            Button mButtonDelete = popupView.findViewById(R.id.btn_delete);

            mButtonEdit.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), EditItemActivity.class);

                switch (getItemViewType(position)){
                    case ListItem.TYPE_HEADER:
                        intent.putExtra("edit",AppDatabase.getAppDatabase(context).dataBaseDao().
                                findByName(((List<HeaderItem>) (Object)items).get(position).getCategory()).uid);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        v.getContext().startActivity(intent);
                        v.postDelayed(() -> popupWindow.dismiss(),300);
                        break;
                    case ListItem.TYPE_MOVIE:
                        intent.putExtra("edit",AppDatabase.getAppDatabase(context).dataBaseDao().
                                findByName(((List<MovieItem>) (Object)items).get(position).getTitle()).uid);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        v.getContext().startActivity(intent);
                        v.postDelayed(() -> popupWindow.dismiss(),300);
                        break;
                    case ListItem.TYPE_SERIES:
                        intent.putExtra("edit",AppDatabase.getAppDatabase(context).dataBaseDao().
                                findByName(((List<SeriesItem>) (Object)items).get(position).getTitle()).uid);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        v.getContext().startActivity(intent);
                        v.postDelayed(() -> popupWindow.dismiss(),300);
                        break;
               }



            });

            mButtonDelete.setOnClickListener(v -> {
                String temp_catygory;
                temp_catygory = items.get(position).category;
                switch (getItemViewType(position)) {
                    case ListItem.TYPE_MOVIE:
                        AppDatabase.getAppDatabase(context).dataBaseDao()
                                .deleteByTitle(((List<MovieItem>) (Object) items).get(position)
                                        .getTitle());
                        items.remove(position);
                        break;
                    case ListItem.TYPE_SERIES:
                        AppDatabase.getAppDatabase(context).dataBaseDao()
                                .deleteByTitle(((List<SeriesItem>) (Object) items).get(position)
                                        .getTitle());

                        items.remove(position);
                        break;
                    case ListItem.TYPE_HEADER:
                        AppDatabase.getAppDatabase(context).dataBaseDao()
                                .deleteCatygory(((List<HeaderItem>) (Object) items).get(position).getCategory());

                        final String temp = items.get(position).category;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            items.removeIf(x -> x.category == temp);
                        }
                        break;
                }
                int temp = 0;
                for(ListItem item: items){
                    if(item.category.equals(temp_catygory))temp++;
                }
                if(temp == 1){
                    for(Iterator iterator = items.iterator(); iterator.hasNext();){
                        ListItem item = (ListItem) iterator.next();
                        if(item.category.equals(temp_catygory)){
                            iterator.remove();
                        }
                    }
                }

                v.postDelayed(() -> popupWindow.dismiss(),300);

                v.post(() -> {
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    Toast.makeText(context,"Item deleted!",Toast.LENGTH_SHORT).show();
                });
            });

            int[] location = new int[2];
            mRecyclerViewHolder.mView.getLocationInWindow(location);


            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


            if(getItemViewType(position) != ListItem.TYPE_SERIES) popupWindow.showAtLocation(view, Gravity.NO_GRAVITY,displayMetrics.widthPixels/4 ,location[1]-150);
            else popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (int) view.getX(),location[1]-150);

            if(mItemTouchHelper!=null) mItemTouchHelper.startDrag(mRecyclerViewHolder);
            return true;
        });

        mRecyclerViewHolder.mView.setOnClickListener(v -> {
            Intent intent;
            switch (getItemViewType(position)) {
                case ListItem.TYPE_MOVIE:
                    intent = new Intent(v.getContext(), MovieActivity.class);
                    intent.putExtra("elo", AppDatabase.getAppDatabase(context).dataBaseDao()
                            .findByName(((List<MovieItem>) (Object) items).get(position)
                                    .getTitle()).uid);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    v.getContext().startActivity(intent);
                    break;
                case ListItem.TYPE_SERIES:
                    intent = new Intent(v.getContext(), SeriesActivity.class);
                    intent.putExtra("elo", AppDatabase.getAppDatabase(context).dataBaseDao()
                            .findByName(((List<SeriesItem>) (Object) items).get(position)
                                    .getTitle()).uid);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    v.getContext().startActivity(intent);
                    break;
                case ListItem.TYPE_HEADER:
                    intent = new Intent(v.getContext(), CategoryActivity.class);
                    intent.putExtra("elo",
                            ((List<HeaderItem>) (Object) items).get(position).getCategory());
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    v.getContext().startActivity(intent);
                    break;
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
        Collections.sort(dataBases, (t0, t1) -> t0.title.compareTo(t1.title));

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
        Collections.sort(items, (t0, t1) -> t0.getCategory().compareTo(t1.getCategory()));

        for (String category : categories) {
            items.add(new HeaderItem(category));
        }
        Collections.reverse(items);
        Collections.sort(items, (t0, t1) -> t0.getCategory().compareTo(t1.getCategory()));
    }

    private void init_no_main() {
        Collections.sort(dataBases, (t0, t1) -> t0.title.compareTo(t1.title));
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
        if(items.get(current).getType() == items.get(target).getType() && items.get(current).getType() == ListItem.TYPE_HEADER){
            return true;
        }
        if (!items.get(current).category.equals(items.get(target).category)) {
            return false;
        }
        if (items.get(target).getType() == ListItem.TYPE_HEADER) {
            return false;
        }
        if(items.get(current).category.equals(items.get(target).category) && items.get(current).getType() == ListItem.TYPE_HEADER){
            return false;
        }
        return true;
    }

    public void hideAllItemsWithCat(String cat){
        for(int i=0; i<getItemCount(); i++){
            if(items.get(i).category.equals(cat) && getItemViewType(i) != ListItem.TYPE_HEADER){
                //notifyItemRemoved(i);
            }
        }
    }
}
