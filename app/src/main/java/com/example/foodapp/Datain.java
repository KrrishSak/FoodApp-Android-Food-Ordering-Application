package com.example.foodapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Datain extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Uri imageUri;

    EditText ingredients, instructions, title, duration, serving;
    ImageView preview;
    Button uploadBtn, photoUpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datain);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ingredients = findViewById(R.id.ingriinput);
        instructions = findViewById(R.id.instructionview);
        title = findViewById(R.id.title);
        duration = findViewById(R.id.duration);
        serving = findViewById(R.id.serving);
        preview = findViewById(R.id.preview);
        uploadBtn = findViewById(R.id.saverec);
        photoUpBtn = findViewById(R.id.uploadpho);

        photoUpBtn.setOnClickListener(view -> selectImage());

        uploadBtn.setOnClickListener(view -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                String t = title.getText().toString().trim();
                String d = duration.getText().toString().trim();
                String s = serving.getText().toString().trim();
                String ing = ingredients.getText().toString().trim();
                String ins = instructions.getText().toString().trim();

                String imgUri = imageUri != null ? imageUri.toString() : "";

                uploadImage(userId, imgUri);
                saveUserDataToFirestore(userId, t, d, s, imgUri, ing, ins);
            } else {
                Toast.makeText(Datain.this, "User is not logged in.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserDataToFirestore(String userId, String t, String d, String s, String imgUri, String ing, String ins) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("title", t);
        userData.put("duration", d);
        userData.put("servings", s);
        userData.put("image", imgUri);
        userData.put("ingredients", ing);
        userData.put("instructions", ins);

        db.collection("userrecipes").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> Toast.makeText(Datain.this, "Data saved successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(Datain.this, "Error saving data: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void uploadImage(String userId, String imagePath) {
        Log.d("dgarf",imageUri.toString());
        if (imageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("foodimages/" + userId + ".jpg");
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        updateImageUri(userId, uri.toString());
                        Toast.makeText(Datain.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }))
                    .addOnFailureListener(e -> Toast.makeText(Datain.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }

    private void updateImageUri(String userId, String newUri) {
        db.collection("userrecipes").document(userId)
                .update("image", newUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            preview.setImageURI(imageUri);
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
}
