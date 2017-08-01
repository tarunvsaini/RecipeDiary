package com.tarun.saini.recipeDiary.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tarun.saini.recipeDiary.data.RecipeContract.FavoriteRecipeEntry;
import com.tarun.saini.recipeDiary.data.RecipeContract.FavoriteRecipeSteps;
import com.tarun.saini.recipeDiary.data.RecipeContract.IngredientEntry;
import com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry;

import static android.R.attr.id;
import static com.tarun.saini.recipeDiary.data.RecipeContract.CONTENT_AUTHORITY;
import static com.tarun.saini.recipeDiary.data.RecipeContract.PATH_FAVORITE;
import static com.tarun.saini.recipeDiary.data.RecipeContract.PATH_INGREDIENTS;
import static com.tarun.saini.recipeDiary.data.RecipeContract.PATH_LIST;
import static com.tarun.saini.recipeDiary.data.RecipeContract.PATH_STEPS;

/**
 * Created by Tarun on 27-07-2017.
 */

public class RecipeProvider extends ContentProvider {


    private static final int SHOPPINGLIST=100;
    private static final int SHOPPING_ITEM_ID=101;
    private static final int FAVORITE_RECIPE=102;
    private static final int INGREDIENT_LIST=103;
    private static final int STEP_LIST=104;


    public static final String LOG_TAG = RecipeProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_LIST,SHOPPINGLIST);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_LIST + "/#",SHOPPING_ITEM_ID);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_FAVORITE,FAVORITE_RECIPE);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_STEPS,STEP_LIST);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_INGREDIENTS,INGREDIENT_LIST);
    }


    private RecipeDbHelper recipeDbHelper;


    @Override
    public boolean onCreate() {

        recipeDbHelper=new RecipeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database=recipeDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case SHOPPINGLIST:

                cursor = database.query(ShoppingListEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case SHOPPING_ITEM_ID:

                selection = ShoppingListEntry.KEY_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(ShoppingListEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FAVORITE_RECIPE:

                cursor = database.query(FavoriteRecipeEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case INGREDIENT_LIST:
                cursor = database.query(IngredientEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case STEP_LIST:
                cursor = database.query(FavoriteRecipeSteps.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SHOPPINGLIST:
                return insertShoppingList(uri, values);

            case FAVORITE_RECIPE:
                return insertFavoriteRecipes(uri,values);

            case INGREDIENT_LIST:
                return insertIngredients(uri,values);

            case STEP_LIST:
                return insertSteps(uri,values);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertFavoriteRecipes(Uri uri, ContentValues values) {

        SQLiteDatabase database = recipeDbHelper.getWritableDatabase();
        long id = database.insert(FavoriteRecipeEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertIngredients(Uri uri, ContentValues values) {

        SQLiteDatabase database = recipeDbHelper.getWritableDatabase();
        long id = database.insert(IngredientEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertSteps(Uri uri, ContentValues values) {

        SQLiteDatabase database = recipeDbHelper.getWritableDatabase();
        long id = database.insert(FavoriteRecipeSteps.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertShoppingList(Uri uri, ContentValues values)
    {
        SQLiteDatabase database = recipeDbHelper.getWritableDatabase();
        long id = database.insert(ShoppingListEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = recipeDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SHOPPINGLIST:
                rowsDeleted=database.delete(ShoppingListEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SHOPPING_ITEM_ID:
                // Delete a single row given by the ID in the URI
                selection = ShoppingListEntry.KEY_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ShoppingListEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE_RECIPE:
                rowsDeleted=database.delete(FavoriteRecipeEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case INGREDIENT_LIST:
                rowsDeleted=database.delete(IngredientEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case STEP_LIST:
                rowsDeleted=database.delete(FavoriteRecipeSteps.TABLE_NAME, selection, selectionArgs);
                break;


            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
