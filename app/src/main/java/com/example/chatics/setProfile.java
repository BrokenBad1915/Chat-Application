package com.example.chatics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class setProfile extends AppCompatActivity {

    CardView musergetimage;
    ImageView mgetuserimageinimageview;
    private static int PICK_IMAGE=123;
    Uri imagepath;
    Button msaveprofile;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String ImageUriAccessToken;
    private String name;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        mAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        musergetimage=(CardView) findViewById(R.id.mgetuserimage);
        mgetuserimageinimageview=(ImageView) findViewById(R.id.imageView3);
        msaveprofile=(Button) findViewById(R.id.button2);
        getSupportActionBar().setTitle("ChaTics");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable((Color.parseColor("#800000"))));


        musergetimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(i,PICK_IMAGE);
            }
        });

        msaveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagepath==null) {
                    mAuth.signOut();
                    Toast.makeText(setProfile.this, "Image is empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    sendDataForNewUser();
                    Intent i = new Intent(setProfile.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private void sendDataForNewUser(){
        sendImagetoStorage();

    }
    private void sendImagetoStorage(){
        StorageReference imageref = storageReference.child("Images").child(mAuth.getUid()).child("Profile Pic");
        Bitmap bitmap=null;
        try{
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        UploadTask uploadTask = imageref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageUriAccessToken=uri.toString();
                        Toast.makeText(setProfile.this, "URI get Success", Toast.LENGTH_SHORT).show();
                        sendDataTocloudFirestore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(setProfile.this, "URI get Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(setProfile.this, "Image is Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(setProfile.this, "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendDataTocloudFirestore() {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(mAuth.getUid());
        Map<String, Object> userdata=new HashMap<>();
        userdata.put("image", ImageUriAccessToken);
        userdata.put("uid",mAuth.getUid());
        userdata.put("status", "Online");

        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            imagepath=data.getData();
            mgetuserimageinimageview.setImageURI(imagepath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}