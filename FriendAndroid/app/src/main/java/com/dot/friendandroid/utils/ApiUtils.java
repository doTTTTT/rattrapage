package com.dot.friendandroid.utils;

import android.Manifest;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.dot.friendandroid.model.ErrorModel;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Response;

public class ApiUtils {
    private static final String TAG = ApiUtils.class.getSimpleName();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Context context;

    public ApiUtils(Context context) {
        this.context = context;
    }

    public String parseErrorAndShow(String tag, Response<?> response){
        try {
            if (response.errorBody() != null) {
                String error = parseError(tag, response);

                ErrorModel errorModel = new Gson().fromJson(error, ErrorModel.class);

                Toast.makeText(context, errorModel.ErrorMessage, Toast.LENGTH_SHORT).show();

                return errorModel.ErrorMessage;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String parseError(String tag, Response<?> response) {
        String tmp = "";
        try {
            if (response.errorBody() != null) {
                tmp = response.errorBody().string();
                Log.e(tag, "" + tmp);
            }
        } catch (IOException e) {
            Log.e(TAG, "" + e.getMessage());
        }

        return tmp;
    }
}