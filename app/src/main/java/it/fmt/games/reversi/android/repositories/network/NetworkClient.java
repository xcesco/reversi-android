package it.fmt.games.reversi.android.repositories.network;

import android.os.Build;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import it.fmt.games.reversi.android.BuildConfig;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUserInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class NetworkClient {
    private final ObjectMapper objectMapper;
    private final WebServiceClient webServiceClient;

    public NetworkClient(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        if (BuildConfig.LOG_ENABLED) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }


        // rest callß
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .callTimeout(240, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(chain -> {
                    Request.Builder ongoing = chain.request().newBuilder();

                    ongoing.addHeader("Accept", "application/json");

                    ongoing.addHeader("X-REVERSI-OS", "Android");
                    ongoing.addHeader("X-REVERSI-OSVersion", Build.VERSION.RELEASE+ ", SDK "+ String.valueOf(Build.VERSION.SDK_INT));
                    ongoing.addHeader("X-REVERSI-App", "FMT Reversi");
                    ongoing.addHeader("X-REVERSI-AppVersion", BuildConfig.VERSION_NAME);

                    return chain.proceed(ongoing.build());
                }).addInterceptor(interceptor).build();

        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Retrofit retrofitJackson = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(httpClient).build();

        webServiceClient = retrofitJackson.create(WebServiceClient.class);
    }

    public User connect(ConnectedUserInfo userInfo) throws IOException {
        return webServiceClient.connect(userInfo).execute().body();
    }

    public User readyToPlay(User user) throws IOException {
        return webServiceClient.executeUserIsReadyToPlay(user.getId()).execute().body();
    }

    public User notReadyToPlay(User user) throws IOException {
        return webServiceClient.executeUserIsNotReadyToPlay(user.getId()).execute().body();
    }
}
