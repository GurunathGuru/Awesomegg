package com.integro.eggpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.integro.eggpro.R;
import com.integro.eggpro.model.Recipes;

import java.util.ArrayList;

public class RecipesImagesAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> recipesArrayList;

    public RecipesImagesAdapter(Context context, ArrayList<String> recipesArrayList) {
        this.context=context;
        this.recipesArrayList=recipesArrayList;
    }

    @Override
    public int getCount() {
        return recipesArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView ivImage;
        View view = LayoutInflater.from(context).inflate(R.layout.card_recipe_images, container, false);
        ivImage = view.findViewById(R.id.ivImage);

        Glide.with(context)
                .load("http://awesomegg.com/app/uploads/"+recipesArrayList.get(position))
                .into(ivImage);

        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}
