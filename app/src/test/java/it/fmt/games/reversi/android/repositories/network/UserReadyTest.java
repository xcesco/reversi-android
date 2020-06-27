package it.fmt.games.reversi.android.repositories.network;

import net.lachlanmckee.timberjunit.TimberTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import it.fmt.games.reversi.android.repositories.network.model.UserRegistration;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;

public class UserReadyTest extends AbstractNetworkUnitTest {

  @Rule
  public TimberTestRule logAllAlwaysRule = TimberTestRule.logAllAlways();

  @Test
  public void connect() throws IOException, InterruptedException, ExecutionException {
    String baseUrl = "https://8c23a3cad349.ngrok.io/";
    String webSocketBaseUrl = "wss://8c23a3cad349.ngrok.io/";

    {
      final NetworkClient client = new NetworkClient(baseUrl, webSocketBaseUrl);
      // register user from server
      final ConnectedUser user = client.connect(UserRegistration.of("player2"));

      client.prepareUserToMatch();
    }

    Thread.sleep(200_000);
    //client.diconnect();
  }


}
