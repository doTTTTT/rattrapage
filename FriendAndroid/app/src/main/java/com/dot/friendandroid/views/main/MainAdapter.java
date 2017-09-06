package com.dot.friendandroid.views.main;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dot.friendandroid.FriendApplication;
import com.dot.friendandroid.R;
import com.dot.friendandroid.databinding.ListItemPeopleBinding;
import com.dot.friendandroid.model.PeopleModel;
import com.dot.friendandroid.model.UserModel;
import com.dot.friendandroid.utils.ApiUtils;
import com.dot.friendandroid.utils.FriendManager;
import com.dot.friendandroid.utils.type.People;
import com.dot.friendandroid.utils.type.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> implements Callback<UserModel> {
    private static final String TAG = MainAdapter.class.getSimpleName();

    private List<PeopleModel> list;

    @Inject User.UserRequest userRequest;
    @Inject FriendManager manager;
    @Inject ApiUtils apiUtils;

    public MainAdapter() {
        list = new ArrayList<>();

        FriendApplication.getComponent().inject(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((ListItemPeopleBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_people, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position), this);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<PeopleModel> list) {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    public void refresh() {
        userRequest.me(manager.getToken()).enqueue(this);
    }

    @Override
    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
        Log.d(TAG, "Code: " + response.code());

        switch (response.code()) {
            case 200:
                if (response.body() != null && response.body().Friends != null) {
                    setList(response.body().Friends);
                }
                break;
            case 401: break;
            default: apiUtils.parseErrorAndShow(TAG, response);
        }
    }

    @Override
    public void onFailure(Call<UserModel> call, Throwable t) {
        Log.e(TAG, "" + t.getMessage());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemPeopleBinding binding;

        public ViewHolder(ListItemPeopleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(PeopleModel model, MainAdapter adapter){
            if (binding.getViewModel() == null) {
                binding.setViewModel(new ListItemPeopleViewModel(model, adapter));
            } else {
                binding.getViewModel().setModel(model);
            }
        }
    }
}
