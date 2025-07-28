package com.example.foodapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Dataout extends AppCompatActivity {
    TextView name,dur,ser,ing,ins;
    ImageView img;
    Recipe recipe;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataout);
        name = findViewById(R.id.titledata);
        dur = findViewById(R.id.durationdata);
        ser = findViewById(R.id.Servingsdata);

        ing = findViewById(R.id.ingredientsdata);
        ins = findViewById(R.id.instructiondata);
        recipe = getIntent().getParcelableExtra("recipe");
        name.setText(recipe.getTitle());
        dur.setText(recipe.getDuration());
        ser.setText(recipe.getServings());
        ins.setText(recipe.getInstructions());
        ing.setText(recipe.getIngredients());

    }
}