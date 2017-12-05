package com.example.muazt.odev;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
    if(item.getItemId()==R.id.Log_Out){
        mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        FirebaseAuth.getInstance().signOut();
        sendToStart();


    }else if(item.getItemId()==R.id.add_event){
        Intent intent=new Intent(getApplicationContext(),UploadActivity.class);
        startActivity(intent);
        }
        return true;
    }

    private void sendToStart() {

        Intent startIntent = new Intent(FeedActivity.this, MainActivity.class);
        startActivity(startIntent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        }

    }
}
