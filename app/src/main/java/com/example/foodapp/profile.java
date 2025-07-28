package com.example.foodapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class profile extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    FirebaseFirestore db;

    private FirebaseAuth mauth;
    RecyclerView recyclerView;
    private FirebaseFirestore firestore;
    TextView name , email;
     StorageReference storagereference;
    private Dialog dialog;
    private ImageButton edit, profilepicture, editdialog, back;
    private Uri imageUri;
    ArrayList<Recipe> userArrayList;
    RecipeAdapter myAdapter;
    private ImageView profimg,displayimg;
    private Button change, rec;


//    @SuppressLint("MissingInflatedId")
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase objects
        mauth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        // Add dialog setup code
        dialog = new Dialog(profile.this);
        dialog.setContentView(R.layout.customdialogbox);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialogbox));
        dialog.setCancelable(true);

        // Find views
        editdialog = findViewById(R.id.edit);
        edit = findViewById(R.id.edit);
        profimg = dialog.findViewById(R.id.profileimage);
        profilepicture = dialog.findViewById(R.id.selectimage);
        change = dialog.findViewById(R.id.change);
        displayimg = findViewById(R.id.profilepicture);
        rec = findViewById(R.id.uploadrec);
       name = findViewById(R.id.name1);
       email = findViewById(R.id.emailid);
       back = findViewById(R.id.back);
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
               startActivity(intent);
               finish();
           }
       });

        loadImage();

        recyclerView = findViewById(R.id.saya);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<>();
        myAdapter = new RecipeAdapter(profile.this, userArrayList);
        recyclerView.setAdapter(myAdapter);
        fetchRecipes();







        if(change != null){
      change.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              // Upload the selected image to Firebase Storage
              uploadImage();
          }
      });
  }

   rec.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           Intent intent = new Intent(getApplicationContext(), Datain.class);
           startActivity(intent);
           finish();
       }
   });
        // Set click listeners

    profilepicture.setOnClickListener(this);
        editdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }




    // Handle the result of the image selection


    // Handle the permission request result

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction (Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    private void uploadImage() {
        String userId = mauth.getCurrentUser().getUid();
        String imagePath = "userprofile/" + userId ;
        storagereference = FirebaseStorage.getInstance().getReference("userprofile/"+userId);
        storagereference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask. TaskSnapshot taskSnapshot) {
                        Toast.makeText( profile.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                  dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure (@NonNull Exception e) {
                        Toast.makeText( profile.this, "fail to upload", Toast.LENGTH_SHORT).show();
                     dialog.dismiss();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {

            imageUri = data.getData();
            profimg.setImageURI(imageUri);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==profilepicture.getId()){
            selectImage();
        }
    }

    private void loadImage() {
        String userId = mauth.getCurrentUser().getUid();
        StorageReference userImageRef = FirebaseStorage.getInstance().getReference("userprofile/" + userId);

        userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                updateImageViewWithUri(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(profile.this, "Error fetching image", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateImageViewWithUri(Uri uri) {
        Picasso.get().load(uri).into(displayimg, new Callback() {
            @Override
            public void onSuccess() {
                // Success handling here if needed
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(profile.this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void fetchRecipes() {
        db.collection("userrecipes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert each document into a Recipe object and add it to the list
                            Recipe recipe = document.toObject(Recipe.class);
                            userArrayList.add(recipe);
                        }
                        // Notify the adapter that the data set has changed
                        myAdapter.notifyDataSetChanged();
                    } else {
                        // Handle errors
                    }
                });
    }


    private void loadUserProfile() {
        String userId = mauth.getCurrentUser().getUid();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String firstName = documentSnapshot.getString("firstName");
                            String email1 = documentSnapshot.getString("email");
                            email.setText(email1);
                            name.setText(firstName);
                        } else {
                            Toast.makeText(profile.this, "Profile does not exist.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profile.this, "Error fetching profile.", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
