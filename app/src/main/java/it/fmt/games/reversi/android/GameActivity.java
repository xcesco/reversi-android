package it.fmt.games.reversi.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import it.fmt.games.reversi.android.ui.AppGridLayout;
import it.fmt.games.reversi.android.ui.GridViewItem;
import it.fmt.games.reversi.model.Board;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Player;


public class GameActivity extends AppCompatActivity implements GameRenderer, UserInputReader, View.OnClickListener {

    @BindView(R.id.blackNum)
    TextView blackNum;

    @BindView(R.id.whiteNum)
    TextView whiteNum;

    @BindView(R.id.gridLayout)
    AppGridLayout appGridLayout;
    private Coordinates coordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Reversi reversi = new Reversi(this, this, PlayerFactory.createUserPlayer1(), PlayerFactory.createRoboPlayer2());
        reversi.play();
    }

    @Override
    public void render(GameSnapshot gameSnapshot) {
        Log.i("RENDER", "draw");
        this.blackNum.setText(MessageFormat.format("BLACK: {0}", gameSnapshot.getScore().getPlayer1Score()));
        this.whiteNum.setText(MessageFormat.format("WHITE: {0}", gameSnapshot.getScore().getPlayer2Score()));

        gameSnapshot.getBoard().getCellStream().forEach(item -> {
            GridViewItem view = (GridViewItem) appGridLayout.getChildAt(item.getCoordinates().getRow() * Board.BOARD_SIZE + item.getCoordinates().getColumn());
            view.setOnClickListener(GameActivity.this);
            view.setTag(item.getCoordinates());
            switch (item.getPiece()) {
                case EMPTY:
                    view.setImageResource(R.drawable.transparent);
                    break;
                case PLAYER_1:
                    view.setImageResource(R.drawable.black_256);
                    break;
                case PLAYER_2:
                    view.setImageResource(R.drawable.white_256);
                    break;
            }
        });

    }


    @Override
    public Coordinates readInputFor(Player player, List<Coordinates> list) {
        try {
            while (coordinate == null || list.indexOf(coordinate)==-1) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return coordinate;
    }

    @Override
    public void onClick(View v) {
        coordinate = (Coordinates) v.getTag();

    }
}
