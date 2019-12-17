package it.fmt.games.reversi.android.logic;

import java.util.List;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.android.GameActivity;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Player;

public class GameLogicThread extends Thread {
    private final Move acceptedMove = new Move();

    private final Reversi reversi;

    private final GameActivity activity;

    private GameRenderer uiRenderer;

    public GameLogicThread(GameActivity activity, Player1 player1, Player2 player2, GameRenderer uiRenderer) {
        this.activity = activity;
        this.uiRenderer = uiRenderer;
        UserInputReader userInputReader = this::readPlayerMove;
        GameRenderer gamerRendererWrapper = this::dispatchToUiRenderer;
        this.reversi = new Reversi(gamerRendererWrapper, userInputReader, player1, player2);
    }

    public Move getAcceptedMove() {
        return acceptedMove;
    }

    private void dispatchToUiRenderer(GameSnapshot gameSnapshot) {
        activity.runOnUiThread(() -> {
            uiRenderer.render(gameSnapshot);
        });
    }

    private Coordinates readPlayerMove(Player player, List<Coordinates> list) {
        synchronized (acceptedMove) {
            try {
                while (isInvalidMove(list)) {
                    acceptedMove.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return acceptedMove.getCoordinates();
        }
    }

    private boolean isInvalidMove(List<Coordinates> list) {
        return acceptedMove.getCoordinates() == null || list.indexOf(acceptedMove.getCoordinates()) == -1;
    }

    @Override
    public void run() {
        reversi.play();
    }
}