package it.fmt.games.reversi.android.repositories.network;

import it.fmt.games.reversi.android.repositories.network.model.ConnectedUserInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceClient {

    @PATCH("api/v1/public/users/{uuid}/ready")
    Call<User> executeUserIsReadyToPlay(@Path("uuid") String userId);

    @PATCH("api/v1/public/users/{uuid}/not-ready")
    Call<User> executeUserIsNotReadyToPlay(@Path("uuid") String userId);

    @POST("api/v1/public/users")
    Call<User> connect(@Body ConnectedUserInfo userInfo);

}