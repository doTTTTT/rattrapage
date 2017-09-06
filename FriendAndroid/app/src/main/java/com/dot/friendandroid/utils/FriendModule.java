package com.dot.friendandroid.utils;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FriendModule {
    private Application application;

    public FriendModule(Application application) {
        this.application = application;
    }

    @Provides
    public Context providesContext(){
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public FriendManager providesFriendManage(Context context){
        return new FriendManager(context);
    }
}
