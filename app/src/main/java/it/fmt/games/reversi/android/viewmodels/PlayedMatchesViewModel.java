package it.fmt.games.reversi.android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import it.fmt.games.reversi.android.ReversiApplication;
import it.fmt.games.reversi.android.repositories.persistence.PlayedMatchRepository;
import it.fmt.games.reversi.android.repositories.persistence.database.PlayedMatch;

public class PlayedMatchesViewModel extends ViewModel {
  @Inject
  PlayedMatchRepository playedMatchRepository;

  public PlayedMatchesViewModel() {
    ReversiApplication.getInjector().inject(this);
  }

  public LiveData<List<PlayedMatch>> getAll() {
    return playedMatchRepository.getAll();
  }

  public void deleteAll() {
    playedMatchRepository.deleteAll();
  }
}
