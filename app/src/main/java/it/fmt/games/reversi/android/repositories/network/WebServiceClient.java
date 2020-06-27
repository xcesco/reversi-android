package it.fmt.games.reversi.android.repositories.network;

import it.fmt.games.reversi.android.repositories.network.model.UserRegistration;
import it.fmt.games.reversi.android.repositories.network.model.ConnectedUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceClient {

    @PATCH("api/v1/public/users/{uuid}/ready")
    Call<ConnectedUser> executeUserIsReadyToPlay(@Path("uuid") String userId);

    @PATCH("api/v1/public/users/{uuid}/not-ready")
    Call<ConnectedUser> executeUserIsNotReadyToPlay(@Path("uuid") String userId);

    @POST("api/v1/public/users")
    Call<ConnectedUser> connect(@Body UserRegistration userInfo);

}
