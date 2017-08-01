package com.tarun.saini.recipeDiary.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tarun.saini.recipeDiary.R;
import com.tarun.saini.recipeDiary.UI.FavoriteDetailActivity;
import com.tarun.saini.recipeDiary.data.RecipeContract.FavoriteRecipeEntry;

/**
 * Created by Tarun on 01-08-2017.
 */

public class FavoriteCursorAdapter extends CursorAdapter

{
    public static final String RECIPE_ID ="Recipe Id" ;
    Context mContext;
    private OnFavoriteRecipeClickListener mClickListener;

    public interface OnFavoriteRecipeClickListener
    {
        void OnFavoriteRecipeSelected(String id);
    }
    public FavoriteCursorAdapter(Context context, Cursor c,OnFavoriteRecipeClickListener listener) {
        super(context, c,0);
        mClickListener=listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.recipe_card, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor)
    {
        ImageView recipeListImage = (ImageView) view.findViewById(R.id.recipe_list_image);
        TextView recipeListText= (TextView) view.findViewById(R.id.slide_textView);
        TextView secondaryText = (TextView) view.findViewById(R.id.secondary_item_textView);

        int nameColumnIndex = cursor.getColumnIndex(FavoriteRecipeEntry.RECIPE_NAME);
        int typeColumnIndex = cursor.getColumnIndex(FavoriteRecipeEntry.RECIPE_TYPE);
        int imageColumnIndex = cursor.getColumnIndex(FavoriteRecipeEntry.RECIPE_IMAGE);
        int idColumnIndex=cursor.getColumnIndex(FavoriteRecipeEntry.RECIPE_ID);

        String recipeName = cursor.getString(nameColumnIndex);
        String recipeType = cursor.getString(typeColumnIndex);
        String recipeImage = cursor.getString(imageColumnIndex);
        final String recipeId=cursor.getString(idColumnIndex);

        recipeListText.setText(recipeName);
        secondaryText.setText(recipeType);
        Glide.with(context).load(recipeImage).into(recipeListImage);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                mClickListener.OnFavoriteRecipeSelected(recipeId);

            }
        });

    }
}
