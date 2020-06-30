package it.fmt.games.reversi.android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.android.ReversiApplication;
import it.fmt.games.reversi.android.repositories.network.MatchEventListener;
import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.repositories.network.model.UserRegistration;
import it.fmt.games.reversi.android.ui.activities.GameActivity;
import it.fmt.games.reversi.android.ui.support.GameActivityHelper;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;

public class NetworkMatchViewModel extends AbstractMatchViewModel {

  @Inject
  NetworkClient client;

  public NetworkMatchViewModel() {
    ReversiApplication.getInjector().inject(this);
  }

  @Override
  public void match(final GameActivity activity, GameType gameType) {
    //if (player1 instanceof Network)
    MutableLiveData<MatchMessage> result = new MutableLiveData<>();
    UserInputReader userInputReader = this::readPlayerMove;
    final ConnectedUser user = client.connect(UserRegistration.of("player2"));
    NetworkMatchEventListener listener = new NetworkMatchEventListener(activity, result, userInputReader);
    client.match(user, listener);
  }

  static class NetworkMatchEventListener implements MatchEventListener {
    private final GameActivity activity;
    private final MutableLiveData<MatchMessage> result;
    private final UserInputReader userInputReader;

    public NetworkMatchEventListener(GameActivity activity, MutableLiveData<MatchMessage> result, UserInputReader userInputReader) {
      this.activity = activity;
      this.result = result;
      this.userInputReader = userInputReader;
    }

    @Override
    public void onMatchStart(MatchStartMessage event) {
      result.postValue(event);
    }

    @Override
    public Coordinates onMatchPlayerMove(MatchStatusMessage event) {
      Coordinates move = userInputReader.readInputFor(null,
              event.getGameSnapshot().getAvailableMoves().getMovesActivePlayer());
      result.postValue(event);
      return move;
    }

    @Override
    public void onMatchEnd(MatchEndMessage event) {
      result.postValue(event);
    }
  }
}
