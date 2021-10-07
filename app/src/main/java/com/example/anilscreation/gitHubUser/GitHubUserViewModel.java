package com.example.anilscreation.gitHubUser;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.anilscreation.gitHubUser.model.Project;
import com.example.anilscreation.gitHubUser.repos.ProjectRepository;

import java.util.List;

public class GitHubUserViewModel extends ViewModel {
    private final LiveData<List<Project>> projectListObserable;

    public GitHubUserViewModel(Application application){
        projectListObserable= ProjectRepository.getInstance().getProjectList("Google");

    }
    public LiveData<List<Project>> getProjectListObserable(){
        return projectListObserable;
    }
}