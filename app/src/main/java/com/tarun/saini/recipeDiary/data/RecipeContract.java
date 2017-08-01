package com.tarun.saini.recipeDiary.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tarun on 27-07-2017.
 */

public class RecipeContract {


    private RecipeContract()
    {

    }



    public static final String CONTENT_AUTHORITY = "com.tarun.saini.recipeDiary";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_LIST = "ShoppingList";
    public static final String PATH_FAVORITE = "favorites";
    public static final String PATH_STEPS = "RecipeSteps";
    public static final String PATH_INGREDIENTS = "ingredient";


    public static final Uri LIST_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_LIST).build();
    public static final Uri FAVORITE_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_FAVORITE).build();
    public static final Uri STEP_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_STEPS).build();
    public static final Uri INGREDIENT_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_INGREDIENTS).build();




    public static final class ShoppingListEntry implements BaseColumns {

        public static final String TABLE_NAME = "ShoppingList";
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";
        public static final String KEY_QUANTITY = "quantity";
        public static final String KEY_MEASURE = "measure";
        public static final String KEY_INGREDIENT = "ingredient";

    }

    public static final class FavoriteRecipeEntry implements BaseColumns {

        public static final String TABLE_NAME = "favorites";
        public static final String RECIPE_ID = "id";
        public static final String RECIPE_NAME = "name";
        public static final String RECIPE_IMAGE = "image";
        public static final String RECIPE_CATEGORY = "category";
        public static final String RECIPE_TYPE = "type";
        public static final String RECIPE_CALORIES = "calorie";
        public static final String RECIPE_SERVES = "serves";
        public static final String RECIPE_TIME = "time";
        public static final String RECIPE_DESCRIPTION = "description";
    }

    public static final class FavoriteRecipeSteps implements BaseColumns {

        public static final String TABLE_NAME = "RecipeSteps";
        public static final String RECIPE_ID = "id";
        public static final String RECIPE_NAME = "name";
        public static final String RECIPE_STEPS = "steps";

    }

    public static final class IngredientEntry implements BaseColumns {

        public static final String TABLE_NAME = "ingredient";
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";
        public static final String KEY_QUANTITY = "quantity";
        public static final String KEY_MEASURE = "measure";
        public static final String KEY_INGREDIENT = "ingredient";

    }
}
