package com.example.api.services;

import com.example.api.models.Login;
import com.example.api.models.LoginRequest;
import com.example.api.models.MultipleArticle;
import com.example.api.models.SignUpRequest;
import com.example.api.models.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ConduitApi {
    @GET("articles")
    Call<MultipleArticle> getArticles();

    @GET("articles")
    Call<MultipleArticle> getArticlesByTag(
            @Query("tag") String tag
    );
    @GET("articles")
    Call<MultipleArticle> getArticlesByAuthor(
            @Query("author") String author
    );
    @GET("articles")
    Call<MultipleArticle> getArticlesByFab(
            @Query("favorited") String favorited
    );

    @POST("users")
    Call<UserResponse> createUser(@Body SignUpRequest user);

    @POST("users/login")
    Call<UserResponse> authenticate(@Body Login user);
}
