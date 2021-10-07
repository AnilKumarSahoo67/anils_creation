package com.example.anilscreation.WeatherCall;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("data/2.5/weather")
    Call<WeatherInfo> getWeatherInfo(@Query("q") String city, @Query("AppID") String appId);

}
