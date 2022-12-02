package com.example.qrscanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenerateCodeActivity extends AppCompatActivity {

    EditText valueView;
    Button generateQRButton, saveQRButton, shareQRButton;
    ImageView qrCode;
    TextView saveView, shareView;

    Bitmap qrBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_code);

        //Method call to assign ID's to variables
        assignIds();

//        To hide views when the QR code is not generated yet
        saveQRButton.setVisibility(View.GONE);
        shareQRButton.setVisibility(View.GONE);
        saveView.setVisibility(View.GONE);
        shareView.setVisibility(View.GONE);
        qrCode.setVisibility(View.GONE);

        generateQRButton.setOnClickListener(view -> {
            String data = valueView.getText().toString();

            hideKeyboard();

            if (!data.isEmpty()) {

                QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 600);

                qrBitmap = qrgEncoder.getBitmap();
                qrCode.setImageBitmap(qrBitmap);

                Toast.makeText(GenerateCodeActivity.this, "QR Code generated", Toast.LENGTH_SHORT).show();

                // To show the buttons when the QR Code is generated
                saveQRButton.setVisibility(View.VISIBLE);
                shareQRButton.setVisibility(View.VISIBLE);
                saveView.setVisibility(View.VISIBLE);
                shareView.setVisibility(View.VISIBLE);
                qrCode.setVisibility(View.VISIBLE);

            } else {
                // If nothing is entered in value field
                valueView.setError("Enter a value");

                Toast.makeText(GenerateCodeActivity.this, "Enter a value", Toast.LENGTH_LONG).show();
            }

        });

        saveQRButton.setOnClickListener((view) -> {
            if (qrCode != null) {
                MediaStore.Images.Media.insertImage(getContentResolver(), qrBitmap, "QR_code_" + System.currentTimeMillis(), null);

                Toast.makeText(GenerateCodeActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
            }

        });

        shareQRButton.setOnClickListener(view -> {
            if (qrCode != null) {
                //To share the QR Code a image
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, getImageUri(GenerateCodeActivity.this, qrBitmap));
                startActivity(Intent.createChooser(share, "Share via"));
            } else {
                Toast.makeText(GenerateCodeActivity.this, "Invalid QR", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void hideKeyboard() {
        // to hide the keyboard after the user has finished typing and pressed generate button

        if (getCurrentFocus() != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private Uri getImageUri(@NonNull Context inContext, @NonNull Bitmap inImage) {
        //To convert image into a Uri to share using intent
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void assignIds() {
        valueView = findViewById(R.id.valueViewID);

        generateQRButton = findViewById(R.id.generateButtonID);
        saveQRButton = findViewById(R.id.saveButtonID);
        shareQRButton = findViewById(R.id.shareQRButtonID);

        qrCode = findViewById(R.id.qrImage);

        saveView = findViewById(R.id.saveQr);
        shareView = findViewById(R.id.shareQr);
    }
}