package com.tarun.saini.recipeDiary.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tarun.saini.recipeDiary.Model.Ingredient;
import com.tarun.saini.recipeDiary.R;
import com.tarun.saini.recipeDiary.UI.DetailActivity;
import com.tarun.saini.recipeDiary.UI.RecipeActivity;
import com.tarun.saini.recipeDiary.widget.ShoppingListWidget;

import java.util.ArrayList;

import static com.tarun.saini.recipeDiary.data.RecipeContract.LIST_CONTENT_URI;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_ID;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_INGREDIENT;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_MEASURE;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_QUANTITY;

/**
 * Created by Tarun on 10-07-2017.
 */

public class FavoriteIngredientAdapter extends RecyclerView.Adapter<FavoriteIngredientAdapter.IngredientViewHolder>  {

    private ArrayList<Ingredient> mIngredients;
    private Context mContext;
    private int position;

    public FavoriteIngredientAdapter(ArrayList<Ingredient> mIngredients, Context context) {
        this.mIngredients = mIngredients;
        this.mContext=context;
    }

    @Override
    public FavoriteIngredientAdapter.IngredientViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_ingredient_layout,parent,false);
        final IngredientViewHolder vh=new IngredientViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(FavoriteIngredientAdapter.IngredientViewHolder holder, int position) {

        Ingredient count=mIngredients.get(position);
        holder.ingredient.setText(String.valueOf(count.getQuantity())+" "+count.getMeasure()+" "+count.getIngredient());
        if (position % 2==0)
        {
            holder.mIngredientLayout.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
        else
        {
            holder.mIngredientLayout.setBackgroundColor(Color.parseColor("#fafafa"));

        }
        RecipeActivity.setLatoRegular(mContext,holder.ingredient);

    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder
    {
        TextView ingredient;
        LinearLayout mIngredientLayout;
        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredient= (TextView) itemView.findViewById(R.id.ingredient);
            mIngredientLayout= (LinearLayout) itemView.findViewById(R.id.ingredient_container);
        }


    }
}
