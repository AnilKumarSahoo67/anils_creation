package com.example.anilscreation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;
import com.example.anilscreation.BluetoothPrinter.PrinterMainActivity;
import com.example.anilscreation.Contact.AddStudentDetailsActivity;
import com.example.anilscreation.Contact.AddStudentToDB;
import com.example.anilscreation.ContactWithImage.ContactImageMain;
import com.example.anilscreation.DBAccess.DatabaseHelper;
import com.example.anilscreation.GoogleMap.Permission;
import com.example.anilscreation.MenuBar.SettingsActivity;
import com.example.anilscreation.gitHubUser.GitHubUserFragment;
import com.example.anilscreation.gitHubUser.model.Project;
import com.example.anilscreation.gitHubUser.SingleUserFragment;
import com.example.cropper.CropImage;
import com.example.cropper.CropImageView;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BiometricCallback {
    private static final int THUMBNAIL_SIZE =700 ;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Toolbar toolbar;
    static UserDetails userDetails;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private static final String PREFRENCES_NAME = "myPref";
    DatabaseHelper databaseHelper;
    SharedPreferences preferences;
    String email="";
    ImageView imgvw;
    boolean isAuth=false;
    int downloaddFlag=1;
    BiometricManager biometricManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        preferences = getSharedPreferences(PREFRENCES_NAME, MODE_PRIVATE);
        email = preferences.getString("EMAIL", null);
        isAuth = preferences.getBoolean("IS_ENABLE", false);
        databaseHelper=new DatabaseHelper(this);
        View hView = navigationView.inflateHeaderView(R.layout.drawer_header);
        imgvw = (CircleImageView) hView.findViewById(R.id.drawerImg);
        TextView tv = (TextView) hView.findViewById(R.id.drawerName);
        TextView tv1 = (TextView) hView.findViewById(R.id.drawerGmail);
//        Bitmap bitmap = strToBitmap(userDetails.getImage());
        imgvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
            }
        });

//        imgvw.setImageBitmap(bitmap);
//        tv.setText(userDetails.getName());
        tv1.setText(email);

        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("USER_NAME", userDetails.getName());
//        editor.putString("USER_PROFILE", userDetails.getImage());
        editor.apply();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.commit();

        if (isAuth){
            biometricManager=new BiometricManager.BiometricBuilder(MainActivity.this)
                    .setTitle("Autenticate User")
                    .setSubtitle("Finger print auth")
                    .setDescription("Place your finger to access this app")
                    .setNegativeButtonText("Cancel")
                    .build();

            biometricManager.autheenticate((BiometricCallback) MainActivity.this);
        }
        if (downloaddFlag==1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("Update Available !!")
                    .setMessage("Dear user new update is available " +
                            "Are you sure you want to download new update ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DownloadTask downloadTask=new DownloadTask(MainActivity.this);
                            downloadTask.execute("https://image.tmdb.org/t/p/original//8yhtzsbBExY8mUct2GOk4LDDuGH.jpg");
                        }
                    })
                    .setNegativeButton("No", null)                        //Do nothing on no
                    .show();
        }
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
                fragmentTransaction.commit();
                break;
            case R.id.nav_weather:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new WeatherFragment());
                fragmentTransaction.commit();
                break;
            case R.id.nav_slideshow:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new MovieFragment());
                fragmentTransaction.commit();
                break;
            case R.id.gitHubUser:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,new GitHubUserFragment());
                fragmentTransaction.commit();
                break;


            case R.id.nav_Contact:
//                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, AddStudentToDB.class));
                break;
            case R.id.nav_Contact_act:
