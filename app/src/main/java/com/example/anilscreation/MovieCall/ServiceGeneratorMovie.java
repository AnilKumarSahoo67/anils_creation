package com.example.anilscreation.MovieCall;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ServiceGeneratorMovie {
    public static final String BASE_URL="https://api.themoviedb.org/3/";
    public static final String API_KEY="e6503da7d6f60fc032a4958a91afee62";

    public static AllMovieInterface allMoviesInterface=null;


    public static AllMovieInterface getNowPlayingMovieList(){
        if (allMoviesInterface==null)
        {
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            allMoviesInterface = retrofit.create(AllMovieInterface.class);
        }
        return allMoviesInterface;
    }

    public interface AllMovieInterface {
        @GET("movie/now_playing?api_key="+API_KEY)
        Call<NowPlayingMovie> getNowPlayingMovie();

        @GET("movie/top_rated?api_key="+API_KEY)
        Call<NowPlayingMovie> getTopRatedMovies();
    }
}
