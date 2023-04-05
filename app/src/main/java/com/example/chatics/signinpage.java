package com.example.chatics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class signinpage extends AppCompatActivity implements View.OnClickListener {

    EditText username;
    EditText age1;
    EditText email1;
    EditText password1;
    Button signup;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinpage);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        username=(EditText) findViewById(R.id.username);
        age1=(EditText) findViewById(R.id.userage);
        email1=(EditText) findViewById(R.id.email);
        password1=(EditText) findViewById(R.id.Password);
        signup=(Button) findViewById(R.id.btnsignup);
        signup.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();



    }
    public void onClick(View v){
        if(v.getId()==signup.getId()){
            registeruser();
        }
    }
    public void mainpage(View v) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
    public void registeruser(){
        String email=email1.getText().toString().trim();
        String password=password1.getText().toString().trim();
        String fullName=username.getText().toString().trim();
        String age=age1.getText().toString().trim();

        if(email.isEmpty() || password.isEmpty() || fullName.isEmpty() || age.isEmpty()){
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email1.setError("Please provide a valid email.");
            email1.requestFocus();
            return;
        }
        if(password.length()<6){
            password1.setError("Min password length should be 6 characters!");
            password1.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullName, age, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                FirebaseAuth.getInstance().signOut();
                                                Intent i = new Intent(signinpage.this, MainActivity.class);
                                                startActivity(i);
                                                Toast.makeText(signinpage.this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(signinpage.this, "Failed to Register!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(signinpage.this, "Failed to Register!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}









