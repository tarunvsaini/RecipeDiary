package com.tarun.saini.recipeDiary.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tarun.saini.recipeDiary.Model.Recipe;
import com.tarun.saini.recipeDiary.R;
import com.tarun.saini.recipeDiary.UI.RecipeActivity;

import java.util.List;

/**
 * Created by Tarun on 09-07-2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>
{
    public static final String RECIPE = "recipe";
    private List<Recipe> mRecipes;
    private Context mContext;
    View mView;
    private final OnRecipeClickListener mClickListener;


    public interface OnRecipeClickListener
    {
        void onRecipeSelected(int position,List<Recipe> recipe);
    }



    public RecipeAdapter(List<Recipe> mRecipes, Context mContext,OnRecipeClickListener listener) {
        this.mRecipes = mRecipes;
        this.mContext = mContext;
        mClickListener=listener;
    }

    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card,parent,false);
        final RecipeViewHolder recipeViewHolder=new RecipeViewHolder(mView);

     /*   mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });*/
        return recipeViewHolder ;

    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeViewHolder holder, int position) {
        Recipe current=mRecipes.get(getItemCount()-position-1);

        holder.recipeListText.setText(current.getName());
        holder.secondaryText.setText(current.getRecipetype());
        Glide.with(mContext).load(current.getImage()).into(holder.recipeListImage);
           /* Glide.with(mContext).load(current.getImage()).listener(GlidePalette.with(current.getImage())
                    .use(GlidePalette.Profile.VIBRANT)
                    .intoBackground(holder.cardView)).into(holder.slideImage);*/


        RecipeActivity.setLatoBlack(mContext,holder.recipeListText);
        RecipeActivity.setLatoRegular(mContext,holder.secondaryText);
        RecipeActivity.setLatoBold(mContext,holder.recipeListText);

        /*if ((position + 1) % 5 == 0)
        {
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) holder.cardView.getLayoutParams();
            marginParams.setMargins(8, 8, 8, 8);
            holder.cardView.setLayoutParams(marginParams);
        }*/




    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView recipeListImage;
        private CardView cardView;

        private TextView recipeListText, secondaryText;
        public RecipeViewHolder(View itemView) {
            super(itemView);

            recipeListImage = (ImageView) itemView.findViewById(R.id.recipe_list_image);
            recipeListText= (TextView) itemView.findViewById(R.id.slide_textView);
            cardView= (CardView) itemView.findViewById(R.id.recipe_cardView);
            secondaryText = (TextView) itemView.findViewById(R.id.secondary_item_textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position=getAdapterPosition();
                    mClickListener.onRecipeSelected(position,mRecipes);

                }
            });
        }
    }
    //private final OnRecipeClickListener mClickListener;

}
