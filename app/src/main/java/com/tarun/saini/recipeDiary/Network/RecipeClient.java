package com.tarun.saini.recipeDiary.Network;

import com.tarun.saini.recipeDiary.Model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Tarun on 02-07-2017.
 */

public interface RecipeClient
{


    @POST("recipe/add")
    Call<Recipe> saveRecipe(@Body Recipe recipe);

    @GET("recipe")
    Call<ArrayList<Recipe>> getAllRecipes();




}
