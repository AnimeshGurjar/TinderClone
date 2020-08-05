package com.example.tinderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private Button mregister;
    private EditText memail,mpassword,mname;
    private RadioGroup mradiogroup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseauthlistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth=FirebaseAuth.getInstance();
        firebaseauthlistener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    Intent intent=new Intent(RegistrationActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return ;
                }
            }
        };

        mregister=(Button)findViewById(R.id.register);
        memail=(EditText)findViewById(R.id.email);
        mpassword=(EditText)findViewById(R.id.password);
        mname=(EditText)findViewById(R.id.name);
        mradiogroup=(RadioGroup)findViewById(R.id.radioGroup);
        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedid=mradiogroup.getCheckedRadioButtonId();
                final RadioButton radiobutton=(RadioButton)findViewById(selectedid);
                if(radiobutton.getText()==null)
                {
                    return;
                }
                final String email=memail.getText().toString();
                final String password=mpassword.getText().toString();
                final String name=mname.getText().toString();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(RegistrationActivity.this,"sign up error",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String userId=mAuth.getCurrentUser().getUid();
                            FirebaseDatabase currentuserdb= FirebaseDatabase.getInstance();
                            DatabaseReference myref=currentuserdb.getReference("Users");
                            //  DatabaseReference one=myref.child(radiobutton.getText().toString());
                            DatabaseReference two=myref.child(userId);
                            Map userinfo=new HashMap<>();
                            userinfo.put("name",name);
                            userinfo.put("sex",radiobutton.getText().toString());
                            userinfo.put("profileImageUrl","default");
                            two.updateChildren(userinfo);
                            // DatabaseReference three=two.child("name");


                            // three.setValue(name);


                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseauthlistener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseauthlistener);
    }
}