package com.dot.friendandroid.views.main;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dot.friendandroid.FriendApplication;
import com.dot.friendandroid.model.UserModel;
import com.dot.friendandroid.utils.ApiUtils;
import com.dot.friendandroid.utils.FriendManager;
import com.dot.friendandroid.utils.type.User;
import com.dot.friendandroid.views.people.FriendDialog;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends BaseObservable implements Callback<UserModel>, SwipeRefreshLayout.OnRefreshListener, DialogInterface.OnDismissListener {
    private static final String TAG = MainViewModel.class.getSimpleName();

    public enum Type {
        FIRSTNAME,
        LASTNAME
    }

    @Inject User.UserRequest userRequest;
    @Inject FriendManager manager;
    @Inject ApiUtils apiUtils;

    private UserModel model;
    private MainAdapter adapter;
    private MainContract.View view;

    public MainViewModel(MainContract.View view, MainAdapter adapter){
        this.view = view;
        this.adapter = adapter;

        FriendApplication.getComponent().inject(this);

        userRequest.me(manager.getToken()).enqueue(this);
    }

    public void setModel(UserModel model) {
        this.model = model;
        Log.d(TAG, "Element !");
        if (model.Friends != null) {
            Log.d(TAG, "Size: " + model.Friends.size());
            this.adapter.setList(model.Friends);
        }
        notifyChange();
    }

    public void onClickField(View view, final Type type){
        final EditText text = new EditText(view.getContext());
        text.setHint(type.toString());
        new AlertDialog.Builder(view.getContext())
                .setTitle("Edit")
                .setView(text)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!text.getText().toString().isEmpty()) {
                            switch (type) {
                                case FIRSTNAME: userRequest.updateFirstName(manager.getToken(), text.getText().toString()).enqueue(MainViewModel.this);break;
                                case LASTNAME: userRequest.updateLastName(manager.getToken(), text.getText().toString()).enqueue(MainViewModel.this);break;
                            }
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void onClickAddPeople(View view) {
        Log.d(TAG, "add");
        new FriendDialog(view.getContext(), null).setOnDismissListener(this);
    }

    public void onClickLogOut(View view) {
        manager.logOut();
        this.view.quitToLogin();
    }

    @Override
    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
        Log.d(TAG, "Code: " + response.code());

        switch (response.code()) {
            case 200:
                setModel(response.body());
                break;
            case 401: break;
            default: apiUtils.parseErrorAndShow(TAG, response);
        }
        view.setRefreshing(false);
    }

    @Override
    public void onFailure(Call<UserModel> call, Throwable t) {
        Log.e(TAG, "" + t.getMessage());
        view.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        userRequest.me(manager.getToken()).enqueue(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        onRefresh();
    }

    public String getFirstName() {
        return model != null && model.FirstName != null ? model.FirstName : "FirstName";
    }

    public String getLastName() {
        return model != null && model.LastName != null ? model.LastName : "LastName";
    }
}
