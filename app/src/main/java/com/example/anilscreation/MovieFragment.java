package com.example.anilscreation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anilscreation.MovieCall.NowPlayingMovie;
import com.example.anilscreation.MovieCall.RecyclerViewAdapter;
import com.example.anilscreation.MovieCall.ServiceGeneratorMovie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieFragment extends Fragment implements View.OnClickListener {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    RadioGroup radioGroup;
    RadioButton nowPlaying,topRated;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        radioGroup=v.findViewById(R.id.RadioGroup);
        nowPlaying=v.findViewById(R.id.nowPlayingMovie);
        topRated=v.findViewById(R.id.topRatedMovie);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        nowPlaying.setOnClickListener(this);
        topRated.setOnClickListener(this);

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching data....");
        progressDialog.setCancelable(false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.topRatedMovie:
                fetchTopRatedMovie();
                break;
            case R.id.nowPlayingMovie:
                fetchNowPlayingMovie();
                break;
            default:
                break;
        }
    }

    private void fetchNowPlayingMovie() {
//        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
//        } else {
//            vibrator.vibrate(50);
//        }
        final Call<NowPlayingMovie> nowPlayingMovie= ServiceGeneratorMovie.getNowPlayingMovieList().getNowPlayingMovie();
        progressDialog.show();
        nowPlayingMovie.enqueue(new Callback<NowPlayingMovie>() {
            @Override
            public void onResponse(Call<NowPlayingMovie> call, Response<NowPlayingMovie> response) {
                progressDialog.dismiss();
                NowPlayingMovie movie=response.body();
                recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(),movie.getResults()));
                Toast.makeText(getContext(), "Successfully Loaded\n Now playing movie", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<NowPlayingMovie> call, Throwable t) {
                Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void fetchTopRatedMovie() {
//        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
//        } else {
//            vibrator.vibrate(50);
//        }
        final Call<NowPlayingMovie> nowPlayingMovieCall= ServiceGeneratorMovie.getNowPlayingMovieList().getTopRatedMovies();
        progressDialog.show();
        nowPlayingMovieCall.enqueue(new Callback<NowPlayingMovie>() {
            @Override
            public void onResponse(Call<NowPlayingMovie> call, Response<NowPlayingMovie> response) {
                NowPlayingMovie movie=response.body();
                recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(),movie.getResults()));
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Successfully Loaded \nTop Rated movie", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<NowPlayingMovie> call, Throwable t) {
                Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}