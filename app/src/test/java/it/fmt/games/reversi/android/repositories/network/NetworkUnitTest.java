package it.fmt.games.reversi.android.repositories.network;

import net.lachlanmckee.timberjunit.TimberTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.fmt.games.reversi.android.repositories.network.model.UserRegistration;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;

import static org.junit.Assert.assertTrue;

public class NetworkUnitTest extends AbstractNetworkUnitTest {

  @Rule
  public TimberTestRule logAllAlwaysRule = TimberTestRule.logAllAlways();

  @Test
  public void connect() throws IOException, InterruptedException, ExecutionException {
    final String baseUrl = "https://8c23a3cad349.ngrok.io/";
    final String webSocketBaseUrl = "wss://8c23a3cad349.ngrok.io/";

    CompletableFuture<Boolean> finish1 = new CompletableFuture<>();
    CompletableFuture<Boolean> finish2 = new CompletableFuture<>();


    ExecutorService executorService = Executors.newFixedThreadPool(2);
    executorService.submit(() -> {
      NetworkClient client = new NetworkClient(baseUrl, webSocketBaseUrl);
      final ConnectedUser user = client.connect(UserRegistration.of("player2"));
      client.match(new TestNetworkPlayerHandler(client, user));
      finish1.complete(true);
    });
    executorService.submit(() -> {
      NetworkClient client = new NetworkClient(baseUrl, webSocketBaseUrl);
      final ConnectedUser user = client.connect(UserRegistration.of("player1"));
      client.match(new TestNetworkPlayerHandler(client, user));
      finish2.complete(true);
    });

    boolean result=finish1.get() && finish2.get();
    assertTrue(result);
    //client.diconnect();
  }


}
