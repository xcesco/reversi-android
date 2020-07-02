package it.fmt.games.reversi.android.repositories.persistence.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlayedMatchDao {
  @Query("SELECT * FROM played_matches ORDER BY date DESC")
  LiveData<List<PlayedMatch>> getAll();

  @Insert
  long insert(PlayedMatch match);

  @Delete
  void delete(PlayedMatch match);
}