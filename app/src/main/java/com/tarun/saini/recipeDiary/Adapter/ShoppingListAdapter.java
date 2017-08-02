package com.tarun.saini.recipeDiary.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tarun.saini.recipeDiary.Model.Ingredient;
import com.tarun.saini.recipeDiary.R;
import com.tarun.saini.recipeDiary.widget.ShoppingListWidget;

import java.util.List;

import static com.tarun.saini.recipeDiary.data.RecipeContract.LIST_CONTENT_URI;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_ID;

/**
 * Created by Tarun on 30-07-2017.
 */

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>
{

    private List<Ingredient> mShoppingList;
    private Context mContext;

    public ShoppingListAdapter(List<Ingredient> shoppingList, Context context)
    {
        this.mShoppingList=shoppingList;
        this.mContext=context;
    }

    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shopping_list,parent,false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShoppingListAdapter.ShoppingListViewHolder holder, final int position)
    {
        final Ingredient ingredient=mShoppingList.get(position);
        holder.item_name.setText(ingredient.getIngredient());
        holder.item_requirement.setText(ingredient.getMeasure());
        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                removeItem(position);
                deleteItem(ingredient.getId());
                
            }
        });
    }

    private void removeItem(int position)
    {
        mShoppingList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mShoppingList.size());

    }

    @Override
    public int getItemCount() {

        return mShoppingList.size();

    }

    public class ShoppingListViewHolder extends RecyclerView.ViewHolder
    {

        TextView item_name;
        TextView item_requirement;
        ImageButton remove_item;
        public ShoppingListViewHolder(View itemView) {
            super(itemView);

            item_name= (TextView) itemView.findViewById(R.id.shopping_item);
            item_requirement= (TextView) itemView.findViewById(R.id.shopping_item_quantity);
            remove_item= (ImageButton) itemView.findViewById(R.id.remove_item);
        }
    }

    private void deleteItem(String id)
    {

        String selection = KEY_ID + "=?";
        String[] selectionArgs = new String[]{id};
        mContext.getContentResolver().delete(LIST_CONTENT_URI, selection, selectionArgs);
        ShoppingListWidget.sendRefreshBroadcast(mContext);

    }
}
