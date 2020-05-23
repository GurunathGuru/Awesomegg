package com.integro.eggpro.adapters;

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

import com.bumptech.glide.Glide;
import com.integro.eggpro.R;
import com.integro.eggpro.RecipeDetailActivity;
import com.integro.eggpro.model.Recipes;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Recipes>recipesArrayList;

    public RecipeAdapter(Context context, ArrayList<Recipes> recipesArrayList) {
        this.context=context;
        this.recipesArrayList=recipesArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_recipes,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Recipes recipes=recipesArrayList.get(position);

        Glide.with(context)
                .load("http://www.awesomegg.com/app/uploads/"+recipes.getImagesList()[0])
                .into(holder.ivImage);

        holder.tvTitle.setText(recipes.getTitle());
        holder.tvDescription.setText(recipes.getRecipemethod());

        holder.cvRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, RecipeDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data",recipes);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipesArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvDescription;
        CardView cvRecipe;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage=itemView.findViewById(R.id.ivImage);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            cvRecipe=itemView.findViewById(R.id.cvRecipe);
        }
    }
}
