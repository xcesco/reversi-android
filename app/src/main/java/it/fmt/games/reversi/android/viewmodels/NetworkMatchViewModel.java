package it.fmt.games.reversi.android.viewmodels;

import javax.inject.Inject;

import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.android.BuildConfig;
import it.fmt.games.reversi.android.ReversiApplication;
import it.fmt.games.reversi.android.exceptions.ReversiRuntimeException;
import it.fmt.games.reversi.android.repositories.network.MatchEventListener;
import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.repositories.network.model.ErrorStatus;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.repositories.network.model.UserRegistration;
import it.fmt.games.reversi.android.repositories.persistence.PlayedMatchRepository;
import it.fmt.games.reversi.android.ui.support.CpuType;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.android.repositories.model.MatchEventDispatcher;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Piece;
import it.fmt.games.reversi.model.cpu.RandomDecisionHandler;
import timber.log.Timber;

public class NetworkMatchViewModel extends AbstractMatchViewModel {
  @Inject
  PlayedMatchRepository playedMatchRepository;

  @Inject
  NetworkClient client;

  public NetworkMatchViewModel() {
    ReversiApplication.getInjector().inject(this);
  }

  @Override
  public void match(String playerName, GameType gameType, CpuType cpuType) {
    executor.execute(() -> {
      try {
        final ConnectedUser user = client.connect(UserRegistration.of(playerName), NetworkMatchViewModel.this);
        if (user == null) throw new ReversiRuntimeException();
        final UserInputReader userInputReader = this::readPlayerMove;
        NetworkMatchEventListener listener = new NetworkMatchEventListener(this, userInputReader);
        client.match(user, listener);
      } catch (Exception e) {
        postErrorStatus(new ErrorStatus());
      }
    });
  }


  class NetworkMatchEventListener implements MatchEventListener {
    private final MatchEventDispatcher matchEventDispatcher;
    private final UserInputReader userInputReader;
    private Piece assignedPiece;
    private String player1Name;
    private String player2Name;

    public NetworkMatchEventListener(MatchEventDispatcher matchEventDispatcher, UserInputReader userInputReader) {
      this.matchEventDispatcher = matchEventDispatcher;
      this.userInputReader = userInputReader;
    }

    @Override
    public void onMatchStart(MatchStartMessage event) {
      assignedPiece = event.getAssignedPiece();
      player1Name = event.getPlayer1Name();
      player2Name = event.getPlayer2Name();
      MatchStartMessage newMessage = new MatchStartMessage(Piece.PLAYER_1 == assignedPiece ? PlayerType.HUMAN_PLAYER : PlayerType.NETWORK_PLAYER,
              event.getPlayer1Name(),
              Piece.PLAYER_2 == assignedPiece ? PlayerType.HUMAN_PLAYER : PlayerType.NETWORK_PLAYER,
              event.getPlayer2Name(),
              event.getPlayerId(),
              event.getMatchId(),
              Piece.PLAYER_1);
      matchEventDispatcher.postMatchStart(newMessage);
    }

    @Override
    public Coordinates onMatchPlayerMove(MatchStatusMessage event) {
      GameSnapshot gameSnapshot = event.getGameSnapshot();
      Timber.i("Tocca muovere a %s %s", gameSnapshot.getActivePiece(), gameSnapshot.getAvailableMoves().getMovesActivePlayer());
      matchEventDispatcher.postMatchMove(event);

      if (assignedPiece == gameSnapshot.getActivePiece() && gameSnapshot.getAvailableMoves().isAvailableMovesForActivePlayer()) {
        RandomDecisionHandler handler = new RandomDecisionHandler();
        Coordinates move;
        if (BuildConfig.AUTOMATIC_NETWORK_PLAYER) {
          move = handler.compute(gameSnapshot.getAvailableMoves().getMovesActivePlayer());
        } else {
          move = userInputReader.readInputFor(null,
                  event.getGameSnapshot().getAvailableMoves().getMovesActivePlayer());
        }

        return move;
      } else {
        // it turn of other player. Do nothing
        return null;
      }
    }

    @Override
    public void onMatchEnd(MatchEndMessage event) {
      playedMatchRepository.insert(player1Name, player2Name, event.getStatus(), event.getScore(), assignedPiece);
      matchEventDispatcher.postMatchEnd(event);
    }
  }
}
