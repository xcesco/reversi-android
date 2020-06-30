package it.fmt.games.reversi.android.viewmodels;

import androidx.lifecycle.MutableLiveData;

import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.android.repositories.network.MatchEventListener;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;

public class LocalMatchEventListener implements MatchEventListener {
  private final MutableLiveData<MatchMessage> result;
  private final UserInputReader userInputReader;
  private final Player1 player1;
  private final Player2 player2;

  LocalMatchEventListener(MutableLiveData<MatchMessage> result, Player1 player1, Player2 player2, UserInputReader userInputReader) {
    this.result = result;
    this.userInputReader = userInputReader;
    this.player1 = player1;
    this.player2 = player2;
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