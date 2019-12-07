package it.fmt.games.reversi.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class ChessboardViewAdapter extends BaseAdapter {

    private final Context context;
    private final int columns;
    private final int rows;

    public ChessboardViewAdapter(Context context, int rows, int columns) {
        this.context=context;
        this.columns=columns;
        this.rows=rows;
    }

    @Override
    public int getCount() {
        return columns*rows;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        int row=position / columns;
        int column=position% columns;
        View view = convertView;
        if(view == null) {
            view = new SquareView(context, row, column);
        }

        // Adjust each square's size to look like a square
        int parentPadding = 2 * parent.getPaddingTop(); // equal paddings
        int newSize1 = (parent.getWidth() - parentPadding) / this.columns;
        int newSize2 = (parent.getHeight() - parentPadding) / this.rows;
        int newSize=Math.min(newSize1, newSize2);
        view.setLayoutParams(new GridView.LayoutParams(newSize, newSize));
      //  view.setTop(row*newSize);
       // view.setLeft(column*newSize);
        view.setPadding(0, 0, 0, 0); // hard coded offsets for the black border

        return view;
    }
}