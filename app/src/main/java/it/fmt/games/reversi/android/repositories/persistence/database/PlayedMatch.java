package it.fmt.games.reversi.android.repositories.persistence.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import it.fmt.games.reversi.model.GameStatus;

@Entity(tableName = "played_matches")
public class PlayedMatch {
  @PrimaryKey(autoGenerate = true)
  public int id;

  public String player1;

  public String player2;

  public GameStatus gameStatus;

  public int player1Score;

  public int player2Score;

  public Date date;

  public boolean winner;

}