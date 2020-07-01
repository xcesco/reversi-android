package it.fmt.games.reversi.android.repositories.network;

import net.lachlanmckee.timberjunit.TimberTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;
import it.fmt.games.reversi.android.repositories.network.model.UserRegistration;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.viewmodels.NetworkMatchViewModel;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;

import static org.junit.Assert.assertTrue;

public class NetworkUnitTest extends AbstractNetworkUnitTest {

  @Rule
  public TimberTestRule logAllAlwaysRule = TimberTestRule.logAllAlways();

  @Test
  public void playerMatchBothHuman() throws IOException, InterruptedException, ExecutionException {
    String baseUrl = "f9e70b111755.ngrok.io";
    final String serverUrl = "https://{baseUrl}/".replace("{baseUrl}", baseUrl);

    final CompletableFuture<Boolean> finish1 = new CompletableFuture<>();
    final CompletableFuture<Boolean> finish2 = new CompletableFuture<>();

    final NetworkClientImpl client1 = new NetworkClientImpl(serverUrl);
    final NetworkClientImpl client2 = new NetworkClientImpl(serverUrl);

    ExecutorService executorService = Executors.newFixedThreadPool(2);
    executorService.submit(() -> {
      final ConnectedUser user = client1.connect(UserRegistration.of("player2"));
      client1.match(user, new MatchEventListener() {
        @Override
        public void onMatchStart(MatchStartMessage event) {

        }

        @Override
        public Coordinates onMatchPlayerMove(MatchStatusMessage event) {
          return event.getGameSnapshot().getAvailableMoves().getMovesActivePlayer().get(0);
        }

        @Override
        public void onMatchEnd(MatchEndMessage event) {

        }
      }, new NetworkMatchViewModel.CurrentPieceStorage() {
        @Override
        public Piece getActivePiece() {
          return null;
        }

        @Override
        public void setActivePiece(Piece activePiece) {

        }
      });
      finish1.complete(true);

    });

    executorService.submit(() -> {
      final ConnectedUser user = client2.connect(UserRegistration.of("player1"));
      client2.match(user, new MatchEventListener() {
        @Override
        public void onMatchStart(MatchStartMessage event) {

        }

        @Override
        public Coordinates onMatchPlayerMove(MatchStatusMessage event) {
          return event.getGameSnapshot().getAvailableMoves().getMovesActivePlayer().get(0);
        }

        @Override
        public void onMatchEnd(MatchEndMessage event) {

        }
      }, new NetworkMatchViewModel.CurrentPieceStorage() {
        @Override
        public Piece getActivePiece() {
          return null;
        }

        @Override
        public void setActivePiece(Piece activePiece) {

        }
      });
      finish2.complete(true);
    });

    //Thread.sleep(5_000);
    boolean result1 = finish1.get();
    boolean result2 = finish2.get();
    assertTrue(result1 && result2);
    //client.diconnect();
    client1.disconnect();
    client2.disconnect();
  }

  @Test
  public void playerMatchOneHuman() throws IOException, InterruptedException, ExecutionException {
    String baseUrl = "https://e26cebedeada.ngrok.io/";

    final CompletableFuture<Boolean> finish1 = new CompletableFuture<>();
    //final CompletableFuture<Boolean> finish2 = new CompletableFuture<>();

    final NetworkClientImpl client1 = new NetworkClientImpl(baseUrl);
    //final NetworkClient client2 = new NetworkClient(httpUrl, webSocketBaseUrl);

    ExecutorService executorService = Executors.newFixedThreadPool(2);
    executorService.submit(() -> {
      final ConnectedUser user = client1.connect(UserRegistration.of("player1"));
      client1.match(user, new MatchEventListener() {
        @Override
        public void onMatchStart(MatchStartMessage event) {

        }

        @Override
        public Coordinates onMatchPlayerMove(MatchStatusMessage event) {
          return event.getGameSnapshot().getAvailableMoves().getMovesActivePlayer().get(0);
        }

        @Override
        public void onMatchEnd(MatchEndMessage event) {

        }
      }, new NetworkMatchViewModel.CurrentPieceStorage() {
        @Override
        public Piece getActivePiece() {
          return null;
        }

        @Override
        public void setActivePiece(Piece activePiece) {

        }
      });
      finish1.complete(true);

    });

    //Thread.sleep(5_000);
    boolean result1 = finish1.get();
    // boolean result2 = finish2.get();
    assertTrue(result1);
    //client.diconnect();
    client1.disconnect();
    // client2.disconnect();
  }


}
