package com.sabithpkcmnr.firebaseauthpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ActivityMobileOTP extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    MaterialCardView authLogin;
    ProgressBar authLoginProgress;
    String stringPhoneCode, stringPhoneNumber; //Yes, Firebase need phone number in string
    TextInputEditText etAuthPhoneCode, etAuthPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_otp);
        firebaseAuth = FirebaseAuth.getInstance();

        authLogin = findViewById(R.id.authLogin);
        etAuthPhoneCode = findViewById(R.id.etAuthPhoneCode);
        etAuthPhoneNumber = findViewById(R.id.etAuthPhoneNumber);
        authLoginProgress = findViewById(R.id.authLoginProgress);

        authLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringPhoneCode = etAuthPhoneCode.getText().toString();
                stringPhoneNumber = etAuthPhoneNumber.getText().toString();

                if (stringPhoneCode.length() < 1) {
                    etAuthPhoneCode.setError("Enter valid country code");
                    return;
                }

                if (stringPhoneNumber.length() < 1) {
                    etAuthPhoneNumber.setError("Enter valid mobile number");
                    return;
                }

                authLoginProgress.setVisibility(View.VISIBLE);
                requestForOTPMessage();
            }
        });

    }

    private void requestForOTPMessage() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                stringPhoneCode + stringPhoneNumber, 60,
                TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        authLoginProgress.setVisibility(View.GONE);
                        Toast.makeText(ActivityMobileOTP.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        authLoginProgress.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(ActivityMobileOTP.this, "OTP Verification Success!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ActivityMobileOTP.this, ActivitySuccess.class));

                        } else {
                            Toast.makeText(ActivityMobileOTP.this, "OTP Verification failed! Error: " + task.getException(), Toast.LENGTH_LONG).show();

                            //Use below code if you want to specific inform the user his entered OTP is wrong
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(ActivityMobileOTP.this, "OTP Entered is wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}