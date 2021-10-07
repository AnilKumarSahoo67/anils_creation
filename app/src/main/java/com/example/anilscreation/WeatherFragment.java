package com.example.anilscreation;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anilscreation.WeatherCall.ServiceGenerator;
import com.example.anilscreation.WeatherCall.WeatherInfo;
import com.example.anilscreation.WeatherCall.WeatherService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherFragment extends Fragment {
    public final  String AppID="ebfcac32bda131ed5a160f2757938396";
    WeatherService weatherService;

    EditText cityName;
    ProgressDialog mprogressdialog;
    TextView latitude,longitude,country,description,maxTemp,minTemp,name,humid,speed,temp,sunrise,sunset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_weather, container, false);
        Button search=v.findViewById(R.id.button);
        latitude=v.findViewById(R.id.latitudeTextView);
        longitude=v.findViewById(R.id.longitudeTextView);
        country=v.findViewById(R.id.textViewCountry);
        description=v.findViewById(R.id.textViewDescription);
        maxTemp=v.findViewById(R.id.textViewMaxTemp);
        minTemp=v.findViewById(R.id.textViewMinTemp);
        name=v.findViewById(R.id.textViewName);
        humid=v.findViewById(R.id.textViewHumidity);
        speed=v.findViewById(R.id.textViewSpeed);
        temp=v.findViewById(R.id.textViewTemp);
        cityName=v.findViewById(R.id.cityName);
        sunrise=v.findViewById(R.id.textViewSunRise);
        sunset=v.findViewById(R.id.textViewSunSet);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultData();
            }
        });
        return v;


    }

    private void resultData() {


        mprogressdialog=new ProgressDialog(getContext());
        mprogressdialog.setMessage("Searching...");
        mprogressdialog.setCancelable(false);


        if (cityName.getText().toString().trim().length()==0){
            cityName.setError("Enter city name");
            cityName.requestFocus();
            return;
        }
        String city=cityName.getText().toString().trim();
//        weatherService=ServiceGenerator.getClient().create(WeatherService.class);
        final Call<WeatherInfo> weatherInfoCall= ServiceGenerator.getClient().create(WeatherService.class).getWeatherInfo(city,AppID);
        showProgressDialog(true);
        weatherInfoCall.enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                WeatherInfo info=response.body();
                showProgressDialog(false);
                onDisplayWeatherInfo(info);
                mprogressdialog.dismiss();
            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                showProgressDialog(false);
                Toast.makeText(getContext(),"Fail to get Weather Info", Toast.LENGTH_SHORT).show();
                mprogressdialog.dismiss();
            }
        });
    }

    private void onDisplayWeatherInfo(WeatherInfo body) {
        if (body!=null){
            latitude.setText(String.valueOf(body.getCoord().getLat()));
            longitude.setText(String.valueOf(body.getCoord().getLon()));

            country.setText(String.valueOf(body.getSys().getCountry()));
            description.setText(String.valueOf(body.getWeather().get(0).getDescription()));

            temp.setText(String.valueOf(body.getMain().getTemp()));
            humid.setText(String.valueOf(body.getMain().getHumidity()));
            minTemp.setText(String.valueOf(body.getMain().getTempMin()));
            maxTemp.setText(String.valueOf(body.getMain().getTempMax()));

            speed.setText(String.valueOf(body.getWind().getSpeed()));
            name.setText(String.valueOf(body.getName()));
            sunrise.setText(String.valueOf(body.getSys().getSunrise()));
            sunset.setText(String.valueOf(body.getSys().getSunset()));
        }
    }

    private void showProgressDialog(boolean b) {
        if (b)
        {
            if (!mprogressdialog.isShowing())
                mprogressdialog.show();
        }else {
            if (!mprogressdialog.isShowing())
                mprogressdialog.dismiss();
        }
    }
}