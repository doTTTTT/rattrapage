package com.dot.friendandroid.views.main;

import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.dot.friendandroid.FriendApplication;
import com.dot.friendandroid.model.PeopleModel;
import com.dot.friendandroid.utils.ApiUtils;
import com.dot.friendandroid.utils.FriendManager;
import com.dot.friendandroid.utils.type.People;
import com.dot.friendandroid.views.people.FriendDialog;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListItemPeopleViewModel extends BaseObservable implements View.OnLongClickListener, Callback<PeopleModel> {
    private static final String TAG = ListItemPeopleViewModel.class.getSimpleName();

    @Inject People.PeopleRequest peopleRequest;
    @Inject FriendManager manager;
    @Inject ApiUtils apiUtils;

    private PeopleModel model;
    private MainAdapter adapter;

    public ListItemPeopleViewModel(PeopleModel model, MainAdapter adapter) {
        this.model = model;
        this.adapter = adapter;

        FriendApplication.getComponent().inject(this);
    }

    public void setModel(PeopleModel model) {
        this.model = model;
        notifyChange();
    }

    public void onClickCard(View view) {
        new FriendDialog(view.getContext(), model);
    }

    public PeopleModel getUser(){
        return model;
    }

    public String getAddress(){
        return model != null && model.Address != null ? model.Address.ZipCode + ", " + model.Address.Street + ", " + model.Address.City + ", " + model.Address.State + ", " + model.Address.Country : "";
    }

    public String getDate(){
        try {
            return model != null && model.DateOfBirth != null ? new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(model.DateOfBirth)) : "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @BindingAdapter("deleteListener")
    public static final void deleteListener(View view, View.OnLongClickListener listener){
        view.setOnLongClickListener(listener);
    }

    @Override
    public boolean onLongClick(View v) {
        new AlertDialog.Builder(v.getContext())
                .setTitle("Delete ?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        peopleRequest.deleteFriend(manager.getToken(), model.ID).enqueue(ListItemPeopleViewModel.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
        return true;
    }

    @Override
    public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
        Log.d(TAG, "Code: " + response.body());

        if (response.code() == 204) {
            adapter.refresh();
        }
    }

    @Override
    public void onFailure(Call<PeopleModel> call, Throwable t) {
        Log.e(TAG, "" + t.getMessage());
    }
}
