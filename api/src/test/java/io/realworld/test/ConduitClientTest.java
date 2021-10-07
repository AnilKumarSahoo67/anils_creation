package io.realworld.test;

import com.example.api.ConduitClient;
import com.example.api.models.Login;
import com.example.api.models.LoginRequest;
import com.example.api.models.MultipleArticle;
import com.example.api.models.SignUpRequest;
import com.example.api.models.UserResponse;
import com.example.api.models.User;
import com.example.api.services.ConduitApi;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConduitClientTest {
    @Test
    public void article_success() throws IOException {
        final Call<MultipleArticle> articlesCall= ConduitClient.getInstance().getApi().getArticles();
        Response<MultipleArticle> response= articlesCall.execute();
        MultipleArticle articles=response.body();
        assertNotNull(articles);
    }

    @Test
    public void articleByTag() throws IOException {
        final Call<MultipleArticle> articlesCall= ConduitClient.getInstance().getApi().getArticlesByTag("dragons");
        Response<MultipleArticle> response= articlesCall.execute();
        MultipleArticle articles=response.body();
        assertNotNull(articles);
    }
    @Test
    public void articleByAuthor() throws IOException {
        final Call<MultipleArticle> articlesCall= ConduitClient.getInstance().getApi().getArticlesByAuthor("heroooo");
        Response<MultipleArticle> response= articlesCall.execute();
        MultipleArticle articles=response.body();
        assertNotNull(articles);
    }

    @Test
    public void userCreate() throws IOException {
        User user=new User("anil_s_1999","sahooanilkumar1999@gmail.com","aksahoo123");
        SignUpRequest request=new SignUpRequest(user);
        final Call<UserResponse> userCall= ConduitClient.getInstance().getApi().createUser(request);
        Response<UserResponse> response= userCall.execute();
        String res=response.body().toString();
        UserResponse userResponse=response.body();
        assertEquals(user.username,userResponse.username);
    }

    @Test
    public void signIn() throws IOException {
        LoginRequest loginRequest=new LoginRequest("akshayanil.sahoo.101@gmail.com","aksahoo123");
        final Call<UserResponse> userCall= ConduitClient.getInstance().getApi().authenticate(new Login(loginRequest));
        Response<UserResponse> response= userCall.execute();
        UserResponse userResponse=response.body();
        assertEquals(loginRequest.email,userResponse.email);
    }
}
