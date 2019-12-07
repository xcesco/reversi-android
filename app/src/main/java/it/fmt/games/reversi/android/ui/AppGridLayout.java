package it.fmt.games.reversi.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;

public class AppGridLayout extends GridLayout {
    public AppGridLayout(Context context) {
        super(context);
    }

    public AppGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppGridLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);


    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int measure = Math.max(widthSpec, heightSpec);
        //super.onMeasure(widthSpec, heightSpec);
        super.onMeasure(measure, measure);
    }
}
