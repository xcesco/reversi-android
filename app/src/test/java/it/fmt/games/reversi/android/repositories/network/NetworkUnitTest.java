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
  public void playerMatchBothHuman() throws IOException, InterruptedException, ExecutionException {
    String baseUrl = "e26cebedeada.ngrok.io";
    final String httpUrl = "https://{baseUrl}/".replace("{baseUrl}", baseUrl);
    final String webSocketBaseUrl = "wss://{baseUrl}/".replace("{baseUrl}", baseUrl);

    final CompletableFuture<Boolean> finish1 = new CompletableFuture<>();
    final CompletableFuture<Boolean> finish2 = new CompletableFuture<>();

    final NetworkClient client1 = new NetworkClient(httpUrl, webSocketBaseUrl);
    final NetworkClient client2 = new NetworkClient(httpUrl, webSocketBaseUrl);

    ExecutorService executorService = Executors.newFixedThreadPool(2);
    executorService.submit(() -> {
      final ConnectedUser user = client1.connect(UserRegistration.of("player2"));
      client1.match(user, new TestNetworkPlayerHandler(client1, user));
      finish1.complete(true);

    });

    executorService.submit(() -> {
      final ConnectedUser user = client2.connect(UserRegistration.of("player1"));
      client2.match(user, new TestNetworkPlayerHandler(client2, user));
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
    String baseUrl = "e26cebedeada.ngrok.io";
    final String httpUrl = "https://{baseUrl}/".replace("{baseUrl}", baseUrl);
    final String webSocketBaseUrl = "wss://{baseUrl}/".replace("{baseUrl}", baseUrl);

    final CompletableFuture<Boolean> finish1 = new CompletableFuture<>();
    //final CompletableFuture<Boolean> finish2 = new CompletableFuture<>();

    final NetworkClient client1 = new NetworkClient(httpUrl, webSocketBaseUrl);
    //final NetworkClient client2 = new NetworkClient(httpUrl, webSocketBaseUrl);

    ExecutorService executorService = Executors.newFixedThreadPool(2);
    executorService.submit(() -> {
      final ConnectedUser user = client1.connect(UserRegistration.of("player1"));
      client1.match(user, new TestNetworkPlayerHandler(client1, user));
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
