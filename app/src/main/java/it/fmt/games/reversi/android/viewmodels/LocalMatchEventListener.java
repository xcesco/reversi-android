package it.fmt.games.reversi.android.viewmodels;

import androidx.lifecycle.MutableLiveData;

import it.fmt.games.reversi.android.repositories.network.MatchEventListener;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.model.Coordinates;

public class LocalMatchEventListener implements MatchEventListener {
  private final MutableLiveData<MatchMessage> result;

  LocalMatchEventListener(MutableLiveData<MatchMessage> result) {
    this.result = result;
  }

  @Override
  public void onMatchStart(MatchStartMessage event) {
    result.postValue(event);
  }


  @Override
  public Coordinates onMatchPlayerMove(MatchStatusMessage event) {
    result.postValue(event);

    return null;
  }

  @Override
  public void onMatchEnd(MatchEndMessage event) {
    result.postValue(event);
  }

}