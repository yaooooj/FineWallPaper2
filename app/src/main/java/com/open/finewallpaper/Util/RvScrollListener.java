package com.open.finewallpaper.Util;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.bumptech.glide.Glide;

/**
 * Created by yaojian on 2017/10/31.
 */

public abstract class RvScrollListener extends RecyclerView.OnScrollListener {
    private static final  String TAG = "RvScrollListener";
    private int firstVisibleItem;
    private int lastVisibleItem;
    private int span = 0;
    public RvScrollListener() {
        super();

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int count = adapter.getItemCount();
        Log.e(TAG, "onScrollStateChanged: "  + visibleItemCount +" + " + totalItemCount + "  + " + lastVisibleItem);
        switch (newState){
            case RecyclerView.SCROLL_STATE_IDLE:
                onLoadMore();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                if (span != 0){
                    if (totalItemCount - lastVisibleItem <= span){
                        onDragLoadMore();
                    }
                }else {
                    onDragLoadMore();
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                break;
            default:
                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

            } else if (layoutManager instanceof GridLayoutManager) {
                firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                span = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
                int[] positions = new int[span];
                // ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions);
                firstVisibleItem = getFirstPosition(((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions));

                lastVisibleItem = getLastPosition(((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(positions));
            }
        }
    }

    private int getFirstPosition(int[] position){
        int first = position[0];
        for (int value : position){
            if (value < first){
                first = value;
            }
        }
        return first;
    }
    private int getLastPosition(int[] position){
        int last = position[0];
        for (int value : position){
            if (value > last){
                last = value;
            }
        }
        return last;
    }

    public abstract void onLoadMore();

    public abstract void onDragLoadMore();
}