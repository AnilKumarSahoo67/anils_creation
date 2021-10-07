package com.example.anilscreation;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.anilscreation.DBAccess.DatabaseHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText edtemail, edtPassword;
    Button login;
    TextView register;
    TextInputLayout emailError, passError;
    DatabaseHelper dataBaseHelper;
    private static final String PREF="myPref";
    private static  String IS_LOGIN="is_login";
    SharedPreferences sharedPreferences;
    UserDetails userDetails;
    String[] permissions = new String[]{
            // Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtemail = (EditText) findViewById(R.id.email);
        edtPassword = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        emailError = (TextInputLayout) findViewById(R.id.emailError);
        passError = (TextInputLayout) findViewById(R.id.passError);
        ActionBar actionBar=getSupportActionBar();
        dataBaseHelper=new DatabaseHelper(this);
        actionBar.setTitle("User Login");
        checkPermissions();
        sharedPreferences=getSharedPreferences(PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        if (sharedPreferences.contains(IS_LOGIN) && sharedPreferences.contains("EMAIL")){
            String email=sharedPreferences.getString("EMAIL",null);
            userDetails= dataBaseHelper.getAllUserData(email);
            MainActivity.userDetails=userDetails;
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                    finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()){
                    return;
                }else{
                    userDetails= dataBaseHelper.getAllUserData(edtemail.getText().toString().trim());
                    if (userDetails != null) {
                        boolean checkPass=dataBaseHelper.checkPass(edtemail.getText().toString().trim(),
                                edtPassword.getText().toString().trim());

                        if (checkPass){
                            MainActivity.userDetails=userDetails;
                            editor.putBoolean(IS_LOGIN,true);
                            editor.putString("EMAIL",edtemail.getText().toString().trim());
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermission=new ArrayList<>();
        for (String s: permissions){
            result= ContextCompat.checkSelfPermission(getApplicationContext(),s);
            if (result!= PackageManager.PERMISSION_GRANTED){
                listPermission.add(s);
            }
        }
        if (!listPermission.isEmpty()){
            ActivityCompat.requestPermissions(this,listPermission.toArray(new String[listPermission.size()]),5000);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 5000:
                if (grantResults.length>0){
                    String permissionDenied="";
                    for (String s: permissions){
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                            permissionDenied+="\n"+s;
                        }
                    }
                }
                return;
        }
    }

    private boolean validate() {
        if(!isValidEmail(edtemail.getText().toString().trim())){
            emailError.setError("Enter Valid Email");
            return false;
        }
        else if(edtemail.getText().toString().equals("")){
            emailError.setError("Enter Email");
            return false;
        }
        else if(edtPassword.getText().toString().equals("")){
            passError.setError("Enter Password");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

        Pattern pat = Pattern.compile(regex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}