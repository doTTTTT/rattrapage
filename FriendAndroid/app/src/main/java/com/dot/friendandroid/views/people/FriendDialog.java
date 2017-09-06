package com.dot.friendandroid.views.people;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;

import com.dot.friendandroid.FriendApplication;
import com.dot.friendandroid.R;
import com.dot.friendandroid.databinding.DialogFriendBinding;
import com.dot.friendandroid.model.PeopleModel;
import com.dot.friendandroid.utils.ApiUtils;
import com.dot.friendandroid.utils.FriendManager;
import com.dot.friendandroid.utils.type.People;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener, Callback<PeopleModel>, FriendContract.View {
    private static final String TAG = FriendDialog.class.getSimpleName();

    @Inject People.PeopleRequest peopleRequest;
    @Inject FriendManager manager;
    @Inject ApiUtils apiUtils;

    private Dialog dialog;
    private PeopleModel model;
    private boolean newOne = false;
    private DialogFriendBinding binding;

    public FriendDialog(Context context, PeopleModel model) {
        super(context);
        this.model = model;

        FriendApplication.getComponent().inject(this);

        newOne = model == null;

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_friend, null, false);
        binding.setViewModel(new FriendViewModel(model, this));

        setTitle("Friend");
        setView(binding.getRoot());
        setPositiveButton(newOne ? "Add" : "Ok", this);
        setNegativeButton("Cancel", this);
        dialog = show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (!binding.friendFirstName.getText().toString().isEmpty() &&
                        !binding.friendLastName.getText().toString().isEmpty()) {
                    if (newOne) {
                        peopleRequest.addPeople(manager.getToken(),
                                manager.getUserID(),
                                binding.friendFirstName.getText().toString(),
                                binding.friendLastName.getText().toString(),
                                !binding.friendAge.getText().toString().equals("Age") ? binding.friendAge.getText().toString() : null,
                                binding.friendDescription.getText().toString(),
                                "",
                                binding.friendRelationShip.getSelectedItem().toString(),
                                binding.friendStreet.getText().toString(),
                                binding.friendCity.getText().toString(),
                                binding.friendState.getText().toString(),
                                binding.friendZipCode.getText().toString(),
                                binding.friendCountry.getText().toString()).enqueue(this);
                    } else {
                        peopleRequest.updatePeople(manager.getToken(),
                                model.ID,
                                binding.friendFirstName.getText().toString(),
                                binding.friendLastName.getText().toString(),
                                !binding.friendAge.getText().toString().equals("Age") ? binding.friendAge.getText().toString() : null,
                                binding.friendDescription.getText().toString(),
                                "",
                                binding.friendRelationShip.getSelectedItem().toString()).enqueue(this);
                        peopleRequest.updateAddress(manager.getToken(),
                                model.ID,
                                binding.friendStreet.getText().toString(),
                                binding.friendCity.getText().toString(),
                                binding.friendState.getText().toString(),
                                binding.friendZipCode.getText().toString(),
                                binding.friendCountry.getText().toString()).enqueue(this);
                    }
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.cancel();
                break;
        }
    }

    @Override
    public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
        Log.d(TAG, "Code: " + response.code());

        switch (response.code()) {
            case 200:
            case 201:
                dialog.dismiss();
                break;
            default:
                apiUtils.parseErrorAndShow(TAG, response);
                break;
        }
    }

    @Override
    public void onFailure(Call<PeopleModel> call, Throwable t) {
        Log.e(TAG, "" + t.getMessage());
    }

    @Override
    public void setDate(String date) {
        binding.friendAge.setText(date);
    }
}
