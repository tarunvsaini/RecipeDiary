package com.tarun.saini.recipeDiary.UI;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
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
import com.tarun.saini.recipeDiary.Adapter.FavoriteCursorAdapter;
import com.tarun.saini.recipeDiary.Adapter.FavoriteIngredientAdapter;
import com.tarun.saini.recipeDiary.Adapter.IngredientAdapter;
import com.tarun.saini.recipeDiary.Adapter.StepAdapter;
import com.tarun.saini.recipeDiary.Model.Ingredient;
import com.tarun.saini.recipeDiary.Model.Recipe;
import com.tarun.saini.recipeDiary.R;
import com.tarun.saini.recipeDiary.data.RecipeContract;
import com.tarun.saini.recipeDiary.data.RecipeContract.FavoriteRecipeEntry;
import com.tarun.saini.recipeDiary.data.RecipeContract.FavoriteRecipeSteps;
import com.tarun.saini.recipeDiary.data.RecipeContract.IngredientEntry;

import java.util.ArrayList;
import java.util.List;

import static com.tarun.saini.recipeDiary.UI.FavoriteRecipe.FAVORITE_RECIPE_ID;
import static com.tarun.saini.recipeDiary.UI.FavoriteRecipe.PANES_F;
import static com.tarun.saini.recipeDiary.data.RecipeContract.FAVORITE_CONTENT_URI;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_ID;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_INGREDIENT;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_MEASURE;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_QUANTITY;


public class FavoriteDetailFragment extends Fragment {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView ingredientRecyclerView, stepRecyclerView;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView mBackdrop;
    private Toolbar toolbar;
    AppBarLayout appBarLayout;
    Boolean mTwoPane;
    TextView recipeName, recipeCategory, ingredient_title, detail_title, directions, details, calories, time, serves;
    StepAdapter mStepAdapter;
    FavoriteIngredientAdapter mIngredientAdapter;
    String name, servings, category, recipeCalories, estimatedTime, id, image, type;
    ArrayList<Ingredient> ingredients;
    ArrayList<String> steps;
    boolean collapsed;
    NestedScrollView mScrollView;


    private static final String ARTICLE_SCROLL_POSITION = "ARTICLE_SCROLL_POSITION";
    private static final String SAVE_COLLAPSE_STATE = "collapsed";

