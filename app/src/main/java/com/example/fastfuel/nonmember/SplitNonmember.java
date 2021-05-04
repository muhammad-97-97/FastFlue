package com.example.fastfuel.nonmember;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastfuel.R;

public class SplitNonmember extends AppCompatActivity {
    Button mGeneral, mEmergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_nonmember);

        mGeneral = findViewById(R.id.btnGeneral);
        mEmergency  = findViewById(R.id.btnEmergency);


        mGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OrderNonmember.class));
            }
        });

        mEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EmergencyNonmember.class));
            }
        });
    }
}