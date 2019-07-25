package com.example.recycleview.RecycleAdapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public ItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            RecyclerView.State state) {
        outRect.left = space / 6;
        outRect.right = space / 6;
        outRect.bottom = space / 6;
        outRect.top = space / 6;
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1
                && parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(view))
                != ListItem.TYPE_SERIES) {
            outRect.bottom = 3 * space;
        }
        int my_postion = parent.getChildAdapterPosition(view);
        int size = parent.getAdapter().getItemCount();
        if (size >= 3 && my_postion == size - 1) {
            if (parent.getAdapter().getItemViewType(my_postion) == ListItem.TYPE_SERIES
                    && parent.getAdapter().getItemViewType(my_postion - 1)
                    != ListItem.TYPE_SERIES) {
                outRect.bottom = 3 * space;
            } else if (parent.getAdapter().getItemViewType(my_postion) == ListItem.TYPE_SERIES
                    && parent.getAdapter().getItemViewType(my_postion - 1) == ListItem.TYPE_SERIES
                    && parent.getAdapter().getItemViewType(my_postion - 2)
                    == ListItem.TYPE_SERIES) {
                outRect.bottom = 3 * space;
            }
        }
    }
}