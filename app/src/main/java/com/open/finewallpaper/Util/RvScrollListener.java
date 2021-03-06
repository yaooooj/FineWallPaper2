package com.open.finewallpaper.Util;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * Created by yaojian on 2017/10/31.
 */

public abstract class RvScrollListener extends RecyclerView.OnScrollListener {
    private static final  String TAG = "RvScrollListener";
    private int firstVisibleItem;
    private int lastVisibleItem;
    private int span = 0;
    private AppBarLayout appbar;

    public RvScrollListener(AppBarLayout appBarLayout) {
        super();
        appbar = appBarLayout;

    }

    public RvScrollListener(){

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int count = adapter.getItemCount();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

       // Log.e(TAG, "onScrollStateChanged: "  + visibleItemCount +" + " + totalItemCount + "  + " + lastVisibleItem);
        switch (newState){
            case RecyclerView.SCROLL_STATE_IDLE:

                if (firstVisibleItem == 0 && appbar != null)  {
                    appbar.setExpanded(true, true);
                }
                GlideApp.with(recyclerView.getContext()).resumeRequests();
                if (lastVisibleItem  == layoutManager.getItemCount() - 1 && (count > 1)){

                    onLoadMore(recyclerView);
                }

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
                Log.e(TAG, "onScrollStateChanged: " + " on fling " );
                break;
            default:
                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int totalItemCount = 0;
        int count = adapter.getItemCount();
        int spanCount = 0;
        if (layoutManager != null) {
            totalItemCount = layoutManager.getItemCount();

            if (layoutManager instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

            }

            if (layoutManager instanceof StaggeredGridLayoutManager) {
                span = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
                int[] positions = new int[span];
                // ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions);
                firstVisibleItem = getFirstPosition(((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions));
                lastVisibleItem = getLastPosition(((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(positions));
            }


            if (layoutManager instanceof GridLayoutManager) {
                spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
                firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

        }

        Log.e(TAG, "onScroll: "  + " + " + totalItemCount + "  + " + lastVisibleItem);

        if (layoutManager != null && lastVisibleItem == layoutManager.getItemCount() - 1  && (count > 1)) {
            Log.e(TAG, "onScrolled: " + "execute" );
            onLoadMore(recyclerView);
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

    public abstract void onLoadMore(View view);

    public abstract void onDragLoadMore();
}
