package it.fmt.games.reversi.android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.model.GameSnapshot;


public class GameActivity extends AppCompatActivity implements GameRenderer, UserInputReader {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Reversi reversi = new Reversi(this, this, null, null);

        reversi.play();
    }

    @Override
    public void render(GameSnapshot gameSnapshot) {

    }
}
