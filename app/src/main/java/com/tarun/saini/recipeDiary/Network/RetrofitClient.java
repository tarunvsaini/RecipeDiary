package com.tarun.saini.recipeDiary.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tarun on 02-07-2017.*/



public class RetrofitClient {



    private static Retrofit retrofit = null;
    public static final String BASE_URL = "http://tarunapi.xyz/api/v1/";
    public RecipeClient recipeClient;


    public RecipeClient getRecipeService()
    {
        if(recipeClient ==null)
        {
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            recipeClient =retrofit.create(RecipeClient.class);
        }
        return recipeClient;
    }
}