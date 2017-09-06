package com.dot.friendandroid.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {
    public static final String Auhorization = "Authorization";

    private static final String BASE_URL = "http://api.friends.patate.io";

    @Provides
    @Singleton
    public Gson provideGson(){
        return new GsonBuilder()
                .create();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build();

        return retrofit;
    }

    @Provides
    @Singleton
    public ApiUtils providesApiUtils(Context context){
        return new ApiUtils(context);
    }
}
