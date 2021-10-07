package com.example.anilscreation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anilscreation.DBAccess.DatabaseHelper;
import com.example.cropper.CropImage;
import com.example.cropper.CropImageView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText txtName,txtNumber,txtPassword,txtEmail,txtAddress;
    TextInputLayout emailError, passError,nameError,addressError,numError;
    ImageView imgProfile;
    TextView loginLink;
    Button btnSignup,btnImage;
    String imageStr;
    private static final String PREF="myPref";
    SharedPreferences sharedPreferences;
    private static final int READ_REQUEST_CODE = 44;
    DatabaseHelper dataBaseHelper;
    private String stringEncoded;
    private static final int THUMBNAIL_SIZE = 700;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loginLink=findViewById(R.id.LoginLink);
        txtName=findViewById(R.id.nameSignup);
        txtNumber=findViewById(R.id.numSignUp);
        txtPassword=findViewById(R.id.passwordSignup);
        loginLink=findViewById(R.id.LoginLink);
        txtAddress=findViewById(R.id.addressSignup);
        txtEmail=findViewById(R.id.emailSignup);
        imgProfile=findViewById(R.id.imageProfile);
        ActionBar actionBar=getSupportActionBar();


        dataBaseHelper=new DatabaseHelper(this);
        actionBar.setTitle("User Registration");
//        ImageView imgBack=findViewById(R.id.backbutton);
//        imgBack.setVisibility(View.GONE);
//        TextView textView=findViewById(R.id.headersName);
//        textView.setText("Register User");
        emailError=findViewById(R.id.emailErrorSignup);
        passError=findViewById(R.id.passErrorSignup);
        nameError=findViewById(R.id.nameErrorName);
        addressError=findViewById(R.id.addressErrorSignup);
        numError=findViewById(R.id.numErrorSignup);
        btnSignup = findViewById(R.id.register);
        btnImage=findViewById(R.id.selectImage);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                finish();
            }
        });
        btnImage.setOnClickListener(this);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressBar = new ProgressDialog(RegistrationActivity.this);
                progressBar.setMessage("Submit...");
                progressBar.setCancelable(false);
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setCancelable(false);

                if (!validate()) {
                    return;
                } else {
                    progressBar.show();
                    boolean isInsert = dataBaseHelper.insertRegData(txtName.getText().toString().trim(),
                            txtEmail.getText().toString().trim(), txtNumber.getText().toString().trim(),
                            txtAddress.getText().toString().trim(), txtPassword.getText().toString().trim(), imageStr);


                    if (isInsert) {
                        sharedPreferences = getSharedPreferences(PREF, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("PROFILE_IMAGE", imageStr);
                        editor.putString("NAME", txtName.getText().toString().trim());

                        editor.apply();
                        progressBar.dismiss();
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegistrationActivity.this);
                        builder
                                .setTitle("Success")
                                .setMessage("User created successfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                })

                                .show();
                    } else {
                        progressBar.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validate() {
        if(txtName.getText().toString().equals("")){
            nameError.setError("Enter Valid Name");
            return false;
        }
        else if(txtEmail.getText().toString().equals("")){
            emailError.setError("Enter Email");
            return false;
        }
        else if(!isValidEmail(txtEmail.getText().toString().trim())){
            emailError.setError("Enter Valid Email");
            return false;
        }
        else if(txtNumber.getText().toString().equals("")){
            numError.setError("Enter 10 digit number");
            return false;
        }

        else if(txtAddress.getText().toString().equals("")){
            addressError.setError("Enter Address");
            return false;
        }
        else if(txtPassword.getText().toString().equals("")){
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap=getThumbnail(result.getUri());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    imageStr =Base64.encodeToString(stream.toByteArray(),0);

                    imgProfile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File file=new File(String.valueOf(result.getUri()));
                String strFileName = file.getName();
                long length=file.length();
                Toast.makeText(this, "Successfully cropped", Toast.LENGTH_SHORT).show();

            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }



    private Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectImage:
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
                break;
        }
    }
}