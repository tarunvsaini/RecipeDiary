package com.tarun.saini.recipeDiary.UI;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tarun.saini.recipeDiary.Adapter.ShoppingListAdapter;
import com.tarun.saini.recipeDiary.Model.Ingredient;
import com.tarun.saini.recipeDiary.R;
import com.tarun.saini.recipeDiary.data.RecipeContract;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_ID;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_INGREDIENT;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_MEASURE;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_QUANTITY;

public class ShoppingListActivity extends AppCompatActivity {

    List<Ingredient> shoppingList;
    ShoppingListAdapter shoppingListAdapter;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shopping_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RelativeLayout emptyLayout = (RelativeLayout) findViewById(R.id.empty_view);
        shoppingList = getShoppingList();
        shoppingListAdapter = new ShoppingListAdapter(shoppingList, getApplicationContext());
        if (shoppingList.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setAdapter(shoppingListAdapter);
            emptyLayout.setVisibility(View.GONE);
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.shopping_list_title);
        setSupportActionBar(toolbar);
        toolbar.setFitsSystemWindows(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#20000000"));


    }


    public List<Ingredient> getShoppingList() {
        List<Ingredient> shoppingList = new ArrayList<>();
        String[] projection = {
                KEY_ID,
                KEY_QUANTITY,
                KEY_MEASURE,
                KEY_INGREDIENT
        };
        Cursor cursor = getContentResolver().query(RecipeContract.LIST_CONTENT_URI, projection, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient(Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_QUANTITY))),
                        cursor.getString(cursor.getColumnIndex(KEY_MEASURE)),
                        cursor.getString(cursor.getColumnIndex(KEY_INGREDIENT)),
                        cursor.getString(cursor.getColumnIndex(KEY_ID)));
                shoppingList.add(ingredient);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return shoppingList;
    }
}
