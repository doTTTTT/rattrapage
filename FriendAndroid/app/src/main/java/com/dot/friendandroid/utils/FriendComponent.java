package com.dot.friendandroid.utils;

import com.dot.friendandroid.utils.type.Auth;
import com.dot.friendandroid.utils.type.People;
import com.dot.friendandroid.utils.type.User;
import com.dot.friendandroid.views.inscription.InscriptionViewModel;
import com.dot.friendandroid.views.login.LoginViewModel;
import com.dot.friendandroid.views.main.ListItemPeopleViewModel;
import com.dot.friendandroid.views.main.MainActivity;
import com.dot.friendandroid.views.main.MainAdapter;
import com.dot.friendandroid.views.main.MainViewModel;
import com.dot.friendandroid.views.people.FriendDialog;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        RetrofitModule.class,
        FriendModule.class,
        Auth.class,
        User.class,
        People.class
})
public interface FriendComponent {
    void inject(LoginViewModel loginViewModel);

    void inject(MainActivity mainActivity);

    void inject(InscriptionViewModel inscriptionViewModel);

    void inject(MainViewModel mainViewModel);

    void inject(FriendDialog friendDialog);

    void inject(ListItemPeopleViewModel listItemPeopleViewModel);

    void inject(MainAdapter adapter);
}
