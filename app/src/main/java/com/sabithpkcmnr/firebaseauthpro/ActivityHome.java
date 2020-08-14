package com.sabithpkcmnr.firebaseauthpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ActivityHome extends AppCompatActivity {

    CardView homeAuthMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeAuthMobile = findViewById(R.id.homeAuthMobile);

        homeAuthMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityHome.this, ActivityMobileOTP.class));
            }
        });


    }
}