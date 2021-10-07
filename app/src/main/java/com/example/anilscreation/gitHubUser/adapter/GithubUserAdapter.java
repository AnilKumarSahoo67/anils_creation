package com.example.anilscreation.gitHubUser.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anilscreation.R;
import com.example.anilscreation.databinding.ProjectListItemBinding;
import com.example.anilscreation.gitHubUser.ProjectClickCallBack;
import com.example.anilscreation.gitHubUser.model.Project;

import java.util.List;
import java.util.Objects;

public class GithubUserAdapter extends RecyclerView.Adapter<MyUserViewHolder> {
    private ProjectClickCallBack projectClickCallBack;
    List<? extends Project> projectList;

    public GithubUserAdapter(ProjectClickCallBack projectClickCallBack) {
        this.projectClickCallBack=projectClickCallBack;
    }

    public void setProjectList(List<Project> projects) {
        if (this.projectList==null){
            this.projectList=projects;
            notifyItemRangeInserted(0,projectList.size());
        }else {
            DiffUtil.DiffResult result=DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return GithubUserAdapter.this.projectList.size();
                }

                @Override
                public int getNewListSize() {
                    return projectList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return GithubUserAdapter.this.projectList.get(oldItemPosition).id==projectList.get(newItemPosition).id;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Project project=projectList.get(newItemPosition);
                    Project old=projectList.get(oldItemPosition);
                    return project.id==old.id && Objects.equals(project.git_url,old.git_url);
                }
            });
            this.projectList=projects;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public MyUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProjectListItemBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
        , R.layout.project_list_item,parent,false);
        binding.setCallback(projectClickCallBack);
        return new MyUserViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return projectList==null?0:projectList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyUserViewHolder holder, int position) {
        holder.binding.setProject(projectList.get(position));
        holder.binding.executePendingBindings();
    }

}
