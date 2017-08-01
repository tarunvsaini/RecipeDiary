package com.tarun.saini.recipeDiary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static com.tarun.saini.recipeDiary.data.RecipeContract.*;
import static com.tarun.saini.recipeDiary.data.RecipeContract.FavoriteRecipeEntry.RECIPE_ID;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_ID;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_INGREDIENT;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_MEASURE;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_NAME;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_QUANTITY;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.TABLE_NAME;

/**
 * Created by Tarun on 27-07-2017.
 */

public class RecipeDbHelper extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "recipe.db";

    private static final String LOGTAG = "RECIPE_DATABASE";
    private SQLiteOpenHelper dbhandler;


    private String CREATE_SHOPPING_LIST_TABLE = "CREATE TABLE " + TABLE_NAME + "("+
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ID + " TEXT,"
            + KEY_QUANTITY + " DOUBLE NOT NULL,"
            + KEY_MEASURE + " TEXT NOT NULL,"
            + KEY_INGREDIENT + " TEXT NOT NULL" + ")";
    //String image, String recipeCategory, String recipetype, Integer calories, Integer serves, String estimatedtime, String description
    String CREATE_FAVORITE_RECIPE_TABLE = "CREATE TABLE " + FavoriteRecipeEntry.TABLE_NAME + "("+
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FavoriteRecipeEntry.RECIPE_ID + " TEXT NOT NULL,"
            + FavoriteRecipeEntry.RECIPE_IMAGE + " TEXT,"
            + FavoriteRecipeEntry.RECIPE_CATEGORY+ " TEXT,"
            + FavoriteRecipeEntry.RECIPE_TYPE + " TEXT,"
            + FavoriteRecipeEntry.RECIPE_CALORIES+ " TEXT,"
            + FavoriteRecipeEntry.RECIPE_SERVES + " TEXT,"
            + FavoriteRecipeEntry.RECIPE_TIME+ " TEXT,"
            + FavoriteRecipeEntry.RECIPE_NAME + " TEXT NOT NULL" + ")";


    private String CREATE_INGREDIENT_TABLE = "CREATE TABLE " +IngredientEntry.TABLE_NAME + "("+
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IngredientEntry.KEY_ID + " TEXT,"
            + IngredientEntry.KEY_NAME + " TEXT,"
            + IngredientEntry.KEY_QUANTITY + " DOUBLE,"
            + IngredientEntry.KEY_MEASURE + " TEXT,"
            + IngredientEntry.KEY_INGREDIENT + " TEXT" + ")";

    private String CREATE_STEPS_TABLE = "CREATE TABLE " + FavoriteRecipeSteps.TABLE_NAME + "("+
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FavoriteRecipeSteps.RECIPE_ID + " TEXT ,"
            + FavoriteRecipeSteps.RECIPE_STEPS + " TEXT,"
            + FavoriteRecipeSteps.RECIPE_NAME + " TEXT" + ")";


    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open(){
        Log.i(LOGTAG, "Database Opened");
        SQLiteDatabase db = dbhandler.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }




    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL(CREATE_SHOPPING_LIST_TABLE);
        db.execSQL(CREATE_FAVORITE_RECIPE_TABLE);
        db.execSQL(CREATE_INGREDIENT_TABLE);
        db.execSQL(CREATE_STEPS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
}
