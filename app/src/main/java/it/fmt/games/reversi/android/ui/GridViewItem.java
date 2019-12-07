package it.fmt.games.reversi.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageButton;

import java.util.logging.Logger;

public class GridViewItem extends AppCompatImageButton {

    public GridViewItem(Context context) {
        super(context);
    }

    public GridViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //final int gridwidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        //int measure=Math.min(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(gridwidth, gridwidth);
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Log.d("ss", String.format("w %s, h %s", widthMeasureSpec,heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //super.onMeasure(heightMeasureSpec, heightMeasureSpec);

//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = getMeasuredWidth() > getMeasuredHeight() ? getMeasuredHeight() : getMeasuredWidth();
        setMeasuredDimension(size, size);
        //setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

//    @Override
//    protected void onSizeChanged(final int width, final int height, final int oldwidth, final int oldheight) {
//        super.onSizeChanged(width, width, oldwidth, oldheight);
//    }
}