package com.dot.friendandroid.views.people;

import android.app.DatePickerDialog;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.dot.friendandroid.model.PeopleModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FriendViewModel extends BaseObservable implements DatePickerDialog.OnDateSetListener {
    private PeopleModel model;
    private FriendContract.View view;

    public FriendViewModel(PeopleModel model, FriendContract.View view) {
        this.model = model;
        this.view = view;

        notifyChange();
    }

    public void setModel(PeopleModel model) {
        this.model = model;
        notifyChange();
    }

    public PeopleModel getUser(){
        return model;
    }

    public void onClickAge(View view) {
        new DatePickerDialog(view.getContext(), this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
    }

    @BindingAdapter("setupValue")
    public static void setupValue(AppCompatSpinner spinner, String relation){
        if (relation != null) {
            switch (relation) {
                case "friend":
                    spinner.setSelection(0);
                    break;
                case "family":
                    spinner.setSelection(1);
                    break;
                case "colleague":
                    spinner.setSelection(2);
                    break;
                case "acquaintance":
                    spinner.setSelection(3);
                    break;
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        this.view.setDate(simpleDateFormat.format(calendar.getTime()));
    }
}
