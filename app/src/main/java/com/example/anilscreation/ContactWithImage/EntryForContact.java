package com.example.anilscreation.ContactWithImage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.anilscreation.MainActivity;
import com.example.anilscreation.R;
import com.example.cropper.CropImage;
import com.example.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class EntryForContact extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    EditText txtName,txtContact;
    Button btnUpload,btnSubmit;
    String imageStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_for_contact);
        btnUpload=findViewById(R.id.btnUpload);
        btnSubmit=findViewById(R.id.submit);
        txtName=findViewById(R.id.name);
        txtContact=findViewById(R.id.number);
        imageView=findViewById(R.id.imageView);
        btnSubmit.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                Intent intent=new Intent();
                if (!validate()){
                   return;
                }else {
                    intent.putExtra("Image", imageStr);
                    intent.putExtra("Name", txtName.getText().toString().trim());
                    intent.putExtra("Number", txtContact.getText().toString().trim());
                    setResult(5000, intent);
                }
                finish();
                break;
            case R.id.btnUpload:
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(EntryForContact.this);
                break;
            default:
                break;
        }
    }

    private boolean validate() {
        if (txtName.getText().toString().trim().length()<1){
            txtName.setError("Enter Name");
            return false;
        }else if (txtContact.getText().toString().trim().length()<1){
            txtContact.setError("Enter Number");
            return false;
        }else if (imageStr==null ||imageStr.length()<1){
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = getThumbnail(result.getUri());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    imageStr = Base64.encodeToString(stream.toByteArray(), 0);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private Bitmap getThumbnail(Uri uri) throws IOException {
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

        double ratio = (originalSize > 700) ? (originalSize / 700) : 1.0;

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
}