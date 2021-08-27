package com.simon.qrscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import javax.security.auth.callback.Callback;

public class ScannerActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector barcodeDetector;
    private Button button;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        getSupportActionBar().hide();
        surfaceView = (SurfaceView) findViewById(R.id.cameraT);
        textView = (TextView) findViewById(R.id.DescText);
        button = findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);
        webView = (WebView) findViewById(R.id.theView);




        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector).setRequestedPreviewSize(640,480).build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA )!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                try {
                    cameraSource.start(holder);

                }catch (IOException e){
                    Toast.makeText(ScannerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode = detections.getDetectedItems();
                if (qrCode.size() != 0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            final String st = qrCode.valueAt(0).displayValue;
                        textView.setText(st);
                            //Toast.makeText(ScannerActivity.this, st, Toast.LENGTH_SHORT).show();
                           if (st.contains("http")){
                               button.setVisibility(View.VISIBLE);
                               button.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                        if (checkInternet() == true){
                                            webView.setVisibility(View.VISIBLE);
                                            surfaceView.setVisibility(View.INVISIBLE);
                                            WebSettings webSettings = webView.getSettings();
                                            webSettings.setJavaScriptEnabled(true);
                                            //webView.setWebViewClient(new Callback());
                                            webSettings.setSupportMultipleWindows(true);
                                            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                                            webView.loadUrl(st);
                                            button.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_SHORT).show();
                                        }






                                   }
                               });

                           }


                        }
                    });
                }
            }
        });

    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true){
            return  true;
        }
        else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}