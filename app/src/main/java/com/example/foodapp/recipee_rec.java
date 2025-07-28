package com.example.foodapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class recipee_rec extends Fragment {

    private static final String ARG_PARAM1 = "recipe_title"; // Renamed for clarity
    private static final String ARG_PARAM2 = "recipe_rating"; // Renamed for clarity
    private static final String ARG_PARAM3 = "recipe_image"; // Renamed for clarity

    private String recipeTitle;
    private float recipeRating; // Using float for rating
    private int recipeImageResourceId;

    public recipee_rec() {
        // Required empty public constructor
    }

    public static recipee_rec newInstance(String recipeTitle, float recipeRating, int recipeImageResourceId) {
        recipee_rec fragment = new recipee_rec();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, recipeTitle);
        args.putFloat(ARG_PARAM2, recipeRating); // Using putFloat for rating
        args.putInt(ARG_PARAM3, recipeImageResourceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeTitle = getArguments().getString(ARG_PARAM1);
            recipeRating = getArguments().getFloat(ARG_PARAM2); // Using getFloat for rating
            recipeImageResourceId = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipee_rec, container, false);

        // Find views by ID
        TextView tvRecipeTitle = view.findViewById(R.id.tv1);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        ImageView recipeImage = view.findViewById(R.id.img1);

        // Set data to views
        tvRecipeTitle.setText(recipeTitle);
        ratingBar.setRating(recipeRating);
        recipeImage.setImageResource(recipeImageResourceId);

        return view;
    }
}
