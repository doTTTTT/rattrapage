package com.dot.friendandroid.utils.type;


import com.dot.friendandroid.model.AuthModel;
import com.dot.friendandroid.model.UserModel;
import com.dot.friendandroid.utils.RetrofitModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

@Module
public class User {
    public interface UserRequest {
        @GET("/users/me")
        Call<UserModel> me(@Header("Authorization") String token);

        @FormUrlEncoded
        @POST("/users")
        Call<AuthModel> createUser(@Field("email") String email,
                                   @Field("password") String password);

        @FormUrlEncoded
        @PUT("/users/me")
        Call<UserModel> updateUser(@Header(RetrofitModule.Auhorization) String token);

        @FormUrlEncoded
        @PUT("/users/me")
        Call<UserModel> updateFirstName(@Header(RetrofitModule.Auhorization) String token,
                                        @Field("firstname") String firstname);

        @FormUrlEncoded
        @PUT("/users/me")
        Call<UserModel> updateLastName(@Header(RetrofitModule.Auhorization) String token,
                                       @Field("lastname") String lastname);

        @DELETE("/users/{id}")
        Call<UserModel> deleteUser(@Header(RetrofitModule.Auhorization) String token,
                                   @Path("id") String id);
    }

    @Singleton
    @Provides
    public UserRequest providesUserRequest(Retrofit retrofit){
        return retrofit.create(UserRequest.class);
    }
}
