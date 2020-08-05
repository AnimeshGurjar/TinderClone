package com.example.tinderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private EditText mname,mphone;
    private Button mconfirm,mback;
    private ImageView mprofileimage;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db=FirebaseDatabase.getInstance();
    private DatabaseReference r1;
    private String userid,name,phone,profileimageurl,usersex;
    private Uri resulturi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mname=(EditText)findViewById(R.id.name);
        mphone=(EditText)findViewById(R.id.phone);
        mconfirm=(Button)findViewById(R.id.confirm);
        mback=(Button)findViewById(R.id.back);
        mprofileimage=(ImageView)findViewById(R.id.profileImage);
        mAuth=FirebaseAuth.getInstance();
        userid=mAuth.getCurrentUser().getUid();
        r1=db.getReference().child("Users").child(userid);
        getuserinfo();
        mprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent (Intent.ACTION_PICK);

                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
        mconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveuserinfo();
            }
        });
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });

    }

    private void getuserinfo() {
        r1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot.getChildrenCount()>0)
                {
                    Map<String,Object> map=(Map<String, Object>)snapshot.getValue();
                    if(map.get("name")!=null)
                    {name=map.get("name").toString();
                        mname.setText(name);
                    }

                    if(map.get("phone")!=null)
                    {phone=map.get("phone").toString();
                        mphone.setText(phone);
                    }
                    if(map.get("sex")!=null)
                    {usersex=map.get("sex").toString();

                    }
                    Glide.clear(mprofileimage);
                    if(map.get("profileImageUrl")!=null)
                    {profileimageurl=map.get("profileImageUrl").toString();
                        switch (profileimageurl)
                        {
                            case "default":
                                // Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(mprofileimage);
                                mprofileimage.setImageResource(R.mipmap.ic_launcher);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileimageurl).into(mprofileimage);
                                break;
                        }
                        Glide.with(getApplication()).load(profileimageurl).into(mprofileimage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveuserinfo() {
        name= mname.getText().toString();
        phone=mphone.getText().toString();
        Map userinfo= new HashMap();
        userinfo.put("name",name);
        userinfo.put("phone",phone);
        r1.updateChildren(userinfo);
        if(resulturi!=null)
        {   FirebaseStorage f=FirebaseStorage.getInstance();
            final StorageReference filepath= f.getReference().child("profileImages").child(userid);
            mprofileimage.setDrawingCacheEnabled(true);
            mprofileimage.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) mprofileimage.getDrawable()).getBitmap();

            ByteArrayOutputStream baos =  new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask=filepath.putBytes(data);







     /*   uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finish();
            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadurl=filepath.getDownloadUrl();
                Log.i(".........................",downloadurl.toString());
                Map userinfo=new HashMap();
                userinfo.put("profileImageUrl",downloadurl.toString());
                r1.updateChildren(userinfo);
                finish();
                return;
            }
        });
        */
            Task<Uri> urltask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful())
                    {Log.i("................................................................","erorrrrrrr");
                        // Log.i("................................................................",task.getResult().toString());
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadurl= (Uri) task.getResult();
                        Log.i(".....................................................",downloadurl.toString());
                        Map userinfo=new HashMap();
                        userinfo.put("profileImageUrl",downloadurl.toString());
                        r1.updateChildren(userinfo);
                        finish();
                        return;
                    }
                    else
                    {

                    }
                }
            });
        }
        else
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&& resultCode== Activity.RESULT_OK)
        {
            final Uri imageuri=data.getData();
            resulturi=imageuri;
            mprofileimage.setImageURI(resulturi);
        }
    }
}