//                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, AddStudentDetailsActivity.class));
                break;

            case R.id.location:
                startActivity(new Intent(MainActivity.this, Permission.class));
                break;
            case R.id.image:
                startActivity(new Intent(MainActivity.this, ContactImageMain.class));
                break;
            case R.id.printer:
                startActivity(new Intent(MainActivity.this, PrinterMainActivity.class));
                break;

        }
        return false;
    }
    public void show(Project project){
//        SingleUserFragment  singleUserFragment=SingleUserFragment.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
            case R.id.setting:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder
                .setTitle("Are you sure ?")
                .setMessage("You Want To Exit App")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainActivity.this.finish();
                        startActivity(startMain);
                    }
                })
                .setNegativeButton("No", null)                        //Do nothing on no
                .show();
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
                    String imageStr = Base64.encodeToString(stream.toByteArray(), 0);
                    boolean updateProfilePic=databaseHelper.UpdateProfilePic(imageStr,userDetails.getID());
                    if (updateProfilePic){
                        imgvw.setImageBitmap(bitmap);
                        Toast.makeText(this, "Profile pic updated", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "Profile pic not updated", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File file = new File(String.valueOf(result.getUri()));
                String strFileName = file.getName();
                long length = file.length();


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
    public void onSdkVersionNotSupported() {
        Toast.makeText(getApplicationContext(), "SDK Version not Supported", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        Toast.makeText(getApplicationContext(), "Device doesn't support biometric", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        Toast.makeText(getApplicationContext(), "Fingerprint is not registered", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
//        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationCancelled() {
        Toast.makeText(getApplicationContext(), "Authentication cancelled", Toast.LENGTH_LONG).show();
        biometricManager.cancelAuthentication();
        this.finish();
    }

    @Override
    public void onAuthenticationSuccessful() {
        Toast.makeText(getApplicationContext(), "Fingerprint verified", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
//        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
    }

    private class DownloadTask extends AsyncTask<String, Integer,String> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;
        ProgressDialog progressBar;
        int downloadedSize = 0;
        int totalSize = 0;
        File file;
        public DownloadTask(Context context) {
            this.context=context;
            progressBar=new ProgressDialog(context);
        }

        @Override
        protected String doInBackground(String... urls) {
            URL url = null;
            try {
                url = new URL(urls[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setDoOutput(true);
                c.connect();

                int len1=0;
                len1=c.getContentLength();

                File file = new File("/storage/emulated/0/Android/data/com.example.anilscreation/files/Download");
                if (!file.exists()){
                    file.mkdirs();
                }
                File outputFile = new File(file, "downloaded_img.jpg");
                if(outputFile.exists()){
                    outputFile.delete();
                }

                InputStream inputStream=new BufferedInputStream(url.openStream(),8192);
//
//                InputStream is = c.getInputStream();
                int total=0;
                int count=0;

                byte[] buffer = new byte[1024];
                OutputStream fos = new FileOutputStream(outputFile);

                while ((count = inputStream.read(buffer)) != -1) {
                    total+=count;
                    fos.write(buffer, 0, count);
                    int progress=(int)total*100/len1;
                    publishProgress(progress);
                }
                fos.close();
                inputStream.close();
                fos.flush();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "Download complete";
        }
        @Override
        protected void onPreExecute() {
            progressBar.setTitle("Download in progress....");
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            progressBar.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressBar.setMessage("Downloading....");
            progressBar.setIndeterminate(true);
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setCancelable(false);
            progressBar.setIndeterminate(false);
            progressBar.setMax(100);
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.hide();
            Uri uri = null;
            if (result == null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("/storage/emulated/0/Android/data/com.example.anilscreation/files/Download/downloaded_img.jpg"), "image/*");
                startActivity(intent);
//                progressBar.dismiss();
//                Intent install = new Intent(Intent.ACTION_VIEW);
//                install.setDataAndType(Uri.fromFile(new File("/storage/emulated/0/Download/app-debug.apk")), "application/vnd.android.package-archive");
////                uri = FileProvider.getUriForFile(MainActivity.this, "com.example.anilscreation.provider", new File("/storage/emulated/0/Android/data/com.example.anilscreation/files/APK/app-debug.apk"));
////                install.setDataAndType(uri, "application/vnd.android.package-archive");
//                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(install);
            }
        }
    }
}