package com.example.qrscanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class ScanCodeActivity extends AppCompatActivity {

    CodeScannerView codeScannerView;
    CodeScanner codeScanner;

    TextView resultView, shareView, openInBrowserView;
    Button shareButton, openInBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        // Method call to assign ID's
        assignIDs();

        resultView.setVisibility(View.GONE);
        shareView.setVisibility(View.GONE);
        openInBrowserView.setVisibility(View.GONE);

        shareButton.setVisibility(View.GONE);
        openInBrowser.setVisibility(View.GONE);

        codeScanner = new CodeScanner(this, codeScannerView);
        codeScanner.startPreview();

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String resultText = result.getText();

                        if (resultText != null && resultText.length() != 0) {
                            resultView.setVisibility(View.VISIBLE);
                            resultView.setText(resultText);

                            shareView.setVisibility(View.VISIBLE);
                            openInBrowserView.setVisibility(View.VISIBLE);

                            shareButton.setVisibility(View.VISIBLE);
                            openInBrowser.setVisibility(View.VISIBLE);

                            shareButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent();
                                    i.setAction(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    i.putExtra(Intent.EXTRA_TEXT, "Share the result ");
                                    i.putExtra(Intent.EXTRA_TEXT, resultText);

                                    Intent intent = Intent.createChooser(i, "Share using");
                                    startActivity(intent);
                                }
                            });

                            openInBrowser.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(resultText));

                                    startActivity(i);
                                }
                            });

                        } else {
                            resultView.setText("Invalid QR");
                        }

                    }
                });

            }
        });

    }

    private void assignIDs() {
        codeScannerView = findViewById(R.id.codeScannerView);
        resultView = findViewById(R.id.resultView);
        shareButton = findViewById(R.id.shareButtonID);
        openInBrowser = findViewById(R.id.openInBrowser);

        shareView = findViewById(R.id.shareScannedView);
        openInBrowserView = findViewById(R.id.openInBrowserView);
    }
}