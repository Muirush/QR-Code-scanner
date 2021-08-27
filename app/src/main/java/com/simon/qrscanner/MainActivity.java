package com.simon.qrscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static  final  int CAMERA_PERMISSION_CODE = 100;

    private Button  camera, generate, scanBt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermision(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        getSupportActionBar().hide();

//        camera = (Button) findViewById(R.id.camera);
//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // checkPermision(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
//            }
//        });
        generate = (Button) findViewById(R.id.generate);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GenerateCode.class);
                startActivity(intent);
            }
        });
        scanBt = (Button) findViewById(R.id.scan);
        scanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });

    }
    public  void checkPermision(String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission ) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{permission}, requestCode);
        }
        else {
           // Toast.makeText(this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
            //camera.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }

    }
}