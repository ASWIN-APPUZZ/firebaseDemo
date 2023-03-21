package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registrationActivity extends AppCompatActivity {

        TextView registered;
        EditText name, email,password, confirmpassword;
        FirebaseAuth Fauth;
        FirebaseFirestore fstore;

        Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registered = findViewById(R.id.tv_registered);
        name = findViewById(R.id.tv_name);
        email = findViewById(R.id.tv_email);
        password = findViewById(R.id.tv_pwd1);
        confirmpassword = findViewById(R.id.tv_cnfrmpwd);
        btn = findViewById(R.id.btnSubmit1);

        Fauth = FirebaseAuth.getInstance();
        fstore =  FirebaseFirestore.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name= name.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                Fauth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser fuser = Fauth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(registrationActivity.this, "Regitration Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                                    btn.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Intent intent = new Intent(registrationActivity.this,HomeActivity.class);
//                                            startActivity(intent);
//                                        }
//                                    });

                                    String userId = Fauth.getCurrentUser().getUid();

                                    // Ading content into firestore
                                    DocumentReference documentReference = fstore.collection("user").document(userId);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Full Name", Name);
                                    user.put("Email",Email);
                                    //user.document("NE").set(user);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(registrationActivity.this, "User Profile is Created", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(registrationActivity.this, "User creation Failure", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(registrationActivity.this, "Registration unsucessful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                });




            }
        });

        // Go to login page
        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(registrationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }
}