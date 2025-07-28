package com.example.foodapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {
    Context context;
    ArrayList<Recipe> usearraylist;

    public RecipeAdapter(Context context, ArrayList<Recipe> usearraylist) {
        this.context = context;
        this.usearraylist = usearraylist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.recipefrag,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Recipe recipe = usearraylist.get(position);
        holder.title.setText(recipe.getTitle());
        holder.duration.setText(recipe.getDuration());
        holder.c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Dataout.class);
                intent.putExtra("recipe",recipe);
                context.startActivity(intent);
            }
        });
        Picasso.get().load(recipe.getImage()).into(holder.image);
    }


    @Override
    public int getItemCount() {
        return usearraylist.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
   TextView title, duration;
   ImageView image;
   CardView c;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv1);
            duration = itemView.findViewById(R.id.tv2);
            image =itemView.findViewById(R.id.img1);
            c = itemView.findViewById(R.id.recrec);
        }
    }
}