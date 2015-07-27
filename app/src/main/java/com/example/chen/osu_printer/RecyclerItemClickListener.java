package com.example.chen.osu_printer;

/**
 * Created by chen on 15/7/14.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
        public void onItemSwipe(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView view, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView = view.findChildViewUnder(e.getX(), e.getY());
                if(childView != null && mListener != null)
                {
                    mListener.onItemClick(childView, view.getChildPosition(childView));

                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e)
            {
                View childView = view.findChildViewUnder(e.getX(), e.getY());

                if(childView != null && mListener != null)
                {
                    mListener.onItemLongClick(childView, view.getChildPosition(childView));
                }
            }

            @Override
            public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY)
            {
                if (event1 != null && event2 != null) {     //for handling the unexpected error in loginActivity when swipe down

                    View childView1 = view.findChildViewUnder(event1.getX(), event1.getY());
                    View childView2 = view.findChildViewUnder(event2.getX(), event2.getY());

                    if (event1.getX() > event2.getX() + 120 && childView1 == childView2 && childView1 != null && mListener != null) {

                        mListener.onItemSwipe(childView1, view.getChildPosition(childView1));
                    }
                    return true;
                } return false;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }
}