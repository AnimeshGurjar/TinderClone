package com.example.tinderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tinderapp.Cards.arrayAdapter;
import com.example.tinderapp.Cards.cards;
import com.example.tinderapp.Matches.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private cards cards_data[];
    private com.example.tinderapp.Cards.arrayAdapter arrayAdapter;
    private int i;
    private FirebaseAuth mAuth;

    private String usersex;
    private String notusersex;
    ListView listView;
    List<cards> rowItems;
    private FirebaseDatabase usersdb = FirebaseDatabase.getInstance();
    ;
    private DatabaseReference rr;
    private DatabaseReference rr1;
    private String currentUId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rr = usersdb.getReference();
        rr1 = rr.child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        rowItems = new ArrayList<cards>();
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);

        checkusersex();


        rowItems = new ArrayList<cards>();
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);


        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                cards obj = (cards) dataObject;
                String userId = obj.getUserid();
                //  DatabaseReference rr= rr1.child(notusersex);
                DatabaseReference rr2 = rr1.child(userId);
                DatabaseReference rr3 = rr2.child("connections");
                DatabaseReference rr4 = rr3.child("nope");
                DatabaseReference rr5 = rr4.child(currentUId);
                rr5.setValue(true);
                Toast.makeText(MainActivity.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserid();
                // DatabaseReference rr= rr1.child(notusersex);
                DatabaseReference rr2 = rr1.child(userId);
                DatabaseReference rr3 = rr2.child("connections");
                DatabaseReference rr4 = rr3.child("yeps");
                DatabaseReference rr5 = rr4.child(currentUId);
                rr5.setValue(true);
                isConnectionmatch(userId);
                Toast.makeText(MainActivity.this, "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isConnectionmatch(String userId) {
        // DatabaseReference currentUserConnectionsDb=rr1.child(usersex);
        DatabaseReference onee = rr1.child(currentUId);
        DatabaseReference two = onee.child("connections");
        DatabaseReference three = two.child("yeps");
        DatabaseReference four = three.child(userId);
        four.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(MainActivity.this, "new connection", Toast.LENGTH_SHORT).show();
                    //  DatabaseReference rr= rr1.child(notusersex);
                    //  DatabaseReference p=rr.child(snapshot.getKey());
                    // DatabaseReference q=p.child("connections");
                    //   DatabaseReference tt=q.child("matches");
                    //  DatabaseReference ttt=tt.child(currentUId);
                    //   ttt.setValue(true);
                    rr1.child(snapshot.getKey()).child("connections").child("matches").child(currentUId).setValue(true);
                    rr1.child(currentUId).child("connections").child("matches").child(snapshot.getKey()).setValue(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void checkusersex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference maledb = rr1.child(user.getUid());
        //DatabaseReference ppp=maledb.getReference();
        // DatabaseReference myref1=maledb.getReference("Users");
        // DatabaseReference one1=ppp.child(user.getUid());
        maledb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("sex").getValue() != null) {
                        usersex = snapshot.child("sex").getValue().toString();
                        notusersex = "Female";
                        switch (usersex) {
                            case "Male":
                                notusersex = "Female";
                                break;
                            case "Female":
                                notusersex = "Male";
                                break;

                        }
                        Log.i("................................................................", "dgwekufglwegof");
                        getoppositesexuser();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getoppositesexuser() {

        rr1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i(".........................................................", "111111");
                DataSnapshot rr3 = snapshot.child("connections");
                DataSnapshot rr4 = rr3.child("yeps");
                DataSnapshot rr5 = rr3.child("nope");
                //  DatabaseReferenc rr5=rr4.child(currentUId);
                if (snapshot.exists() && !rr4.hasChild(currentUId) && !rr5.hasChild(currentUId) && snapshot.child("sex").getValue().toString().equals(notusersex)) {
                    Log.i("..........................................................................", "22222222");
                    String profileImageUrl = "default";
                    // rowItems.add(snapshot.child("name").getValue().toString());
                    if (!snapshot.child("profileImageUrl").getValue().equals("default")) {
                        profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                    }
                    cards Item = new cards(snapshot.getKey(), snapshot.child("name").getValue().toString(), profileImageUrl);
                    rowItems.add(Item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, ChoooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }


    public void gotosettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        return;
    }

    public void gotomatches(View view) {
        Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
        startActivity(intent);
        return;
//    }
    }
}