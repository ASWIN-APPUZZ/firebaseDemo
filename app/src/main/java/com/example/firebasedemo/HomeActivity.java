package com.example.firebasedemo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HomeActivity extends AppCompatActivity {

    TextView Username, Email;
    FirebaseFirestore fstore;
    FirebaseAuth fauth;
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       Username= findViewById(R.id.tv_uid);
       Email= findViewById(R.id.tv_psd);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

       // FirebaseUser fuser = fauth.getCurrentUser();
        String userId = fauth.getCurrentUser().getUid();

        DocumentReference docRef = fstore.collection("user").document(userId);
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Username.setText(value.getString("Full Name"));
                Email.setText(value.getString("Email"));
            }
        });
    }
}