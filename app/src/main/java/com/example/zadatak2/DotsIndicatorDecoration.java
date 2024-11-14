package com.example.zadatak2;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DotsIndicatorDecoration extends RecyclerView.ItemDecoration {

    private final int lineLength;
    private final int indicatorHeight;
    private final int indicatorItemPadding;
    private final int radius;
    private final Paint inactivePaint;
    private final Paint activePaint;

    public DotsIndicatorDecoration(@ColorInt int colorInactive, @ColorInt int colorActive) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        lineLength = (int) (40 * displayMetrics.density + 0.5f);
        indicatorHeight = (int) (20 * displayMetrics.density + 0.5f);
        indicatorItemPadding = (int) (4 * displayMetrics.density + 0.5f);
        radius = (int) (2 * displayMetrics.density + 0.5f);

        inactivePaint = new Paint();
        inactivePaint.setStyle(Paint.Style.FILL);
        inactivePaint.setAntiAlias(true);
        inactivePaint.setColor(colorInactive);

        activePaint = new Paint();
        activePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        activePaint.setStrokeCap(Paint.Cap.ROUND);
        activePaint.setStrokeWidth(radius * 2.0f);
        activePaint.setAntiAlias(true);
        activePaint.setColor(colorActive);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        int itemCount = adapter.getItemCount();

        float totalLength = (this.radius * 2 * itemCount);
        float paddingBetweenItems = (0 < itemCount - 1 ? itemCount - 1 : 0) * indicatorItemPadding;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;
        float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2f;

        float indicatorPosY = parent.getHeight() - indicatorHeight / 2f;

        int firstVisiblePosition = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            firstVisiblePosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else {
            // not supported layout manager
            return;
        }

        if (firstVisiblePosition == RecyclerView.NO_POSITION) {
            return;
        }

        drawInactiveDots(c, indicatorStartX, indicatorPosY, itemCount, firstVisiblePosition);
        drawActiveLine(c, indicatorStartX, indicatorPosY, firstVisiblePosition);
    }

    private void drawInactiveDots(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount, int activeIndex) {
        float itemWidth = (this.radius * 2 + indicatorItemPadding);
        float start = indicatorStartX + radius;
        for (int i = 0; i < itemCount; i++) {
            if (i == activeIndex) {
                start += lineLength / 2;
                continue;
            }
            c.drawCircle(start, indicatorPosY, radius, inactivePaint);
            start += itemWidth;
        }
    }

    private void drawActiveLine(Canvas c, float indicatorStartX, float indicatorPosY, int highlightPosition) {
        float itemWidth = (lineLength + indicatorItemPadding) / 2f;
        float circleWidth = (radius * 2) + indicatorItemPadding;
        float highlightStart = indicatorStartX + circleWidth * highlightPosition + radius;
        c.drawLine(
                highlightStart,
                indicatorPosY,
                highlightStart + itemWidth - indicatorItemPadding / 2,
                indicatorPosY,
                activePaint
        );
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = indicatorHeight;
    }
}
