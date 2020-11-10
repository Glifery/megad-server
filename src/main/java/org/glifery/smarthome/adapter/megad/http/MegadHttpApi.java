package org.glifery.smarthome.adapter.megad.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MegadHttpApi {
    @GET("/{password}/")
    Call<String> storeProfile(@Path("password") String password, @Query(value = "cmd", encoded = true) String cmd);
}
