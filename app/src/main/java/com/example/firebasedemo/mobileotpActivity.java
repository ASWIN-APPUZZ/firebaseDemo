package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class mobileotpActivity extends AppCompatActivity {

    EditText phno, otp;
    Button send, verify;
    private String verificationId;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobileotp);

        phno= findViewById(R.id.txt_phno);
        otp= findViewById(R.id.txt_otp);
        send= findViewById(R.id.btn_otp);
        verify= findViewById(R.id.btn_verify);
        fauth=FirebaseAuth.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phno.getText().toString().isEmpty() && phno.getText().toString().length()==10){
                    String phonenum="+91"+phno.getText().toString().trim();
                    requestOTP(phonenum);
                }else {
                    send.setError("Invalid Mobile Number");
                }
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 verifyCode(otp.getText().toString());
            }
        });
    }



            private void requestOTP(String phonenum) {
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(fauth)
                        .setPhoneNumber(phonenum)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(mobileotpActivity.this)
                        .setCallbacks(mCallbacks)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }

            private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                public void OnCodeSend(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken){
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    final String code = phoneAuthCredential.getSmsCode();
                    if (code!=null){
                        otp.setText(code);
                    }
                    verifyCode(code);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(mobileotpActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            };

            private void verifyCode(String code) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
                signInWithCredential(credential);
            }

            private void signInWithCredential(PhoneAuthCredential credential) {
                fauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(mobileotpActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }



}