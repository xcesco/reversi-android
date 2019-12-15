package it.fmt.games.reversi.android;

import it.fmt.games.reversi.PlayerFactory;
import it.fmt.games.reversi.android.logic.AndroidPlayer;

public class GameActivityHelper {
    public static void definePlayersAndPieces(GameActivity gameActivity) {
        gameActivity.whitePieceDrawable = gameActivity.getDrawable(R.drawable.white_256);
        gameActivity.blackPieceDrawable = gameActivity.getDrawable(R.drawable.black_256);
        gameActivity.rotationAngle = -90f;

        String type = gameActivity.getIntent().getStringExtra(GameActivity.GAME_TYPE);
        switch (type) {
            case "1":
                gameActivity.player1 = PlayerFactory.createUserPlayer1();
                gameActivity.player2 = PlayerFactory.createUserPlayer2();
                break;
            case "2":
                gameActivity.player1 = PlayerFactory.createUserPlayer1();
                gameActivity.player2 = PlayerFactory.createRoboPlayer2(new AndroidPlayer());
                break;
            case "3":
                gameActivity.player1 = PlayerFactory.createRoboPlayer1(new AndroidPlayer());
                gameActivity.player2 = PlayerFactory.createUserPlayer2();
                break;
            case "4":
                gameActivity.player1 = PlayerFactory.createRoboPlayer1(new AndroidPlayer());
                gameActivity.player2 = PlayerFactory.createRoboPlayer2(new AndroidPlayer());
                break;
        }
        if (!gameActivity.player1.isHumanPlayer()) gameActivity.tvPlayer1Title.setText("CPU 1");
        if (!gameActivity.player2.isHumanPlayer()) gameActivity.tvPlayer2Title.setText("CPU 2");
    }
}