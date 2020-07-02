package it.fmt.games.reversi.android.repositories.persistence.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {PlayedMatch.class},
        version = 1)
@TypeConverters(GameStatusConverter.class)
public abstract class AppDatabase extends RoomDatabase {
  public abstract PlayedMatchDao userDao();
}