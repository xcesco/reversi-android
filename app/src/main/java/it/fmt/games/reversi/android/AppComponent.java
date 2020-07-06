package it.fmt.games.reversi.android;

import javax.inject.Singleton;

import dagger.Component;
import it.fmt.games.reversi.android.repositories.network.NetworkClient;
import it.fmt.games.reversi.android.repositories.PlayedMatchRepository;
import it.fmt.games.reversi.android.viewmodels.LocalMatchViewModel;
import it.fmt.games.reversi.android.viewmodels.PlayedMatchesViewModel;
import it.fmt.games.reversi.android.viewmodels.NetworkMatchViewModel;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
  PlayedMatchRepository provideMatchRepository();

  NetworkClient provideNetworkClient();

  void inject(NetworkMatchViewModel viewModel);

  void inject(LocalMatchViewModel viewModel);

  void inject(PlayedMatchesViewModel viewModel);
}
