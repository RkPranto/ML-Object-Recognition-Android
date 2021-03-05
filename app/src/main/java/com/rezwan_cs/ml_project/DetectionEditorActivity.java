package com.rezwan_cs.ml_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class DetectionEditorActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_detection_editor);

        mTitle = findViewById(R.id.et_title);
        mDetectionText  = findViewById(R.id.et_detection_text);
        mImage = findViewById(R.id.imageView);
        mCancel = findViewById(R.id.btn_cancel);
        mSave = findViewById(R.id.btn_save);


        storageRef = FirebaseStorage.getInstance().getReference();

        name = UUID.randomUUID() +"_"+DocumentFile.fromSingleUri(this, uri).getName();
        uploadRef = storageRef.child("images/"+name);

        database = FirebaseDatabase.getInstance();
        mDbRef = database.getReference().child("images");

        setImageAndText();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getIntent().getExtras().getString(Constants.PASSED_TYPE) != null
                        && getIntent().getExtras().getString(Constants.PASSED_TYPE).equals(Constants.URI_)){
                    saveDataUriToFirebase();
                }
                else{
                    saveDataBitmapToFirebase();
                }

                startActivity(new Intent(DetectionEditorActivity.this, ImageListActivity.class));
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void saveDataBitmapToFirebase() {
        Bitmap bitmap = (Bitmap) getIntent().getExtras().get("data");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = uploadRef.putBytes(data);
        postUploadWorks(uploadTask);

    }

    private void saveDataUriToFirebase() {
        Uri file = uri;

        UploadTask uploadTask = uploadRef.putFile(file);

        postUploadWorks(uploadTask);

    }

    private void postUploadWorks(UploadTask uploadTask) {
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.d("Download: ", task.getException().getMessage());
                    throw task.getException();
                }

                uploadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("Download: ", "URI: "+ uri.toString());
                        String id = mDbRef.push().getKey();
                        ImageModel model = new ImageModel(id, mTitle.getText().toString().trim() , mDetectionText.getText().toString().trim(), uri.toString());
                        mDbRef.child(id).setValue(model);
                    }
                });

                // Continue with the task to get the download URL
                Log.d("Download: ", "D: "+uploadRef.getDownloadUrl()+" => " +uploadRef.getDownloadUrl().getResult().toString());
                return uploadRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });



        // Observe state change events such as progress, pause, and resume
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d("Download : ", "Upload is " + progress + "% done"+ uploadRef.getDownloadUrl());
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                //Log.d(TAG, "Upload is paused");
            }
        });
    }

    private void setImageAndText() {
        if(getIntent().getExtras() != null){
            title = getIntent().getExtras().getString(Constants.TITLE);
            detection = getIntent().getExtras().getString(Constants.DETECTION_SCORE);

            mTitle.setText(title);
            mDetectionText.setText(detection);
            mTitle.setSelection(mTitle.getText().length());
            mDetectionText.setSelection(mDetectionText.getText().length());


            if(getIntent().getExtras().getString(Constants.PASSED_TYPE) != null && getIntent().getExtras().getString(Constants.PASSED_TYPE).equals(Constants.URI_)){
                type = Constants.URI_;
                uri = Uri.parse(getIntent().getExtras().getString(Constants.URI_));
                mImage.setImageURI(uri);
            }
            else{
                Log.d(getClass().getName(), "Bitomap");
                type = Constants.BITMAP_;
                bitmap = (Bitmap) getIntent().getExtras().get("data");
                mImage.setImageBitmap(bitmap);
            }
        }
    }
}