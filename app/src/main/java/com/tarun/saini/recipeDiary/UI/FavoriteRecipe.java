package com.tarun.saini.recipeDiary.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tarun.saini.recipeDiary.Adapter.FavoriteCursorAdapter;
import com.tarun.saini.recipeDiary.R;

public class FavoriteRecipe extends AppCompatActivity implements FavoriteCursorAdapter.OnFavoriteRecipeClickListener {


    private static final String SAVE_STATE = "save_state";
    public static final String PANES_F = "panes";
    public static final String FAVORITE_RECIPE_ID = "recipe id";
    private Toolbar mToolbar;
    Fragment fragmentFavoriteList;
    public boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_recipe);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.favorite_recipes);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setFitsSystemWindows(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#20000000"));

        fragmentFavoriteList = new FavoriteFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.favorite_list_container, fragmentFavoriteList);
        fragmentTransaction.commit();

        if (mTwoPane = findViewById(R.id.favorite_recipe_linear_layout) != null) {
            mTwoPane = true;

        } else {
            mTwoPane = false;
        }


    }

    @Override
    public void OnFavoriteRecipeSelected(String id) {

        Bundle bundle = new Bundle();
        if (mTwoPane) {

            FavoriteDetailFragment favoriteDetailFragment = new FavoriteDetailFragment();
            bundle.putBoolean(PANES_F, mTwoPane);
            bundle.putString(FAVORITE_RECIPE_ID, id);
            favoriteDetailFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.favorite_detail_container, favoriteDetailFragment);
            transaction.commit();
        } else {
            Intent intent = new Intent(this, FavoriteDetailActivity.class);
            bundle.putBoolean(PANES_F, mTwoPane);
            intent.putExtra(FAVORITE_RECIPE_ID, id);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtras(bundle);
            startActivity(intent);

        }


    }
}



