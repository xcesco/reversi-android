package it.fmt.reversi.android.repositories.network;

import org.junit.Test;

import java.io.IOException;

import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.network.User;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUserInfo;

public class NetworkUnitTest extends AbstractNetworkUnitTest {

    @Test
    public void connect() throws IOException {
        NetworkClient client=new NetworkClient("https://8c23a3cad349.ngrok.io/");
        User user=client.connect(ConnectedUserInfo.of("ciao"));
        user=client.readyToPlay(user);
        user=client.notReadyToPlay(user);
    }
}
