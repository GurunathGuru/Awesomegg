package com.integro.eggpro.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipes implements Serializable {

    private String id;

    private String title;

    //private String[] imagesList;

    private ArrayList<String> imagesList;

    private String nutrients;

    private String createdAt;

    private String recipemethod;

    private String ingredients;

    public Recipes(String id, String title, ArrayList<String> imagesList, String nutrients, String createdAt, String recipemethod, String ingredients) {
        this.id = id;
        this.title = title;
        this.imagesList = imagesList;
        this.nutrients = nutrients;
        this.createdAt = createdAt;
        this.recipemethod = recipemethod;
        this.ingredients = ingredients;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getRecipemethod() {
        return recipemethod;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getImagesList() {
        return imagesList;
    }

    public String getNutrients() {
        return nutrients;
    }
}
