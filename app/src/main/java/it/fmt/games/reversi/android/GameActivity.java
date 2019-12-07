package it.fmt.games.reversi.android;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.GridView;

public class GameActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // this.gridView = (GridView) findViewById(R.id.chessboard_gridview);
        //ChessboardViewAdapter adapter = new ChessboardViewAdapter(this, 8, 8);
        //gridView.setAdapter(adapter);
       // gridView.set
    }

}
