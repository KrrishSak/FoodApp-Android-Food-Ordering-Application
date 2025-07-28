package com.example.foodapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
    private String duration;
    private String image;
    private String ingredients;
    private String instructions;
    private String servings;
    private String title;

    public Recipe() {
        // Empty constructor required by Firestore
    }

    public Recipe(String title, String ingredients, String instructions, String duration, String servings, String image) {
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.duration = duration;
        this.servings = servings;
        this.image = image;
    }

    protected Recipe(Parcel in) {
        duration = in.readString();
        image = in.readString();
        ingredients = in.readString();
        instructions = in.readString();
        servings = in.readString();
        title = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(duration);
        dest.writeString(image);
        dest.writeString(ingredients);
        dest.writeString(instructions);
        dest.writeString(servings);
        dest.writeString(title);
    }
}
