package it.fmt.games.reversi.android;

import android.app.Application;
import android.content.Context;

public class ReversiApplication extends Application {
    private static AppComponent injector;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static AppComponent getInjector() {
        return injector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        build();
    }

    private void build() {
        context = this;
        injector = DaggerAppComponent.builder().build();
    }
}
