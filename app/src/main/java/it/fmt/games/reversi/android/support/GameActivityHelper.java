package it.fmt.games.reversi.android.support;

import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.PlayerFactory;
import it.fmt.games.reversi.android.GameActivity;
import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.logic.AndroidDecisionHandler;

public class GameActivityHelper {
    public static void definePlayersAndPieces(GameActivity gameActivity) {
        gameActivity.setWhitePieceDrawable(gameActivity.getDrawable(R.drawable.white_256));
        gameActivity.setBlackPieceDrawable(gameActivity.getDrawable(R.drawable.black_256));
        gameActivity.setRotationAngle(-90f);

        Player1 player1;
        Player2 player2;

        String type = gameActivity.getIntent().getStringExtra(GameActivity.GAME_TYPE);
        switch (type) {
            case "1":
                player1=PlayerFactory.createHumanPlayer1();
                player2=PlayerFactory.createHumanPlayer2();
                break;
            case "2":
                player1=PlayerFactory.createHumanPlayer1();
                player2=PlayerFactory.createRoboPlayer2(new AndroidDecisionHandler());
                break;
            case "3":
                player1=PlayerFactory.createRoboPlayer1(new AndroidDecisionHandler());
                player2=PlayerFactory.createHumanPlayer2();
                break;
            case "4":
            default:
                player1=PlayerFactory.createRoboPlayer1(new AndroidDecisionHandler());
                player2=PlayerFactory.createRoboPlayer2(new AndroidDecisionHandler());
                break;
        }

        gameActivity.setPlayer1(player1);
        gameActivity.setPlayer2(player2);

        if (!player1.isHumanPlayer()) gameActivity.getTvPlayer1Title().setText("CPU 1");
        if (!player2.isHumanPlayer()) gameActivity.getTvPlayer2Title().setText("CPU 2");
    }
}
