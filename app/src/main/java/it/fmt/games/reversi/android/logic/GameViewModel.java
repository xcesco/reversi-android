package it.fmt.games.reversi.android.logic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.android.GameActivity;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Player;

public class GameViewModel extends ViewModel {

    private Executor executor = Executors.newSingleThreadExecutor();

    private GameActivity activity;

    private GameRenderer uiRenderer;

    private final Move acceptedMove = new Move();

    public LiveData<GameSnapshot> play(final GameActivity activity, final Player1 player1, final Player2 player2, final GameRenderer gameRenderer) {
        MutableLiveData<GameSnapshot> result = new MutableLiveData<>();
        this.activity=activity;
        this.uiRenderer=gameRenderer;
        UserInputReader userInputReader = this::readPlayerMove;
        GameRenderer gamerRendererWrapper = this::dispatchToUiRenderer;

        executor.execute(() -> {
            Reversi reversi = new Reversi(gamerRendererWrapper, userInputReader, player1, player2);
            GameSnapshot finalGameSnapshot=reversi.play();

            result.postValue(finalGameSnapshot);
        });


        return result;
    }

    public Move getAcceptedMove() {
        return acceptedMove;
    }

    private void dispatchToUiRenderer(GameSnapshot gameSnapshot) {
        activity.runOnUiThread(() -> uiRenderer.render(gameSnapshot));
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
}
