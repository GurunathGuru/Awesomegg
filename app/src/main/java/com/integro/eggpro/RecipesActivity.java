package com.integro.eggpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.integro.eggpro.adapters.RecipeAdapter;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.model.Recipes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesActivity extends AppCompatActivity {

    private RecyclerView rvRecipe;
    private ArrayList<Recipes> recipesArrayList;
    private RecipeAdapter adapter;

    private static final String TAG = "RecipesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        rvRecipe = findViewById(R.id.rvRecipes);
        rvRecipe.setLayoutManager(new LinearLayoutManager(this));
        getRecipesList();
    }

    public void getRecipesList() {
        Call<ArrayList<Recipes>> arrayListCall = ApiClient.getClient2().create(ApiService.class).getRecipe();
        arrayListCall.enqueue(new Callback<ArrayList<Recipes>>() {
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
                int size = response.body().size();
                for (int i = 0; i < size; i++) {
                    recipesArrayList = new ArrayList<>(response.body());
                }
                adapter=new RecipeAdapter(getApplicationContext(),recipesArrayList);
                rvRecipe.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Recipes>> call, Throwable t) {

            }
        });
    }
}
