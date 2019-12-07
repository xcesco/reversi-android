package it.fmt.games.reversi.core;

public class Score {
    private int player1Scores = 0;
    private int player2Scores = 0;

    public Score(int player1Scores, int player2Scores) {
        this.player1Scores=player1Scores;
        this.player2Scores=player2Scores;
    }

    public int getPlayer1Scores() {
        return player1Scores;
    }

    public int getPlayer2Scores() {
        return player2Scores;
    }

}
