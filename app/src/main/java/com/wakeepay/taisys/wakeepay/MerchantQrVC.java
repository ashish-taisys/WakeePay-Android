package com.wakeepay.taisys.wakeepay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MerchantQrVC extends Activity {
    public TextView MerchId;
    ImageView imageView;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String mypreference = "wakeepref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_qr_vc);
        imageView = (ImageView) findViewById(R.id.qrimage);
        MerchId = (TextView) findViewById(R.id.MerchantId);

        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

//        Log.i("Result Merchant Id",sharedpreferences.getString("MerchID",null));

        String text = sharedpreferences.getString("MerchID",null);

        MerchId.setText("Merchant Id:-" + text);

        if (text != null){
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = null;
                bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }
    public void onBackPressed()
    {
        this.startActivity(new Intent(MerchantQrVC.this, MerchantDashVC.class));
        finish();
    }
}
