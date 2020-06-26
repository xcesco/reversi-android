package it.fmt.reversi.android.repositories.network;

import net.lachlanmckee.timberjunit.TimberTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import io.reactivex.functions.Action;
import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import it.fmt.games.reversi.android.repositories.network.model.User;
import timber.log.Timber;

public class UserReadyTest extends AbstractNetworkUnitTest {

  @Rule
  public TimberTestRule logAllAlwaysRule = TimberTestRule.logAllAlways();

  @Test
  public void connect() throws IOException, InterruptedException, ExecutionException {
    String baseUrl = "https://8c23a3cad349.ngrok.io/";
    String webSocketBaseUrl = "wss://8c23a3cad349.ngrok.io/";

    {
      NetworkClient client = new NetworkClient(baseUrl, webSocketBaseUrl);
      // get user from server
      final User user = client.connect(ConnectedUser.of("player2"));

      // register to user status
      client.observeUserStatus().subscribe(stompMessage -> {
        Timber.i("observeUserStatus receives " + stompMessage.getPayload());
      });

      // tell to server user is ready to play
      client.sendUserReady(user.getId()).subscribe(() -> Timber.i("sendUserReady complete" ));
    }

    Thread.sleep(200_000);
    //client.diconnect();
  }


}
