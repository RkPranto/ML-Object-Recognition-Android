package com.rezwan_cs.ml_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FirebaseItemShowerActivity extends AppCompatActivity {
    ImageModel imageModel;

    TextView mTitle, mDetectionTxt;
    ImageView imageView;
    Button mDelete, mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_item_shower);

        if(getIntent().getExtras() != null){
            imageModel = (ImageModel) getIntent().getSerializableExtra(Constants.FIREBASE_DATA_ITEM);
            Log.d("FB: ", imageModel.toString());
        }

        findViews();

        setUpDataOnUI();

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("images").child(imageModel.getId());
                mRef.removeValue();
                Toast.makeText(FirebaseItemShowerActivity.this,
                        imageModel.getName()+" is being removed from firebase database", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirebaseItemShowerActivity.this, FirebaseItemEditActivity.class);
                intent.putExtra(Constants.FIREBASE_DATA_ITEM, imageModel);
                startActivity(intent);
            }
        });

    }

    private void findViews() {
        mTitle = findViewById(R.id.tv_title);
        mDetectionTxt = findViewById(R.id.tv_detection_text);
        imageView = findViewById(R.id.imageView);
        mDelete = findViewById(R.id.btn_delete);
        mEdit = findViewById(R.id.btn_edit);
    }

    private void setUpDataOnUI() {
        mTitle.setText(imageModel.getName());
        mDetectionTxt.setText(imageModel.getDetectionText());
        Picasso.get().load(imageModel.getImageLink()).into(imageView);

    }
}