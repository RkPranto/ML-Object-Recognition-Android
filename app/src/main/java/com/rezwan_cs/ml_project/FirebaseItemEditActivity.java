package com.rezwan_cs.ml_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FirebaseItemEditActivity extends AppCompatActivity {
    ImageModel imageModel;

    EditText mTitle, mDetectionText;
    ImageView mImage;
    Button mCancel, mSave;
    Bitmap bitmap = null;
    Uri uri = null;

    String title, detection, type = "";

    StorageReference storageRef;
    String name;
    StorageReference uploadRef;
    FirebaseDatabase database;
    DatabaseReference mDbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_item_edit);

        mTitle = findViewById(R.id.et_title);
        mDetectionText  = findViewById(R.id.et_detection_text);
        mImage = findViewById(R.id.imageView);
        mCancel = findViewById(R.id.btn_cancel);
        mSave = findViewById(R.id.btn_save);


        if(getIntent().getExtras() != null){
            imageModel = (ImageModel) getIntent().getSerializableExtra(Constants.FIREBASE_DATA_ITEM);
        }

        setImageToUI();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("images").child(imageModel.getId());
                imageModel.setName(mTitle.getText().toString().trim());
                imageModel.setDetectionText(mDetectionText.getText().toString().trim());
                mRef.setValue(imageModel);
                Toast.makeText(FirebaseItemEditActivity.this, "Updated", Toast.LENGTH_SHORT).show();

                goToImageListActivity();
            }
        });

    }

    private void goToImageListActivity() {
        finish();
        startActivity(new Intent(FirebaseItemEditActivity.this, ImageListActivity.class));
    }

    private void setImageToUI() {
        mTitle.setText(imageModel.getName());
        mDetectionText.setText(imageModel.getDetectionText());
        mTitle.setSelection(mTitle.getText().length());
        mDetectionText.setSelection(mDetectionText.getText().length());
        Picasso.get().load(imageModel.getImageLink()).into(mImage);
    }
}