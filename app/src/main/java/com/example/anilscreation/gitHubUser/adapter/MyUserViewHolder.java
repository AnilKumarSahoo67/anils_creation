package com.example.anilscreation.gitHubUser.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anilscreation.databinding.ProjectListItemBinding;

public class MyUserViewHolder extends RecyclerView.ViewHolder {
    final ProjectListItemBinding binding;
    public MyUserViewHolder(@NonNull ProjectListItemBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
    }
}
