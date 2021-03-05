package com.rezwan_cs.ml_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ImageListActivity extends AppCompatActivity {
    ListView mImageList;
    ArrayList<ImageModel> arrayList = new ArrayList<>();
    ImageAdapter imageAdapter;
    Button mAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        mImageList = findViewById(R.id.lv_image_list);
        mAdd = findViewById(R.id.btn_add);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("images");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Download: ", "Snapshot: "+ snapshot.toString());
                arrayList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    ImageModel model = snapshot1.getValue(ImageModel.class);
                    arrayList.add(model);
                }
                imageAdapter  = new ImageAdapter(arrayList, ImageListActivity.this);
                mImageList.setAdapter(imageAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ImageListActivity.this, MainActivity.class));
            }
        });

        mImageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageModel model = arrayList.get(position);

                Intent intent = new Intent(ImageListActivity.this, FirebaseItemShowerActivity.class);
                intent.putExtra(Constants.FIREBASE_DATA_ITEM, model);
                startActivity(intent);

            }
        });


    }
}