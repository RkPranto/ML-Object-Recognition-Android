package com.rezwan_cs.ml_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.io.IOException;
import java.util.List;

public class ImageChooserActivity extends AppCompatActivity {
    String tapped = "";
    ImageView mImage;
    Button mCapture, mLoad;
    Integer REQUEST_CODE_LOAD = 101, REQUEST_CODE_CAPTURE = 201;
    Intent i = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        i  = new Intent(this, DetectionTextShowerActivity.class);

        if(getIntent() != null){
            tapped = getIntent().getStringExtra("tap");
            Log.d(getClass().getName(), "Tapped: " + tapped);
        }

        findViews();
        setUpTheImage();

        mLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Image"), REQUEST_CODE_LOAD);
            }
        });

        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == REQUEST_CODE_LOAD && data != null){
            Uri uri = data.getData();

            i.putExtra(Constants.PASSED_TYPE, Constants.URI_);
            i.putExtra(Constants.URI_, uri.toString());

            FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(this, data.getData());
                labelDetectionWithFirebaseImage(image);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (requestCode == REQUEST_CODE_CAPTURE && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            //TODO: do
            i.putExtras(data);

            FirebaseVisionImage image;
            image = FirebaseVisionImage.fromBitmap(bitmap);
            labelDetectionWithFirebaseImage(image);

            //mImage.setImageBitmap(image);
            Log.d(getClass().getName(), "CAPUTER");
        }

    }

    private void labelDetectionWithFirebaseImage(FirebaseVisionImage image) {
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getOnDeviceImageLabeler();
        labeler.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        // Task completed successfully
                        // ...
                        String x = tapped+ " : ";
                        for (FirebaseVisionImageLabel label: labels) {
                            String text = label.getText();
                            //String entityId = label.getEntityId();
                            float confidence = label.getConfidence();
                            x+= "Detected Item: "+ text+ " , Detected Score: "+ confidence+"\n";
                            i.putExtra(Constants.TITLE, text);
                            i.putExtra(Constants.DETECTION_SCORE, x);
                            break;
                        }
                        startActivity(i);
                        // ((TextView)findViewById(R.id.tv_view)).setText(x);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        Log.d(getClass().getName(),e.getMessage());
                        ((TextView)findViewById(R.id.tv_view)).setText(e.getMessage()+"");
                    }
                });
    }

    private void findViews() {
        mImage = findViewById(R.id.iv_image);
        mCapture = findViewById(R.id.btn_capture);
        mLoad = findViewById(R.id.btn_load);
    }

    private void setUpTheImage() {
        if(tapped.equals(Constants.FIREBASE_ML)){
            mImage.setImageResource(R.drawable.ml_kit_image);
        }
        else{
            mImage.setImageResource(R.drawable.ibm_ml_image);
        }
    }
}