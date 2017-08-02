package com.tarun.saini.recipeDiary.UI;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kosalgeek.android.photoutil.MainActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tarun.saini.recipeDiary.Adapter.RecipeAdapter;
import com.tarun.saini.recipeDiary.Extras.ConnectivityCheck;
import com.tarun.saini.recipeDiary.Extras.MyApplication;
import com.tarun.saini.recipeDiary.Model.Recipe;
import com.tarun.saini.recipeDiary.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.tarun.saini.recipeDiary.Adapter.RecipeAdapter.RECIPE;

public class RecipeActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener, NavigationView.OnNavigationItemSelectedListener {



    public static final String RECIPE_CALORIES = "calories";
    public static final String SERVES = "serves";
    public static final String TIME = "estimated_time";
    public static final String ID = "recipe_id";
    private static final String SAVE_STATE = "save_state";
    public static final String RECIPE_TYPE = "recipe_type";
    private Toolbar mToolbar;
    Context mContext;
    private boolean mTwoPane;
    public static final String POSITION = "position";
    public static final String PANES = "panes";
    public static final String RECIPE_STEPS="recipe steps";
    public static final String RECIPE_INGREDIENTS="recipe ingredients";
    public static final String RECIPE_NAME="name";
    public static final String RECIPE_CATEGORY="recipeCategory";
    public static final String RECIPE_IMAGE="imageUrl";
    TextView recipeUploadText,userEmail;
    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    Fragment fragmentRecipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            // User is not signed in
            finish();
            Intent intent=new Intent(RecipeActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        mToolbar.setFitsSystemWindows(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#20000000"));
        setupNavigationDrawerMenu();



        if (savedInstanceState==null)
        {

            fragmentRecipeList=new RecipeFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.recipe_list_container,fragmentRecipeList);
            fragmentTransaction.commit();
        }else
        {
            if(fragmentRecipeList!=null && fragmentRecipeList.isAdded())
            {

                fragmentRecipeList =  getSupportFragmentManager().getFragment(savedInstanceState,SAVE_STATE);

            }

        }


        if(mTwoPane = findViewById(R.id.recipe_linear_layout) != null)
        {
            mTwoPane=true;

        }
        else
        {
            mTwoPane=false;
        }

    }

    public static void setLatoRegular(Context context, TextView textView)
    {
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"lato_regular.ttf");
        textView.setTypeface(typeface);

    }
    public static void setLatoBold(Context context, TextView textView)
    {
        Typeface typefaceBold=Typeface.createFromAsset(context.getAssets(),"lato_bold.ttf");
        textView.setTypeface(typefaceBold);

    }
    public static void setLatoBlack(Context context, TextView textView)
    {
        Typeface typefaceBlack=Typeface.createFromAsset(context.getAssets(),"lato_black.ttf");
        textView.setTypeface(typefaceBlack);

    }


    @Override
    public void onRecipeSelected(int position,List<Recipe> recipe)
    {

        Bundle bundle = new Bundle();
        if (mTwoPane) {

            DetailFragment detailFragment = new DetailFragment();
            bundle.putInt(POSITION, recipe.size()-position-1);
            bundle.putBoolean(PANES, mTwoPane);
            bundle.putString(ID,recipe.get(recipe.size()-position-1).getId());
            bundle.putString(RECIPE_TYPE,recipe.get(recipe.size()-position-1).getRecipetype());
            bundle.putStringArrayList(RECIPE_STEPS,recipe.get(recipe.size()-position-1).getSteps());
            bundle.putParcelableArrayList(RECIPE_INGREDIENTS,recipe.get(recipe.size()-position-1).getIngredients());
            bundle.putString(RECIPE_NAME,recipe.get(recipe.size()-position-1).getName());
            bundle.putString(RECIPE_CATEGORY,recipe.get(recipe.size()-position-1).getRecipeCategory());
            bundle.putString(RECIPE_IMAGE,recipe.get(recipe.size()-position-1).getImage());
            bundle.putInt(RECIPE_CALORIES,recipe.get(recipe.size()-position-1).getCalories());
            bundle.putInt(SERVES,recipe.get(recipe.size()-position-1).getServes());
            bundle.putString(TIME,recipe.get(recipe.size()-position-1).getEstimatedtime());
            detailFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.recipe_detail_container, detailFragment);
            transaction.commit();
        }
        else
        {
            Intent intent = new Intent(this, DetailActivity.class);
            bundle.putInt(POSITION, position);
            bundle.putBoolean(PANES, mTwoPane);
            intent.putExtra(RECIPE, recipe.get(recipe.size()-position-1));
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity(intent);

        }
    }

    private void setupNavigationDrawerMenu() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        View header=navigationView.getHeaderView(0);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        userEmail= (TextView)header.findViewById(R.id.txvEmail);

        assert user != null;
       userEmail.setText(user.getEmail());
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        String itemName = (String) menuItem.getTitle();

        closeDrawer();

        Intent intent;

        switch (menuItem.getItemId()) {

            case R.id.home:
                // Put your Item specific Code here
                break;

            case R.id.upload_recipe:
                intent=new Intent(this,UploadActivity.class);
                startActivity(intent);

                break;

            case R.id.favourite_recipes:
                intent=new Intent(this,FavoriteRecipe.class);
                startActivity(intent);
                break;


            case R.id.logout:
                mAuth.signOut();
                finish();
                intent=new Intent(this,LoginActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
                break;
            case R.id.shopping_list:
                intent=new Intent(this,ShoppingListActivity.class);
                startActivity(intent);

        }

        return true;
    }

    // Close the Drawer
    private void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        if (fragmentRecipeList!=null && fragmentRecipeList.isAdded())
        {
            getSupportFragmentManager().putFragment(outState,SAVE_STATE,fragmentRecipeList);

        }
    }
}






