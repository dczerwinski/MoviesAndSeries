package com.example.recycleview.RecycleAdapters


import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG
import java.util.Collections

class MyItemTouchHelperCallback(var recyclerAdapter: RecyclerAdapter) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {

        if(recyclerView.adapter!!.getItemViewType(viewHolder.adapterPosition) == ListItem.TYPE_HEADER){
            recyclerAdapter.hideAllItemsWithCat(recyclerAdapter.items.get(viewHolder.adapterPosition).category)
        }
        var dragFlags:Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT


        /*
        var dragFlags: Int = (if (recyclerView.adapter!!.getItemViewType(viewHolder.adapterPosition) == ListItem.TYPE_HEADER) 0
        else ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) as Int
*/
        var swipeFlags = 0

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ACTION_STATE_DRAG) {
            viewHolder!!.itemView.alpha = 0.5f
            viewHolder.itemView.bringToFront()
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        dragged: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {


        Collections.swap(recyclerAdapter.items, dragged.adapterPosition, target.adapterPosition)
        recyclerView.adapter!!.notifyItemMoved(dragged.adapterPosition, target.adapterPosition)

        return true
    }



    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.alpha = 1.0f
        recyclerView.post(recyclerView.adapter!!::notifyDataSetChanged)
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
        return
    }

    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        recyclerAdapter.hideMenu()
        return recyclerAdapter.canDropOver(current.adapterPosition, target.adapterPosition)
    }
}