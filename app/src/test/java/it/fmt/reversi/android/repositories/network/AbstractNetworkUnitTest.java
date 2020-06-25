package it.fmt.reversi.android.repositories.network;

import net.lachlanmckee.timberjunit.TimberTestRule;

import org.junit.Rule;

import it.fmt.games.reversi.android.repositories.network.NetworkClient;

public abstract class AbstractNetworkUnitTest {


    public void log(Object message) {
        System.out.println(message);
    }

    public void log(String message, Object ... args) {
        System.out.println(String.format(message, args));
    }

    protected NetworkClient networkClient;


}
