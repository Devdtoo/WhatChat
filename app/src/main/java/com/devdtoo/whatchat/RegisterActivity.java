package com.devdtoo.whatchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username, email, password;
    Button register_btn;

    
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register_btn = findViewById(R.id.register_btn);
//        Toolbar setup -> working --> use android.widget.Toolbar  ...not androidx toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setTitle("Register");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_usesrname = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                Log.i("Register Btn", "Button clicked");

                if (TextUtils.isEmpty(txt_usesrname) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) ) {
                    Toast.makeText(RegisterActivity.this, "All the fields are required", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must be of at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_usesrname, txt_email, txt_password);
                }
            }
        });
        
    }

    private void register(final String username, String email, String password) {


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("Registered", "Registration IS SUCCESSFULL");
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String uid = firebaseUser.getUid();
                            Log.i("UID", "GOT UID STORED");

//                          Database
                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            Log.i("Reference", "GOT REFERENCE");

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", uid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase());


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.i("Database", "Database VALUE is SET");
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Something went wrong while Creating Account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
























