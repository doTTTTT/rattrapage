package com.dot.friendandroid.utils.type;

import android.support.annotation.FractionRes;

import com.dot.friendandroid.model.AuthModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

@Module
public class Auth {
    public interface AuthRequest {
        @FormUrlEncoded
        @POST("/auth/basic")
        Call<AuthModel> auth(@Field("email") String email, @Field("password") String password);
    }

    @Provides
    public AuthRequest providesAuthRequest(Retrofit retrofit){
        return retrofit.create(AuthRequest.class);
    }
}
