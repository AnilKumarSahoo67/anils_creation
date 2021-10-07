package com.example.anilscreation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anilscreation.Slider.SliderMainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HomeFragment extends Fragment {
    TextView txtWish;
    ImageView imgState;
    Button btnClick;
    Calendar c = Calendar.getInstance();
    SharedPreferences preferences;
    private static final String PREF="myPref";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        txtWish=v.findViewById(R.id.wishText);
        imgState=v.findViewById(R.id.callState);
        btnClick=v.findViewById(R.id.btn_click);
        preferences=getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String name=preferences.getString("USER_NAME",null);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String subStrHr=formattedDate.substring(0,2);
        String subStrMin=formattedDate.substring(3,5);
        int hr=Integer.parseInt(subStrHr);
        int min=Integer.parseInt(subStrMin);
        if(isAfter(hr,min,0,0)&& isBefore(hr,min,11,59)){
            txtWish.setText("Good Morning"+", "+name);
        }else if(isAfter(hr,min,12,0)&& isBefore(hr,min,16,59)){
            txtWish.setText("Good Afternoon"+", "+name);
        }else {
            txtWish.setText("Good Evening"+", "+name);
        }
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SliderMainActivity.class));
            }
        });
        return v;
    }

    private boolean isBefore(int currentHr,int currentMin, int hr,int min) {
        return currentHr<=hr && currentMin<=min?true:false;
    }

    private boolean isAfter(int currentHr,int currentmin, int hr,int min) {
        return currentHr>=hr && currentmin>=min?true:false ;
    }

}