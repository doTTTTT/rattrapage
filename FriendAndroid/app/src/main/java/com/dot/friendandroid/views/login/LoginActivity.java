package com.dot.friendandroid.views.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dot.friendandroid.FriendApplication;
import com.dot.friendandroid.R;
import com.dot.friendandroid.databinding.ActivityLoginBinding;
import com.dot.friendandroid.views.main.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setViewModel(new LoginViewModel(this));
    }

    @Override
    public void quitToMain() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
