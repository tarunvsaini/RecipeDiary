package com.tarun.saini.recipeDiary.UI;


import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tarun.saini.recipeDiary.Adapter.IngredientAdapter;
import com.tarun.saini.recipeDiary.Adapter.StepAdapter;
import com.tarun.saini.recipeDiary.Model.Ingredient;
import com.tarun.saini.recipeDiary.Model.Recipe;
import com.tarun.saini.recipeDiary.R;
import com.tarun.saini.recipeDiary.data.RecipeContract;
import com.tarun.saini.recipeDiary.data.RecipeContract.FavoriteRecipeEntry;
import com.tarun.saini.recipeDiary.data.RecipeContract.IngredientEntry;

import java.util.ArrayList;

import static com.tarun.saini.recipeDiary.Adapter.RecipeAdapter.RECIPE;
import static com.tarun.saini.recipeDiary.UI.RecipeActivity.ID;
import static com.tarun.saini.recipeDiary.UI.RecipeActivity.PANES;
import static com.tarun.saini.recipeDiary.UI.RecipeActivity.RECIPE_CALORIES;
import static com.tarun.saini.recipeDiary.UI.RecipeActivity.RECIPE_CATEGORY;
import static com.tarun.saini.recipeDiary.UI.RecipeActivity.RECIPE_IMAGE;
import static com.tarun.saini.recipeDiary.UI.RecipeActivity.RECIPE_INGREDIENTS;
import static com.tarun.saini.recipeDiary.UI.RecipeActivity.RECIPE_NAME;
import static com.tarun.saini.recipeDiary.UI.RecipeActivity.RECIPE_STEPS;
import static com.tarun.saini.recipeDiary.UI.RecipeActivity.RECIPE_TYPE;
import static com.tarun.saini.recipeDiary.UI.RecipeActivity.SERVES;
import static com.tarun.saini.recipeDiary.UI.RecipeActivity.TIME;
import static com.tarun.saini.recipeDiary.data.RecipeContract.FAVORITE_CONTENT_URI;
import static com.tarun.saini.recipeDiary.data.RecipeContract.FavoriteRecipeEntry.RECIPE_ID;
import static com.tarun.saini.recipeDiary.data.RecipeContract.INGREDIENT_CONTENT_URI;
import static com.tarun.saini.recipeDiary.data.RecipeContract.LIST_CONTENT_URI;
import static com.tarun.saini.recipeDiary.data.RecipeContract.STEP_CONTENT_URI;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


    private CoordinatorLayout coordinatorLayout;
    private RecyclerView ingredientRecyclerView, stepRecyclerView;
    private CollapsingToolbarLayout collapsingToolbar;
    private Recipe mRecipe;
    private CheckBox addFavorite;
    private ImageView mBackdrop;
    private Toolbar toolbar;
    AppBarLayout appBarLayout;
    Boolean mTwoPane;
    TextView recipeName, recipeCategory, ingredient_title, directions, calories, time, serves;
    StepAdapter mStepAdapter;
    IngredientAdapter mIngredientAdapter;
    String name, servings, category, recipeCalories, estimatedTime, id, image, type;
    ArrayList<Ingredient> ingredients;
    ArrayList<String> steps;
    boolean collapsed;

    private static final String SAVE_STEPS = "saveSteps";
    private static final String SAVE_INGREDIENT = "saveIngredients";
    private static final String SAVE_NAME = "saveRecipeName";
    private static final String SAVE_CATEGORY = "saveRecipeCategory";
    private static final String SAVE_CALORIES = "saveRecipeCalories";
    private static final String SAVE_TIME = "saveRecipeTime";
    private static final String SAVE_SERVES = "Saveserves";
    private static final String SAVE_COLLAPSE_STATE = "collapsed";

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        recipeName = (TextView) rootView.findViewById(R.id.recipeName);
        recipeCategory = (TextView) rootView.findViewById(R.id.recipeCategory);
        ingredient_title = (TextView) rootView.findViewById(R.id.ingredient_title);
        directions = (TextView) rootView.findViewById(R.id.steps_title);
        mBackdrop = (ImageView) rootView.findViewById(R.id.backdrop);
        calories = (TextView) rootView.findViewById(R.id.calories);
        time = (TextView) rootView.findViewById(R.id.prep_time);
        serves = (TextView) rootView.findViewById(R.id.serves);
        addFavorite = (CheckBox) rootView.findViewById(R.id.addFavorite);


        RecipeActivity.setLatoBlack(getActivity(), recipeName);
        RecipeActivity.setLatoBlack(getActivity(), ingredient_title);
        RecipeActivity.setLatoBlack(getActivity(), directions);
        RecipeActivity.setLatoRegular(getActivity(), recipeCategory);

        RecipeActivity.setLatoRegular(getActivity(), serves);
        RecipeActivity.setLatoRegular(getActivity(), calories);
        RecipeActivity.setLatoRegular(getActivity(), time);

        ingredientRecyclerView = (RecyclerView) rootView.findViewById(R.id.ingredient_recyclerView);
        ingredientRecyclerView.setNestedScrollingEnabled(false);
        ingredientRecyclerView.setHasFixedSize(true);
        ingredientRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        stepRecyclerView = (RecyclerView) rootView.findViewById(R.id.steps_recyclerView);
        stepRecyclerView.setNestedScrollingEnabled(false);
        stepRecyclerView.setHasFixedSize(true);
        stepRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((addFavorite.isChecked())) //check for condition if recipe in database
                {
                    addRecipes();
                    addIngredient(ingredients);
                    addSteps(steps);
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Added to Favourite Recipes", Snackbar.LENGTH_LONG);


                    snackbar.show();

                } else {
                    deleteRecipe(id);
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Removed From Favourite Recipes", Snackbar.LENGTH_LONG);


                    snackbar.show();

                }

            }
        });

        mTwoPane = getArguments().getBoolean(PANES);

        if (mTwoPane) {
            tabView();
        } else {
            phoneView();
        }

        if (checkRecipeId(id)) {

            addFavorite.setChecked(true);
        } else {
            addFavorite.setChecked(false);

        }


        collapsingToolbar =
                (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appBar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {


                if (!mTwoPane) {


                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }

                    if (scrollRange + verticalOffset == 0) {
                        collapsingToolbar.setTitle(mRecipe.getName());
                        isShow = true;
                        collapsed = false;
                    } else if (isShow) {
                        collapsingToolbar.setTitle(" ");
                        isShow = false;
                        collapsed = true;
                    }
                } else {
                    if (verticalOffset == 0) {

                        collapsed = true;
                    } else {
                        collapsed = false;
                    }

                }

            }

        });

        if (savedInstanceState != null) {


            name = savedInstanceState.getString(SAVE_NAME);
            servings = savedInstanceState.getString(SAVE_SERVES);
            category = savedInstanceState.getString(SAVE_CATEGORY);
            recipeCalories = savedInstanceState.getString(SAVE_CALORIES);
            estimatedTime = savedInstanceState.getString(SAVE_TIME);
            steps = savedInstanceState.getStringArrayList(SAVE_STEPS);
            ingredients = savedInstanceState.getParcelableArrayList(SAVE_INGREDIENT);
            collapsed = savedInstanceState.getBoolean(SAVE_COLLAPSE_STATE);
            appBarLayout.setExpanded(collapsed);
            setValues();

        } else {
            setValues();
        }


        return rootView;
    }


    public void phoneView() {
        mRecipe = getActivity().getIntent().getParcelableExtra(RECIPE);
        name = mRecipe.getName();
        category = mRecipe.getRecipeCategory();
        image = mRecipe.getImage();
        type = mRecipe.getRecipetype();
        Glide.with(getContext()).load(image).into(mBackdrop);
        recipeCalories = String.valueOf(mRecipe.getCalories()) + getString(R.string.calories_string);
        estimatedTime = mRecipe.getEstimatedtime();
        servings = getString(R.string.serves_string) + String.valueOf(mRecipe.getServes());
        ingredients = mRecipe.getIngredients();
        steps = mRecipe.getSteps();
        id = mRecipe.getId();
        setValues();


    }

    public void tabView() {

        ingredients = getArguments().getParcelableArrayList(RECIPE_INGREDIENTS);
        steps = getArguments().getStringArrayList(RECIPE_STEPS);
        name = getArguments().getString(RECIPE_NAME);
        id = getArguments().getString(ID);
        category = getArguments().getString(RECIPE_CATEGORY);
        type = getArguments().getString(RECIPE_TYPE);
        image = getArguments().getString(RECIPE_IMAGE);
        Glide.with(getContext()).load(image).into(mBackdrop);
        recipeCalories = String.valueOf(getArguments().getInt(RECIPE_CALORIES)) + getString(R.string.calories_string);
        estimatedTime = getArguments().getString(TIME);
        servings = getString(R.string.serves_string) + String.valueOf(getArguments().getInt(SERVES));
        toolbar.setVisibility(View.GONE);
        setValues();


    }

    private void setValues() {

        serves.setText(servings);
        time.setText(estimatedTime);
        recipeName.setText(name);
        recipeCategory.setText(category);
        calories.setText(recipeCalories);
        mIngredientAdapter = new IngredientAdapter(ingredients, getActivity());
        ingredientRecyclerView.setAdapter(mIngredientAdapter);
        mStepAdapter = new StepAdapter(steps, getActivity());
        stepRecyclerView.setAdapter(mStepAdapter);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_NAME, name);
        outState.putString(SAVE_SERVES, servings);
        outState.putString(SAVE_TIME, estimatedTime);
        outState.putString(SAVE_CALORIES, recipeCalories);
        outState.putStringArrayList(SAVE_STEPS, steps);
        outState.putParcelableArrayList(SAVE_INGREDIENT, ingredients);
        outState.putBoolean(SAVE_COLLAPSE_STATE, collapsed);
    }

    private void addRecipes() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ContentValues values = new ContentValues();
                values.put(FavoriteRecipeEntry.RECIPE_ID, id);
                values.put(FavoriteRecipeEntry.RECIPE_IMAGE, image);
                values.put(FavoriteRecipeEntry.RECIPE_NAME, name);
                values.put(FavoriteRecipeEntry.RECIPE_SERVES, servings);
                values.put(FavoriteRecipeEntry.RECIPE_CATEGORY, category);
                values.put(FavoriteRecipeEntry.RECIPE_CALORIES, recipeCalories);
                values.put(FavoriteRecipeEntry.RECIPE_TIME, estimatedTime);
                values.put(FavoriteRecipeEntry.RECIPE_TYPE, type);
                getActivity().getContentResolver().insert(FAVORITE_CONTENT_URI, values);
                return null;
            }
        }.execute();


    }


    public void deleteRecipe(String id) {

        String selection = RECIPE_ID + "=?";
        String[] selectionArgs = new String[]{id};
        getActivity().getContentResolver().delete(FAVORITE_CONTENT_URI, selection, selectionArgs);
        getActivity().getContentResolver().delete(INGREDIENT_CONTENT_URI, selection, selectionArgs);
        getActivity().getContentResolver().delete(STEP_CONTENT_URI, selection, selectionArgs);
    }


    //check if movie is already in database

    public boolean checkRecipeId(String id) {

        String[] projection = {RECIPE_ID};
        String selection = RECIPE_ID + "=?";
        String[] selectionArgs = new String[]{id};
        Cursor cursor = getActivity().getContentResolver().query(FAVORITE_CONTENT_URI, projection, selection, selectionArgs, null);
        assert cursor != null;
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }


    private void addIngredient(final ArrayList<Ingredient> ingredients) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for (Ingredient i : ingredients) {
                    ContentValues v = new ContentValues();
                    v.put(IngredientEntry.KEY_ID, id);
                    v.put(IngredientEntry.KEY_NAME, name);
                    v.put(IngredientEntry.KEY_MEASURE, i.getMeasure());
                    v.put(IngredientEntry.KEY_QUANTITY, i.getQuantity());
                    v.put(IngredientEntry.KEY_INGREDIENT, i.getIngredient());
                    getActivity().getContentResolver().insert(INGREDIENT_CONTENT_URI, v);
                }
                return null;
            }
        }.execute();


    }

    private void addSteps(final ArrayList<String> steps) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for (int j = 0; j < steps.size(); j++) {
                    ContentValues vr = new ContentValues();
                    vr.put(RecipeContract.FavoriteRecipeSteps.RECIPE_ID, id);
                    vr.put(RecipeContract.FavoriteRecipeSteps.RECIPE_NAME, name);
                    vr.put(RecipeContract.FavoriteRecipeSteps.RECIPE_STEPS, steps.get(j));

                    getActivity().getContentResolver().insert(STEP_CONTENT_URI, vr);
                }
                return null;
            }
        }.execute();

    }


}

