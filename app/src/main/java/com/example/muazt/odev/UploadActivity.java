package com.example.muazt.odev;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;

    AutoCompleteTextView autocomplete;

    String[] arr = { "FOOTBALL", "PATEN","BICYCLE",
            "BASKETBALL", "BILARDO"};


    EditText event_name;
    EditText event_date;
    EditText event_time;
    EditText autoCompleteText_View;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorageRef;
    Uri selected;
    private Button event_create;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        event_name = (EditText) findViewById(R.id.event_name);
        event_date = (EditText) findViewById(R.id.event_date);
        event_time = (EditText) findViewById(R.id.event_time);
        autoCompleteText_View = (EditText) findViewById(R.id.autoCompleteText_View);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        autocomplete = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteText_View);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, arr);

        autocomplete.setThreshold(1);
        autocomplete.setAdapter(adapter);


    }

    public void upload(View view) {


        UUID uuidImage = UUID.randomUUID();

        String imageName = "images/"+uuidImage+".jpg";

        StorageReference storageReference = mStorageRef.child(imageName);

        storageReference.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String downloadURL = taskSnapshot.getDownloadUrl().toString();

                FirebaseUser user = mAuth.getCurrentUser();
                String userEmail = user.getEmail().toString();
                String eventName = event_name.getText().toString();
                String eventDate = event_date.getText().toString();
                String eventTime = event_time.getText().toString();
                String autoCompleteTextView = autoCompleteText_View.getText().toString();

                UUID uuid = UUID.randomUUID();
                String uuidString = uuid.toString();

                myRef.child("Posts").child(uuidString).child("useremail").setValue(userEmail);
                myRef.child("Posts").child(uuidString).child("EventName").setValue(eventName);
                myRef.child("Posts").child(uuidString).child("EventDate").setValue(eventDate);
                myRef.child("Posts").child(uuidString).child("EventTıme").setValue(eventTime);
                myRef.child("Posts").child(uuidString).child("Category").setValue(autoCompleteTextView);
                myRef.child("Posts").child(uuidString).child("downloadurl").setValue(downloadURL);

                Toast.makeText(getApplicationContext(),"Post Shared",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


    }



    //Telefondan resim seçmeyi sağlayan metod
    private void chooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            selected = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selected);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }




}