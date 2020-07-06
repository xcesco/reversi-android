package it.fmt.games.reversi.android.viewmodels;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.android.ReversiApplication;
import it.fmt.games.reversi.android.repositories.NetworkMatchRepository;
import it.fmt.games.reversi.android.repositories.PlayedMatchRepository;
import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.android.viewmodels.model.CpuType;
import it.fmt.games.reversi.model.Piece;

public class NetworkMatchViewModel extends AbstractMatchViewModel {
  @Inject
  PlayedMatchRepository playedMatchRepository;

  @Inject
  NetworkMatchRepository networkMatchRepository;

  private Piece assignedPiece;
  private String player1Name;
  private String player2Name;


  public NetworkMatchViewModel() {
    ReversiApplication.getInjector().inject(this);
  }

  @Override
  public void match(String playerName, GameType gameType, CpuType cpuType) {
    final UserInputReader userInputReader = this::readPlayerMove;

    networkMatchRepository.match(this, playerName, userInputReader);
  }

  @Override
  public void postMatchStart(@NotNull MatchStartMessage event) {
    super.postMatchStart(event);

    assignedPiece = event.getAssignedPiece();
    player1Name = event.getPlayer1Name();
    player2Name = event.getPlayer2Name();
  }

  @Override
  public void postMatchEnd(@NotNull MatchEndMessage event) {
    super.postMatchEnd(event);
    playedMatchRepository.insert(player1Name, player2Name, event.getStatus(), event.getScore(), assignedPiece);
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    this.networkMatchRepository.onCleared();
  }
}
