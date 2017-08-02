package com.tarun.saini.recipeDiary.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarun.saini.recipeDiary.Adapter.RecipeAdapter;
import com.tarun.saini.recipeDiary.Extras.ConnectivityCheck;
import com.tarun.saini.recipeDiary.Extras.MyApplication;
import com.tarun.saini.recipeDiary.Model.Recipe;
import com.tarun.saini.recipeDiary.Network.RetrofitClient;
import com.tarun.saini.recipeDiary.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tarun on 08-07-2017.
 */

public class RecipeFragment extends Fragment implements ConnectivityCheck.ConnectivityReceiverListener {

    private RecyclerView mRecyclerView;
    private RecipeAdapter.OnRecipeClickListener mCallBack;
    private static final String SAVE_STATE = "saveState";
    TextView noNetwork;
    CoordinatorLayout coordinatorLayout;
    ArrayList<Recipe> recipeList;
    RecipeAdapter recipeAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallBack = (RecipeAdapter.OnRecipeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeClickListener");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        noNetwork = (TextView) rootView.findViewById(R.id.no_internet_tv);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayoutRecipeList);


        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if ((position + 1) % 5 == 0) {
                    return 2;
                } else {

                    return 1;
                }

            }
        });
        mRecyclerView.setLayoutManager(mGridLayoutManager);



        if (savedInstanceState == null) {

            getRecipes();
            checkConnection();

        } else {
            recipeList = savedInstanceState.getParcelableArrayList(SAVE_STATE);
            if (recipeList != null) {
                recipeAdapter=new RecipeAdapter(recipeList, getActivity(), mCallBack);
                mRecyclerView.setAdapter(recipeAdapter);

            }

        }


        return rootView;
    }


    private void getRecipes() {
        RetrofitClient retrofitClient = new RetrofitClient();
        Call<ArrayList<Recipe>> call = retrofitClient.getRecipeService().getAllRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                recipeList = response.body();
                recipeAdapter=new RecipeAdapter(recipeList, getActivity(), mCallBack);
                mRecyclerView.setAdapter(recipeAdapter);

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d("JSON fAILURE", t + "");

            }
        });

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityCheck.isConnected();
        String message;
        if (isConnected) {
            message = getString(R.string.connected_to_internet);
            getRecipes();
            noNetwork.setVisibility(View.GONE);

        } else {
            message = getString(R.string.offline_message);
            noNetwork.setVisibility(View.VISIBLE);

        }
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        String message;
        if (isConnected) {
            message = getString(R.string.connected_to_internet);
            getRecipes();
            noNetwork.setVisibility(View.GONE);

        } else {
            message = getString(R.string.offline_message);
            noNetwork.setVisibility(View.VISIBLE);

        }
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_STATE, recipeList);

    }


}
