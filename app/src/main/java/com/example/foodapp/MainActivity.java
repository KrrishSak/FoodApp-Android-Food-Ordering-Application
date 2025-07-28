package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton drawer;
    RecyclerView recyclerView;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    TextView name,see;
    ArrayList<Recipe> userArrayList;
    RecipeAdapter myAdapter;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        name = findViewById(R.id.namedetail);
        recyclerView = findViewById(R.id.recycler3);
        drawerLayout = findViewById(R.id.appdrawer);
         drawer = findViewById(R.id.menu_icon);
        navigationView = findViewById(R.id.navigationview);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();

                if (itemid == R.id.favourite) {
                    startActivity(new Intent(getApplicationContext(), profile.class));
                } else if (itemid == R.id.history) {
                    Toast.makeText(MainActivity.this, "Cart Clicked", Toast.LENGTH_SHORT).show();
                } else if (itemid == R.id.help) {
                    Toast.makeText(MainActivity.this, "Menu Clicked", Toast.LENGTH_SHORT).show();
                } else if (itemid == R.id.logout) {
                    mAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), profile.class);
                startActivity(intent);
                finish();
            }
        });



        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<>();
        myAdapter = new RecipeAdapter(MainActivity.this, userArrayList);
        recyclerView.setAdapter(myAdapter);



        // Call method to fetch recipes from Firestore
        fetchRecipes();
        loadUserProfile();

    }

    private void fetchRecipes() {
        db.collection("recipe")
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
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                               String firstName = documentSnapshot.getString("firstName");
                            name.setText(firstName);
                        } else {
                            Toast.makeText(MainActivity.this, "Profile does not exist.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error fetching profile.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
