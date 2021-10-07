package com.example.anilscreation.gitHubUser;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anilscreation.MainActivity;
import com.example.anilscreation.R;
import com.example.anilscreation.databinding.GitHubUserFragmentBinding;
import com.example.anilscreation.gitHubUser.adapter.GithubUserAdapter;
import com.example.anilscreation.gitHubUser.model.Project;

import java.util.List;

public class GitHubUserFragment extends Fragment {
    private static final String KEY_PROJECT_ID = "project_id";
    private GitHubUserViewModel mViewModel;
    private GitHubUserFragmentBinding binding;
    GithubUserAdapter recycleriewAdapter;

    public static GitHubUserFragment newInstance() {
        return new GitHubUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.git_hub_user_fragment,container,false);
        recycleriewAdapter=new GithubUserAdapter(projectClickCallBack);
        binding.projectList.setAdapter(recycleriewAdapter);
        binding.setIsLoading(true);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GitHubUserViewModel.class);
        observeViewModel(mViewModel);
    }

    private void observeViewModel(GitHubUserViewModel mViewModel) {
        mViewModel.getProjectListObserable().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {
                if (projects!=null){
                    binding.setIsLoading(false);
                    recycleriewAdapter.setProjectList(projects);
                }
            }
        });
    }

    private final ProjectClickCallBack projectClickCallBack=new ProjectClickCallBack() {
        @Override
        public void onClick(Project project) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).show(project);
            }
        }
    };
}