package com.dot.friendandroid.utils.type;

import com.dot.friendandroid.model.PeopleModel;
import com.dot.friendandroid.utils.RetrofitModule;

import java.util.List;

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
public class People {
    public interface PeopleRequest {
        @GET("/people/user/me")
        Call<List<PeopleModel>> getListPeople(@Header(RetrofitModule.Auhorization) String token);

        @GET("/people/me/{id}")
        Call<PeopleModel> getFriend(@Header(RetrofitModule.Auhorization) String token);

        @FormUrlEncoded
        @POST("/people")
        Call<PeopleModel> addPeople(@Header(RetrofitModule.Auhorization) String token,
                                    @Field("user_id") String userID,
                                    @Field("firstname") String firstname,
                                    @Field("lastname") String lastname,
                                    @Field("date_of_birth") String dateOfBirth,
                                    @Field("description") String description,
                                    @Field("phone_number") String phoneNumber,
                                    @Field("relationship") String relationShip,
                                    @Field("street") String street,
                                    @Field("city") String city,
                                    @Field("state") String state,
                                    @Field("zipcode") String zipCode,
                                    @Field("country") String country);

        @FormUrlEncoded
        @PUT("/people/address/me/{id}")
        Call<PeopleModel> updateAddress(@Header(RetrofitModule.Auhorization) String token,
                                        @Path("id") String id,
                                        @Field("street") String street,
                                        @Field("city") String city,
                                        @Field("state") String state,
                                        @Field("zipcode") String zipCode,
                                        @Field("country") String country);

        @FormUrlEncoded
        @PUT("/people/me/{id}")
        Call<PeopleModel> updatePeople(@Header(RetrofitModule.Auhorization) String token,
                                       @Path("id") String id,
                                       @Field("firstname") String firstname,
                                       @Field("lastname") String lastname,
                                       @Field("date_of_birth") String dateOfBirth,
                                       @Field("description") String description,
                                       @Field("phone_number") String phoneNumber,
                                       @Field("relationship") String relationShip);

        @DELETE("/people/me/{id}")
        Call<PeopleModel> deleteFriend(@Header(RetrofitModule.Auhorization) String token,
                                       @Path("id") String id);
    }

    @Provides
    public PeopleRequest providesFriendRequest(Retrofit retrofit){
        return retrofit.create(PeopleRequest.class);
    }
}
