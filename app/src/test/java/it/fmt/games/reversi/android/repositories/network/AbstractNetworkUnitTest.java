package it.fmt.games.reversi.android.repositories.network;

public abstract class AbstractNetworkUnitTest {


    public void log(Object message) {
        System.out.println(message);
    }

    public void log(String message, Object ... args) {
        System.out.println(String.format(message, args));
    }

    protected NetworkClientImpl networkClient;


}
