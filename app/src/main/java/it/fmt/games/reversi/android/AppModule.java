package it.fmt.games.reversi.android;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.network.NetworkClientImpl;

@Module
public class AppModule {

  @Singleton
  @Provides
  public NetworkClient providesNetworkClient() {
    String baseUrl = ReversiApplication.getContext().getResources().getString(R.string.base_url);
    NetworkClientImpl networkClient = new NetworkClientImpl(baseUrl);

    return networkClient;
  }

}
