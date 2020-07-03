package it.fmt.games.reversi.android;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

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

        Timber.plant(new Timber.DebugTree());
//        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
//        } else {
//            Timber.plant(new CrashReportingTree());
//        }

        injector = DaggerAppComponent.builder().build();
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
            if (priority >= Log.ERROR) {
                FirebaseCrashlytics.getInstance().log(String.format(message));
            }
        }
    }
}
