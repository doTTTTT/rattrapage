package com.dot.friendandroid.views.inscription;

import android.databinding.BaseObservable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;

import com.dot.friendandroid.FriendApplication;
import com.dot.friendandroid.model.AuthModel;
import com.dot.friendandroid.model.UserModel;
import com.dot.friendandroid.utils.ApiUtils;
import com.dot.friendandroid.utils.FriendManager;
import com.dot.friendandroid.utils.type.User;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriptionViewModel extends BaseObservable implements Callback<AuthModel> {
    private static final String TAG = InscriptionViewModel.class.getSimpleName();

    @Inject User.UserRequest userRequest;
    @Inject FriendManager manager;
    @Inject ApiUtils apiUtils;

    private InscriptionContract.View view;

    public InscriptionViewModel(InscriptionContract.View view) {
        this.view = view;

        FriendApplication.getComponent().inject(this);
    }

    public void onClickSignUp(View view, AppCompatEditText email, AppCompatEditText password, AppCompatEditText passwordConf) {
        boolean error = false;

        error = verify(email, InscriptionContract.Type.EMAIL, error);
        error = verify(password, InscriptionContract.Type.PASSWORD, error);
        error = verify(passwordConf, InscriptionContract.Type.PASSWORD_CONF, error);

        if (!password.getText().toString().isEmpty() && !passwordConf.getText().toString().isEmpty() &&
                !password.getText().toString().equals(passwordConf.getText().toString())) {
            error = true;
            this.view.setError("Not the same", InscriptionContract.Type.PASSWORD_CONF);
        }

        if (!error) {
            userRequest.createUser(email.getText().toString(), password.getText().toString()).enqueue(this);
        }
    }

    public boolean verify(AppCompatEditText text, InscriptionContract.Type type, boolean error) {
        if (text.getText().toString().isEmpty()) {
            this.view.setError("Cannot be empty", type);
            return true;
        } else { this.view.setError(null, type); }
        return error;
    }

    @Override
    public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
        Log.d(TAG, "Code: " + response.code());

        switch (response.code()) {
            case 201:
                manager.logIn(response.body().token, response.body().User.ID);
                this.view.quitToMain();
                break;
            default:
                apiUtils.parseErrorAndShow(TAG, response);
                break;
        }
    }

    @Override
    public void onFailure(Call<AuthModel> call, Throwable t) {
        Log.e(TAG, "" + t.getMessage());
    }
}
