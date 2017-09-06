package com.dot.friendandroid.views.inscription;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dot.friendandroid.R;
import com.dot.friendandroid.databinding.ActivityInscriptionBinding;
import com.dot.friendandroid.views.main.MainActivity;

public class InscriptionActivity extends AppCompatActivity implements InscriptionContract.View {
    private ActivityInscriptionBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inscription);
        binding.setViewModel(new InscriptionViewModel(this));
    }

    @Override
    public void setError(String error, InscriptionContract.Type type) {
        switch (type) {
            case EMAIL: binding.userEmailLayout.setError(error); break;
            case PASSWORD: binding.userPasswordLayout.setError(error); break;
            case PASSWORD_CONF: binding.userPasswordConfLayout.setError(error); break;
        }
    }

    @Override
    public void quitToLogin() {
        finish();
    }

    @Override
    public void quitToMain() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
