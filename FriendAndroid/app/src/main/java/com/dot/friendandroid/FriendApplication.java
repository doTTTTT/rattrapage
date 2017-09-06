package com.dot.friendandroid;

import android.app.Application;

import com.dot.friendandroid.utils.DaggerFriendComponent;
import com.dot.friendandroid.utils.FriendComponent;
import com.dot.friendandroid.utils.FriendModule;

public class FriendApplication extends Application {
    private static FriendComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerFriendComponent.builder()
                .friendModule(new FriendModule(this))
                .build();
    }

    public static FriendComponent getComponent() {
        return component;
    }
}
