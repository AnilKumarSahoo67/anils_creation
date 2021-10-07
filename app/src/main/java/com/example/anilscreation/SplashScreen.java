package com.example.anilscreation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.anilscreation.Connectivity.Connection;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());

        ImageView image= (ImageView) findViewById(R.id.imageView);
        TextView txtTitle=findViewById(R.id.appName);


        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.myanim);
        txtTitle.setAnimation(myanim);
        image.setAnimation(myanim);

        if (!Connection.isConnected(SplashScreen.this)) {
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(SplashScreen.this).create();
            alertDialog.setTitle("Alert!");
            alertDialog.setMessage("Please turn on Internet Connection.");
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        startActivity(new Intent(
                                Settings.ACTION_WIFI_SETTINGS));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            alertDialog.show();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                    finish();
                }
            },3000);
        }
    }
}