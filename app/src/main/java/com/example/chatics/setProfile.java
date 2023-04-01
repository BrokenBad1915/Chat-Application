package com.example.chatics;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class setProfile extends AppCompatActivity {

    CardView musergetimage;
    ImageView mgetuserimageinimageview;
    private static int PICK_IMAGE=123;
    Uri imagepath;
    EditText mgetusername;
    Button msaveprofile;
    private FirebaseAuth mAuth;
    private String name;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String ImageUriAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        mAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        getSupportActionBar().setTitle("ChaTics");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable((Color.parseColor("#800000"))));
    }
}