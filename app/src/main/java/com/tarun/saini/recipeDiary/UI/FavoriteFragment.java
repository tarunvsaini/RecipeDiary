package com.tarun.saini.recipeDiary.UI;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.tarun.saini.recipeDiary.Adapter.FavoriteCursorAdapter;
import com.tarun.saini.recipeDiary.R;
import com.tarun.saini.recipeDiary.data.RecipeContract.FavoriteRecipeEntry;

import static com.tarun.saini.recipeDiary.data.RecipeContract.FAVORITE_CONTENT_URI;


public class FavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    FavoriteCursorAdapter favoriteCursorAdapter;
    static final int RECIPE_LOADER = 1;

    private FavoriteCursorAdapter.OnFavoriteRecipeClickListener mCallBack;


    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallBack = (FavoriteCursorAdapter.OnFavoriteRecipeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMovieClickListener");
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        GridView recipeList = (GridView) rootView.findViewById(R.id.grid);

        View emptyView = rootView.findViewById(R.id.empty_view);
        recipeList.setEmptyView(emptyView);

        favoriteCursorAdapter = new FavoriteCursorAdapter(getActivity(), null, mCallBack);
        recipeList.setAdapter(favoriteCursorAdapter);
        getLoaderManager().initLoader(RECIPE_LOADER, null, this);


        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                FavoriteRecipeEntry._ID,
                FavoriteRecipeEntry.RECIPE_NAME,
                FavoriteRecipeEntry.RECIPE_TYPE,
                FavoriteRecipeEntry.RECIPE_IMAGE,
                FavoriteRecipeEntry.RECIPE_ID};

        return new CursorLoader(
                getActivity(),
                FAVORITE_CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favoriteCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favoriteCursorAdapter.swapCursor(null);
    }

}
