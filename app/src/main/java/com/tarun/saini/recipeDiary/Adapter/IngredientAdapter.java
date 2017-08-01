package com.tarun.saini.recipeDiary.Adapter;

import android.content.ContentUris;
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
import com.tarun.saini.recipeDiary.data.RecipeContract;
import com.tarun.saini.recipeDiary.widget.ShoppingListWidget;

import java.util.ArrayList;

import static com.tarun.saini.recipeDiary.data.RecipeContract.LIST_CONTENT_URI;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_ID;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_INGREDIENT;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_MEASURE;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_NAME;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_QUANTITY;

/**
 * Created by Tarun on 10-07-2017.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>  {

    private ArrayList<Ingredient> mIngredients;
    private Context mContext;
    private int position;

    public IngredientAdapter(ArrayList<Ingredient> mIngredients,Context context) {
        this.mIngredients = mIngredients;
        this.mContext=context;
    }

    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_layout,parent,false);
        final IngredientViewHolder vh=new IngredientViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=vh.getAdapterPosition();

                vh.mCheckBox.setChecked(!vh.mCheckBox.isChecked());

                if(vh.mCheckBox.isChecked())
                {
                    Snackbar.make(view,"Added to Shopping List ",Snackbar.LENGTH_SHORT).show();
                    addToShoppingList();

                }
                else
                {
                    Snackbar.make(view,"Removed from Shopping List",Snackbar.LENGTH_SHORT).show();
                    deleteItem(mIngredients.get(position).getId());

                }

            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.IngredientViewHolder holder, int position) {

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


        if (checkIfAdded(count.getId()))
        {
            holder.mCheckBox.setChecked(true);
        }
        else
            {
                holder.mCheckBox.setChecked(false);
            }


    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder
    {
        TextView ingredient;
        CheckBox mCheckBox;
        LinearLayout mIngredientLayout;
        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredient= (TextView) itemView.findViewById(R.id.ingredient);
            mCheckBox= (CheckBox) itemView.findViewById(R.id.checkboxAdd);
            mIngredientLayout= (LinearLayout) itemView.findViewById(R.id.ingredient_container);
        }


    }
    private void addToShoppingList()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                ContentValues values=new ContentValues();
                values.put(KEY_ID,mIngredients.get(position).getId());
                values.put(KEY_MEASURE,mIngredients.get(position).getMeasure());
                values.put(KEY_QUANTITY,mIngredients.get(position).getQuantity());
                values.put(KEY_INGREDIENT,mIngredients.get(position).getIngredient());
                mContext.getContentResolver().insert(LIST_CONTENT_URI,values);
                ShoppingListWidget.sendRefreshBroadcast(mContext);
                return null;
            }
        }.execute();



    }
    private void deleteItem(String id)
    {

        String selection = KEY_ID + "=?";
        String[] selectionArgs = new String[]{id};
        mContext.getContentResolver().delete(LIST_CONTENT_URI, selection, selectionArgs);
        ShoppingListWidget.sendRefreshBroadcast(mContext);

    }

    private boolean checkIfAdded(String id) {
        String[] projection = {KEY_ID};
        String selection = KEY_ID + "=?";
        String[] selectionArgs = new String[]{id};
        Cursor cursor = mContext.getContentResolver()
                .query(LIST_CONTENT_URI, projection, selection, selectionArgs, null);
        assert cursor != null;
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }



}
