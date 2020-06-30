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
    String baseUrl = "https://e26cebedeada.ngrok.io/";

    {
      final NetworkClientImpl client = new NetworkClientImpl(baseUrl);
      // register user from server
      final ConnectedUser user = client.connect(UserRegistration.of("player2"));

      // client.watchUserStatus();
    }

    Thread.sleep(200_000);
    //client.diconnect();
  }


}
