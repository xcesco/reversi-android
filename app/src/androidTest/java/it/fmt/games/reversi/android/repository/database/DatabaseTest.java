package it.fmt.games.reversi.android.repository.database;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import it.fmt.games.reversi.android.ReversiApplication;
import it.fmt.games.reversi.android.repositories.persistence.AppDatabase;
import it.fmt.games.reversi.android.repositories.persistence.PlayedMatch;
import it.fmt.games.reversi.model.GameStatus;
import timber.log.Timber;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
  @Test
  public void useAppContext() {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    assertEquals("it.fmt.games.reversi.android", appContext.getPackageName());

    AppDatabase dataSource = Room.databaseBuilder(ReversiApplication.getContext(),
            AppDatabase.class, "played_matches.db")
            .build();

    PlayedMatch match = new PlayedMatch();
    match.player1 = "player1";
    match.player2 = "player1";
    match.gameStatus = GameStatus.PLAYER1_WIN;
    match.date = new Date();
    match.player1Score = 20;
    match.player2Score = 1;
    match.winner = true;

    long id = dataSource.userDao().insert(match);
    Timber.i("match inserted with id %s ", id);
    dataSource.close();
  }
}
