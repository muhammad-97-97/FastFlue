package com.example.fastfuel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fastfuel.nonmember.SplitNonmember;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register2 extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,  mPhone, mAddress, mEmail;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        mEmail = findViewById(R.id.email);
        mFullName = findViewById(R.id.fullName);
        mPhone = findViewById(R.id.phoneNum);
        mAddress = findViewById(R.id.address);
        mRegisterBtn = findViewById(R.id.signupBtn);
        mLoginBtn = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String fullName = mFullName.getText().toString().trim();
                String address = mAddress.getText().toString().trim();

                if (TextUtils.isEmpty(fullName)){
                    mFullName.setError("Full name is Required.");
                    return;
                }

                if (TextUtils.isEmpty(phone)){
                    mPhone.setError("Phone is Required.");
                    return;
                }
                if (phone.length() < 13){
                    mPhone.setError("Phone Must be > 13 Characters.");
                    return;
                }

                if (TextUtils.isEmpty(address)){
                    mAddress.setError("Address is Required.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register the user in firebase
                fAuth.createUserWithEmailAndPassword(email, phone).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register2.this, "User Created.", Toast.LENGTH_SHORT).show();

                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("Non-member").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName",   fullName);
                            user.put("phone",   phone);
                            user.put("address", address);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+ e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(), SplitNonmember.class));
                        }else {
                            Toast.makeText(Register2.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}