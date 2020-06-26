package it.fmt.reversi.android.repositories.network;

import net.lachlanmckee.timberjunit.TimberTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.repositories.network.model.User;

public class NetworkUnitTest extends AbstractNetworkUnitTest {

  @Rule
  public TimberTestRule logAllAlwaysRule = TimberTestRule.logAllAlways();

  @Test
  public void connect() throws IOException, InterruptedException {
    String baseUrl = "https://8c23a3cad349.ngrok.io/";
    String webSocketBaseUrl = "wss://8c23a3cad349.ngrok.io/";

    {
      NetworkClient client = new NetworkClient(baseUrl, webSocketBaseUrl);
      final User user = client.connect(ConnectedUser.of("player2"));
      client.match(new TestNetworkPlayerHandler(client, user));
    }

    {
      NetworkClient client = new NetworkClient(baseUrl, webSocketBaseUrl);
      final User user = client.connect(ConnectedUser.of("player1"));
      client.match(new TestNetworkPlayerHandler(client, user));
    }

    Thread.sleep(200_000);
    //client.diconnect();
  }


}
