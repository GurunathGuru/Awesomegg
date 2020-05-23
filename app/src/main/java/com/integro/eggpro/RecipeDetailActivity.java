package com.integro.eggpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.demono.AutoScrollViewPager;
import com.integro.eggpro.adapters.RecipesImagesAdapter;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.model.Recipes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String TAG = "RecipeDetailActivity";

    String itemId;
    AutoScrollViewPager autoScrollViewPager;
    TextView tvTitle;
    TextView tvCreatedAt;
    TextView tvIngredients;
    TextView tvRecipeMethod;
    TextView tvNutrients;

    private AutoScrollViewPager viewPager;
    private ArrayList<String> recipesArrayList;
    private RecipesImagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Recipes recipes = (Recipes) getIntent().getSerializableExtra("data");

        itemId = recipes.getId();
        viewPager = findViewById(R.id.vpImages);

        getRecipeMethod();
        autoScrollViewPager = findViewById(R.id.vpImages);
        tvTitle = findViewById(R.id.tvTitle);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvRecipeMethod = findViewById(R.id.tvRecipeMethod);
        tvNutrients = findViewById(R.id.tvNutrients);

        tvTitle.setText(recipes.getTitle());
        tvCreatedAt.setText(recipes.getCreatedAt());
        tvIngredients.setText(recipes.getIngredients());
        tvRecipeMethod.setText(recipes.getRecipemethod());
        tvNutrients.setText(recipes.getNutrients());
    }

    public void getRecipeMethod() {
        Call<ArrayList<Recipes>> recipe = ApiClient.getClient2().create(ApiService.class).getRecipe(itemId);
        recipe.enqueue(new Callback<ArrayList<Recipes>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipes>> call, Response<ArrayList<Recipes>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "onResponse: fail");
                    return;
                }
                if (response.body() == null) {
                    Log.i(TAG, "onResponse: null");
                    return;
                }

                recipesArrayList = new ArrayList<>(response.body().getImagesList());
                adapter = new RecipesImagesAdapter(getApplicationContext(), recipesArrayList);
                viewPager.setAdapter(adapter);
                viewPager.startAutoScroll();
            }

            @Override
            public void onFailure(Call<ArrayList<Recipes>> call, Throwable t) {

            }
        });
    }

}
