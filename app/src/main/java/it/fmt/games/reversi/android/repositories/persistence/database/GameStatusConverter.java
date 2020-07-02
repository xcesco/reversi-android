package it.fmt.games.reversi.android.repositories.persistence.database;

import androidx.room.TypeConverter;

import java.util.Date;

import it.fmt.games.reversi.model.GameStatus;

public class GameStatusConverter {
  @TypeConverter
  public static GameStatus fromInt(int value) {
    return GameStatus.values()[value];
  }

  @TypeConverter
  public static int gameStatusToInt(GameStatus value) {
    return value.ordinal();
  }

  @TypeConverter
  public static Date fromLong(long value) {
    return new Date(value);
  }

  @TypeConverter
  public static long dateToLong(Date value) {
    return value.getTime();
  }
}
