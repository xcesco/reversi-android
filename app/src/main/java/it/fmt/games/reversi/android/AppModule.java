package it.fmt.games.reversi.android;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.fmt.games.reversi.android.repositories.network.NetworkClient;

@Module
public class AppModule {

    @Provides
    public Context provideAppContext() {
        return appContext;
    }

    private final Context appContext;

    @Inject
    @Singleton
    @Provides
    public NetworkClient providesNetworkClient() {
        NetworkClient networkClient = new NetworkClient(appContext.getResources().getString(R.string.base_url));

        return networkClient;
    }

    public AppModule(Context context) {
        this.appContext = context;
    }
}
