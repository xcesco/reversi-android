package it.fmt.games.reversi.android;

import javax.inject.Singleton;

import dagger.Component;
import it.fmt.games.reversi.android.viewmodels.NetworkMatchViewModel;
import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.persistence.MatchRepository;
import it.fmt.games.reversi.android.viewmodels.MatchViewModel;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
  MatchRepository provideMatchRepository();

  NetworkClient provideNetworkClient();

  void inject(MatchViewModel matchViewModel);

  void inject(NetworkMatchViewModel networkMatchViewModel);
}