    public FavoriteDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite_detail, container, false);


        recipeName = (TextView) rootView.findViewById(R.id.recipeName);
        recipeCategory = (TextView) rootView.findViewById(R.id.recipeCategory);
        ingredient_title = (TextView) rootView.findViewById(R.id.ingredient_title);
        directions = (TextView) rootView.findViewById(R.id.steps_title);
        mBackdrop = (ImageView) rootView.findViewById(R.id.backdrop);
        calories = (TextView) rootView.findViewById(R.id.calories);
        time = (TextView) rootView.findViewById(R.id.prep_time);
        serves = (TextView) rootView.findViewById(R.id.serves);
        mScrollView = (NestedScrollView) rootView.findViewById(R.id.nestedScrollView);


        RecipeActivity.setLatoBlack(getActivity(), recipeName);
        RecipeActivity.setLatoBlack(getActivity(), ingredient_title);
        RecipeActivity.setLatoBlack(getActivity(), directions);
        RecipeActivity.setLatoRegular(getActivity(), recipeCategory);

        RecipeActivity.setLatoRegular(getActivity(), serves);
        RecipeActivity.setLatoRegular(getActivity(), calories);
        RecipeActivity.setLatoRegular(getActivity(), time);

        ingredientRecyclerView = (RecyclerView) rootView.findViewById(R.id.favorite_ingredient_recyclerView);
        ingredientRecyclerView.setNestedScrollingEnabled(false);
        ingredientRecyclerView.setHasFixedSize(true);
        ingredientRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        stepRecyclerView = (RecyclerView) rootView.findViewById(R.id.favorite_steps_recyclerView);
        stepRecyclerView.setNestedScrollingEnabled(false);
        stepRecyclerView.setHasFixedSize(true);
        stepRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mTwoPane = getArguments().getBoolean(PANES_F);
        if (mTwoPane) {
            tabView();
        } else {
            phoneView();
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
                        collapsingToolbar.setTitle(name);
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
            collapsed = savedInstanceState.getBoolean(SAVE_COLLAPSE_STATE);
            appBarLayout.setExpanded(collapsed);
            final int[] position = savedInstanceState.getIntArray(ARTICLE_SCROLL_POSITION);
            if (position != null) {
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.scrollTo(position[1], position[1]);
                    }
                });
            }
        }

        return rootView;
    }


    public void phoneView() {

        Bundle extras = getActivity().getIntent().getExtras();
        id = extras.getString(FAVORITE_RECIPE_ID);
        getRecipeDetails();
        setValues();


    }

    public void tabView() {

        id = getArguments().getString(FAVORITE_RECIPE_ID);
        getRecipeDetails();
        setValues();
        toolbar.setVisibility(View.GONE);

    }

    private void setValues() {

        serves.setText(servings);
        time.setText(estimatedTime);
        recipeName.setText(name);
        recipeCategory.setText(category);
        calories.setText(recipeCalories);
        Glide.with(getContext()).load(image).into(mBackdrop);
        ingredients = getRecipeIngredients();
        mIngredientAdapter = new FavoriteIngredientAdapter(ingredients, getActivity());
        ingredientRecyclerView.setAdapter(mIngredientAdapter);
        steps = getRecipeSteps();
        mStepAdapter = new StepAdapter(steps, getActivity());
        stepRecyclerView.setAdapter(mStepAdapter);
    }


    private void getRecipeDetails() {
        String[] projection = {
                FavoriteRecipeEntry._ID,
                FavoriteRecipeEntry.RECIPE_NAME,
                FavoriteRecipeEntry.RECIPE_IMAGE,
                FavoriteRecipeEntry.RECIPE_CALORIES,
                FavoriteRecipeEntry.RECIPE_CATEGORY,
                FavoriteRecipeEntry.RECIPE_TIME,
                FavoriteRecipeEntry.RECIPE_SERVES};
        String selection = FavoriteRecipeEntry.RECIPE_ID + "=?";
        String[] selectionArgs = new String[]{id};
        Cursor cursor = getActivity().getContentResolver().query(FAVORITE_CONTENT_URI, projection, selection, selectionArgs, null);


        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(FavoriteRecipeEntry.RECIPE_NAME));
            category = cursor.getString(cursor.getColumnIndex(FavoriteRecipeEntry.RECIPE_CATEGORY));
            image = cursor.getString(cursor.getColumnIndex(FavoriteRecipeEntry.RECIPE_IMAGE));
            recipeCalories = cursor.getString(cursor.getColumnIndex(FavoriteRecipeEntry.RECIPE_CALORIES));
            estimatedTime = cursor.getString(cursor.getColumnIndex(FavoriteRecipeEntry.RECIPE_TIME));
            servings = cursor.getString(cursor.getColumnIndex(FavoriteRecipeEntry.RECIPE_SERVES));

        }
        assert cursor != null;
        cursor.close();


    }

    private ArrayList<Ingredient> getRecipeIngredients() {
        ArrayList<Ingredient> ingredientsList = new ArrayList<>();
        String[] projection = {
                IngredientEntry.KEY_ID,
                IngredientEntry.KEY_QUANTITY,
                IngredientEntry.KEY_MEASURE,
                IngredientEntry.KEY_INGREDIENT
        };
        String selection = IngredientEntry.KEY_ID + "=?";
        String[] selectionArgs = new String[]{id};
        Cursor cursor = getActivity().getContentResolver().query(RecipeContract.INGREDIENT_CONTENT_URI, projection, selection, selectionArgs, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient(Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_QUANTITY))),
                        cursor.getString(cursor.getColumnIndex(KEY_MEASURE)),
                        cursor.getString(cursor.getColumnIndex(KEY_INGREDIENT)),
                        cursor.getString(cursor.getColumnIndex(KEY_ID)));
                ingredientsList.add(ingredient);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return ingredientsList;
    }

    private ArrayList<String> getRecipeSteps() {

        ArrayList<String> steps = new ArrayList<>();
        String[] projection = {
                FavoriteRecipeSteps.RECIPE_STEPS
        };
        String selection = FavoriteRecipeSteps.RECIPE_ID + "=?";
        String[] selectionArgs = new String[]{id};
        Cursor cursor = getActivity().getContentResolver().query(RecipeContract.STEP_CONTENT_URI, projection, selection, selectionArgs, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            do {
                steps.add(cursor.getString(cursor.getColumnIndex(FavoriteRecipeSteps.RECIPE_STEPS)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return steps;

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_COLLAPSE_STATE, collapsed);
        outState.putIntArray(ARTICLE_SCROLL_POSITION,
                new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});
    }

}
