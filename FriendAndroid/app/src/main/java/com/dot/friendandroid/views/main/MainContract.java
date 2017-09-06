package com.dot.friendandroid.views.main;

public interface MainContract {
    public interface View {
        void setRefreshing(boolean refreshing);

        void quitToLogin();
    }
}
