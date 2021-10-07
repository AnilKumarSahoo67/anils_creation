package com.example.anilscreation.gitHubUser.repos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.anilscreation.gitHubUser.model.Project;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectRepository {
    private GitHubService gitHubService;
    private static ProjectRepository projectRepository;

    private ProjectRepository(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(GitHubService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gitHubService=retrofit.create(GitHubService.class);
    }
    public synchronized static ProjectRepository getInstance(){
        if (projectRepository==null){
            projectRepository=new ProjectRepository();
        }
        return projectRepository;
    }
    public LiveData<List<Project>> getProjectList(String userId){
        final MutableLiveData<List<Project>> data=new MutableLiveData<>();
        gitHubService.getProjectList(userId).enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
