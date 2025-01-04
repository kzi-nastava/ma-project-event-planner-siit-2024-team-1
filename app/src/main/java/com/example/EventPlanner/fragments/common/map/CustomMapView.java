package com.example.EventPlanner.fragments.common.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import org.osmdroid.views.MapView;

public class CustomMapView extends MapView {
    private int touchCount = 0;

    public CustomMapView(Context context) {
        super(context);
    }

    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchCount++;
                // Always disallow ScrollView to intercept touch events when interacting with map
                getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                touchCount++;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                touchCount--;
                if (touchCount == 0) {
                    // Only allow ScrollView to intercept once all touches are finished
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                touchCount = 0;
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}