package it.fmt.games.reversi.core;

public class ScoreCalculator {

    private final Board board;

    public ScoreCalculator(Board board) {
        this.board=board;
    }

    public Score execute() {
        int[] score=new int[Piece.values().length];
        board.getCellStream().forEach(cell -> score[cell.getPiece().ordinal()]++);
        return new Score(score[Piece.PLAYER_1.ordinal()],score[Piece.PLAYER_2.ordinal()]);
    }


}
