package com.tarun.saini.recipeDiary.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tarun.saini.recipeDiary.Model.Ingredient;
import com.tarun.saini.recipeDiary.R;
import com.tarun.saini.recipeDiary.UI.RecipeActivity;

import java.util.ArrayList;

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
