package com.dot.friendandroid.views.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dot.friendandroid.FriendApplication;
import com.dot.friendandroid.R;
import com.dot.friendandroid.databinding.ActivityMainBinding;
import com.dot.friendandroid.utils.FriendManager;
import com.dot.friendandroid.views.login.LoginActivity;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    @Inject FriendManager manager;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        FriendApplication.getComponent().inject(this);

        Log.d("TOKEN", "" + manager.getToken());

        if (manager.isLoggedIn()) {
            MainAdapter adapter = new MainAdapter();
            MainViewModel viewModel = new MainViewModel(this, adapter);
            binding.setViewModel(viewModel);

            binding.refresh.setOnRefreshListener(viewModel);
            binding.userFriendList.setAdapter(adapter);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        binding.refresh.setRefreshing(refreshing);
    }

    @Override
    public void quitToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
