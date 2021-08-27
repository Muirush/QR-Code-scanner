package com.simon.qrscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

public class GenerateCode extends AppCompatActivity {
    public static  final  int QRCodeWidth = 500;
    Bitmap bitmap;
    EditText text;
    Button generate, download;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_code);
        getSupportActionBar().hide();
        text = (EditText) findViewById(R.id.text);
        generate = (Button) findViewById(R.id.generate);
        download = (Button) findViewById(R.id.download);
        download.setVisibility(View.INVISIBLE);
        imageView = (ImageView) findViewById(R.id.image);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().length()== 0){
                    //Toast.makeText(GenerateCode.this, "Enter text to be associated with the QR Code", Toast.LENGTH_SHORT).show();
                    text.setError("Cannot be empty");
                }
                else {
                    try {
                        bitmap =textToImageEncoder(text.getText().toString());
                        imageView.setImageBitmap(bitmap);
                        download.setVisibility(View.VISIBLE);
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Code_Scanner",null);
                                Toast.makeText(GenerateCode.this, "Saved in gallery", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (WriterException we){
                        Toast.makeText(GenerateCode.this, we.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private  Bitmap textToImageEncoder(String value) throws WriterException{
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE, QRCodeWidth,QRCodeWidth, null);


        }
        catch (IllegalArgumentException e){
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++){
            int offSet = y*bitMatrixWidth;
            for (int x = 0; x <bitMatrixWidth; x++){
                pixels[offSet + x] = bitMatrix.get(x,y)? getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight,Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels,0,500,0,0,bitMatrixWidth,bitMatrixHeight);
        return bitmap;


    }
}











