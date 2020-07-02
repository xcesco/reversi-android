package it.fmt.games.reversi.android.repositories.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.fmt.games.reversi.android.ReversiApplication;
import it.fmt.games.reversi.android.repositories.persistence.database.AppDatabase;
import it.fmt.games.reversi.android.repositories.persistence.database.PlayedMatch;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Piece;
import it.fmt.games.reversi.model.Score;
import timber.log.Timber;

@Singleton
public class PlayedMatchRepository {

  private final AppDatabase dataSource;

  @Inject
  public PlayedMatchRepository() {
    dataSource = Room.databaseBuilder(ReversiApplication.getContext(),
            AppDatabase.class, "played_matches.db")
            .build();

  }

  public LiveData<List<PlayedMatch>> getAll() {
    return dataSource.userDao().getAll();
  }

  public void insert(String player1Name, String player2Name, GameStatus gameStatus, @NotNull Score score, Piece assignedPiece) {
    PlayedMatch match = new PlayedMatch();
    match.player1 = player1Name;
    match.player2 = player2Name;
    match.gameStatus = gameStatus;
    match.date = new Date();
    match.player1Score = score.getPlayer1Score();
    match.player2Score = score.getPlayer2Score();
    match.winner = (assignedPiece == Piece.PLAYER_1 && gameStatus == GameStatus.PLAYER1_WIN) || (assignedPiece == Piece.PLAYER_2 && gameStatus == GameStatus.PLAYER2_WIN);
    long id = dataSource.userDao().insert(match);
    Timber.i("match inserted with id %s: ", id);
  }
}
