package com.dot.friendandroid.views.login;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;

import com.dot.friendandroid.FriendApplication;
import com.dot.friendandroid.model.AuthModel;
import com.dot.friendandroid.utils.ApiUtils;
import com.dot.friendandroid.utils.FriendComponent;
import com.dot.friendandroid.utils.FriendManager;
import com.dot.friendandroid.utils.type.Auth;
import com.dot.friendandroid.views.inscription.InscriptionActivity;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends BaseObservable implements Callback<AuthModel> {
    private static final String TAG = LoginViewModel.class.getSimpleName();

    @Inject Auth.AuthRequest authRequest;
    @Inject FriendManager manager;
    @Inject ApiUtils apiUtils;

    private LoginContract.View view;

    public LoginViewModel(LoginContract.View view) {
        this.view = view;

        FriendApplication.getComponent().inject(this);
    }

    public void onClickSignIn(View view, AppCompatEditText login, AppCompatEditText password){
        authRequest.auth(login.getText().toString(), password.getText().toString()).enqueue(this);
    }

    public void onClickSignUp(View view){
        view.getContext().startActivity(new Intent(view.getContext(), InscriptionActivity.class));
    }

    @Override
    public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
        Log.d(TAG, "Code: " +  response.code());

        switch (response.code()) {
            case 200:
                manager.logIn(response.body().token, response.body().User.ID);
                view.quitToMain();
                break;
            default:
                apiUtils.parseErrorAndShow(TAG, response);
        }
    }

    @Override
    public void onFailure(Call<AuthModel> call, Throwable t) {
        Log.e(TAG, "" + t.getMessage());
    }
}
