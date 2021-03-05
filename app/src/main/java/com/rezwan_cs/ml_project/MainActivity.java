package com.rezwan_cs.ml_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView mMLKit, mIBMML;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        mIBMML = findViewById(R.id.iv_ibm_watson_ml);
        mMLKit = findViewById(R.id.iv_ml_kit);

        mMLKit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity_3(Constants.FIREBASE_ML);
            }
        });

        mIBMML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity_3(Constants.IBM_WATSON);
            }
        });
    }

    private void goToActivity_3(String str) {

        Intent i = new Intent(this, ImageChooserActivity.class);
        i.putExtra("tap", str);
        startActivity(i);
    }
}