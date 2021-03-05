package com.rezwan_cs.ml_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetectionTextShowerActivity extends AppCompatActivity {
    TextView mTitle, mDetectionText;
    ImageView mImage;
    Button mEdit;

    String title, detection, type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6);
        mTitle = findViewById(R.id.tv_title);
        mDetectionText = findViewById(R.id.tv_detection_text);
        mEdit = findViewById(R.id.btn_edit);
        mImage = findViewById(R.id.imageView);


        setImageAndText();





        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetectionTextShowerActivity.this, DetectionEditorActivity.class);
                if(type.equals(Constants.URI_)){
                    intent.putExtra(Constants.PASSED_TYPE, Constants.URI_);
                    intent.putExtra(Constants.URI_, getIntent().getExtras().getString(Constants.URI_));
                }
                else if(type.equals(Constants.BITMAP_)){
                    intent.putExtras(getIntent().getExtras());
                }
                intent.putExtra(Constants.TITLE, title);
                intent.putExtra(Constants.DETECTION_SCORE, detection);
                startActivity(intent);
            }
        });
    }

    private void setImageAndText() {
        if(getIntent().getExtras() != null){
            title = getIntent().getExtras().getString(Constants.TITLE);
            detection = getIntent().getExtras().getString(Constants.DETECTION_SCORE);

            mTitle.setText(title);
            mDetectionText.setText(detection);


            if(getIntent().getExtras().getString(Constants.PASSED_TYPE) != null && getIntent().getExtras().getString(Constants.PASSED_TYPE).equals(Constants.URI_)){
                type = Constants.URI_;
                Uri uri = Uri.parse(getIntent().getExtras().getString(Constants.URI_));
                mImage.setImageURI(uri);
            }
            else{
                Log.d(getClass().getName(), "Bitomap");
                type = Constants.BITMAP_;
                Bitmap bitmap = (Bitmap) getIntent().getExtras().get("data");
                mImage.setImageBitmap(bitmap);
            }
        }
    }
}