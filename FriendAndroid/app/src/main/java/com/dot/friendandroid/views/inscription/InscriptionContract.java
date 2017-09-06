package com.dot.friendandroid.views.inscription;

public interface InscriptionContract {
    public enum Type {
        EMAIL,
        PASSWORD,
        PASSWORD_CONF
    }

    public interface View {
        void setError(String error, Type type);

        void quitToLogin();

        void quitToMain();
    }
}
