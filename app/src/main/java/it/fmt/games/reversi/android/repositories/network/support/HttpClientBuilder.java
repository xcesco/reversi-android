package it.fmt.games.reversi.android.repositories.network.support;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import it.fmt.games.reversi.android.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public abstract class HttpClientBuilder {
  private HttpClientBuilder() {

  }

  @NotNull
  public static OkHttpClient buildHttpClient() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

    if (BuildConfig.LOG_ENABLED) {
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }
    // rest call
    return new OkHttpClient.Builder()
            .callTimeout(240, TimeUnit.SECONDS)
            .readTimeout(240, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(chain -> {
              Request.Builder ongoing = chain.request().newBuilder();

              ongoing.addHeader("Accept", "application/json");

//              ongoing.addHeader("X-REVERSI-OS", "Android");
//              ongoing.addHeader("X-REVERSI-OSVersion", Build.VERSION.RELEASE + ", SDK " + String.valueOf(Build.VERSION.SDK_INT));
//              ongoing.addHeader("X-REVERSI-App", "FMT Reversi");
//              ongoing.addHeader("X-REVERSI-AppVersion", BuildConfig.VERSION_NAME);

              return chain.proceed(ongoing.build());
            }).addInterceptor(interceptor).build();
  }
}
