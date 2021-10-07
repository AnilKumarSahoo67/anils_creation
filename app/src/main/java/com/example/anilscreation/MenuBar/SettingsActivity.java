package com.example.anilscreation.MenuBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anilscreation.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    SwitchCompat switchCompat;
    CircleImageView circleImageView;
    TextView txtName,txtEmail;
    private static final String PREF="myPref";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    boolean isEnable=false;
    String image,name,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        switchCompat=findViewById(R.id.switchCompact);
        circleImageView=findViewById(R.id.settingsDp);
        txtName=findViewById(R.id.settingsName);
        txtEmail=findViewById(R.id.settingsMail);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Settings");
        preferences=getSharedPreferences(PREF,MODE_PRIVATE);
        boolean setEnable=preferences.getBoolean("IS_ENABLE",false);
        email=preferences.getString("EMAIL",null);
        name=preferences.getString("USER_NAME",null);
        image=preferences.getString("USER_PROFILE",null);
        if (image!=null){
            Bitmap bitmap = strToBitmap(image);
            circleImageView.setImageBitmap(bitmap);
            txtName.setText(name);
            txtEmail.setText(email);
        }else {
            Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();
        }
        if (setEnable){
            switchCompat.setChecked(true);
        }else {
            switchCompat.setChecked(false);
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isEnable=true;
                    editor=preferences.edit();
                    editor.putBoolean("IS_ENABLE",isEnable);
                    editor.commit();
                }else {
                    isEnable=false;
                    editor=preferences.edit();
                    editor.putBoolean("IS_ENABLE",isEnable);
                    editor.commit();
                }
            }
        });
    }

    private Bitmap strToBitmap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
            InputStream inputStream = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}