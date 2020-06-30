package it.fmt.games.reversi.android;

import javax.inject.Singleton;

import dagger.Component;
import it.fmt.games.reversi.android.viewmodels.AbstractMatchViewModel;
import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.persistence.MatchRepository;
import it.fmt.games.reversi.android.viewmodels.MatchEventDispatcher;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
  MatchRepository provideMatchRepository();

  NetworkClient provideNetworkClient();

  void inject(MatchEventDispatcher matchEventDispatcher);

  void inject(AbstractMatchViewModel abstractMatchViewModel);
}
