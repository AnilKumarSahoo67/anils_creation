package com.example.anilscreation.BluetoothPrinter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anilscreation.R;

import java.util.ArrayList;
import java.util.List;

public class PrinterMainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSION = 10;
    Button btn_print,btn_scan,btn_disable,btn_choose;
    TextView TxtWarning;
    String target;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_main);
        btn_choose=findViewById(R.id.btn_choosefile);
        btn_disable=findViewById(R.id.btn_disable);
        btn_print=findViewById(R.id.btn_print);
        btn_scan=findViewById(R.id.scan_printer);
        TxtWarning=findViewById(R.id.textView_printerWarning);
        btn_scan.setOnClickListener(this);
        btn_print.setOnClickListener(this);
        btn_disable.setOnClickListener(this);
        btn_choose.setOnClickListener(this);
        requestRuntimePermission();

    }

    private void requestRuntimePermission() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return;
        }
        int permissionStorage= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionLocation= ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> requestPermission=new ArrayList<>();
        if (permissionStorage== PackageManager.PERMISSION_DENIED){
            requestPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionLocation==PackageManager.PERMISSION_DENIED){
            requestPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!requestPermission.isEmpty()){
            ActivityCompat.requestPermissions(this,requestPermission.toArray(new String[requestPermission.size()]),REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode!=REQUEST_PERMISSION || grantResults.length==0){
            return;
        }
        List<String> requestPermissions = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(permissions[i]);
            }
            if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                    && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(permissions[i]);
            }
        }

        if (!requestPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), REQUEST_PERMISSION);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_print:

                break;
            case R.id.btn_choosefile:

                break;
            case R.id.scan_printer:
                Intent intent=new Intent(this,EPSON_ListAdapter.class);
                startActivityForResult(intent,0);
                break;
            case R.id.btn_disable:

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null && requestCode==0 && resultCode==RESULT_OK){
            target=data.getStringExtra("Target");
            if (target!=null){
                Toast.makeText(this,target+" is Connected" ,Toast.LENGTH_LONG).show();
            }
        }
    }
}