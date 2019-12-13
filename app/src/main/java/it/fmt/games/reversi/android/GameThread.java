package it.fmt.games.reversi.android;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.MainThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Player;

class GameThread extends Thread {
    private final GameActivity main;
    private final Reversi reversi;
    private Handler handler;
    private GameActivity activity;

    public Move acceptedMove = new Move();
    public List<Coordinates> availableMoves = new ArrayList<>();

    private UserInputReader userInputReader;

    public GameThread(GameActivity mainClass, Player1 player1, Player2 player2, GameRenderer renderer) {
        this.main = mainClass;
        this.userInputReader = (player, list) -> {
            availableMoves = list;

            synchronized (acceptedMove) {
                while (acceptedMove.getCoordinates() == null || availableMoves.indexOf(acceptedMove.getCoordinates()) == -1) {
                    try {
                        acceptedMove.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return acceptedMove.getCoordinates();
            }
        };

        this.reversi = new Reversi(new GameRenderer() {
            @Override
            public void render(GameSnapshot gameSnapshot) {
                //try {
                   // Thread.sleep(1000);
                    mainClass.runOnUiThread(() -> {
                        renderer.render(gameSnapshot);
                    });
              //  } catch (InterruptedException e) {
                //    e.printStackTrace();
                //}
            }
        }, userInputReader, player1, player2);
    }

    @Override
    public void run() {
        //Looper.prepare();

        /*handler = new Handler() {
            public void handleMessage(Message msg) {
                // inputReader.
            }
        };*/
        GameSnapshot result = reversi.play();

        //Looper.loop();
    }
}