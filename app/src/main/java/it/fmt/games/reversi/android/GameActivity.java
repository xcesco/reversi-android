package it.fmt.games.reversi.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.MessageFormat;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.PlayerFactory;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Player;


public class GameActivity extends AppCompatActivity implements GameRenderer, UserInputReader {

    @BindView(R.id.blackNum)
    TextView blackNum;

    @BindView(R.id.whiteNum)
    TextView whiteNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Reversi reversi = new Reversi(this, this, PlayerFactory.createRoboPlayer1(), PlayerFactory.createRoboPlayer2());

        reversi.play();
    }

    @Override
    public void render(GameSnapshot gameSnapshot) {
        Log.i("RENDER", "draw");
        this.blackNum.setText(MessageFormat.format("BLACK: {0}", gameSnapshot.getScore().getPlayer1Score()));
        this.whiteNum.setText(MessageFormat.format("WHITE: {0}", gameSnapshot.getScore().getPlayer2Score()));

        gameSnapshot.getBoard().getCellStream().forEach(item -> {
            //item.get
        });
    }


    @Override
    public Coordinates readInputFor(Player player, List<Coordinates> list) {
        return list.get(0);
    }
}
