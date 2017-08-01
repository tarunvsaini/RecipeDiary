package com.tarun.saini.recipeDiary.UI;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tarun.saini.recipeDiary.Model.Recipe;
import com.tarun.saini.recipeDiary.R;

import static com.tarun.saini.recipeDiary.Adapter.RecipeAdapter.RECIPE;
import static com.tarun.saini.recipeDiary.UI.RecipeActivity.PANES;

public class DetailActivity extends AppCompatActivity {

    private static final String SAVE_STATE = "save_state";
    private Recipe mRecipe;
    private Fragment detailFragment;
    private static final String TAG = "MainActivity";
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mRecipe = getIntent().getParcelableExtra(RECIPE);
        boolean mTwoPane = getIntent().getBooleanExtra(PANES, false);
        Bundle bundle = new Bundle();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (savedInstanceState == null) {
            detailFragment = new DetailFragment();
            bundle.putBoolean(PANES, mTwoPane);
            detailFragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.recipe_detail_container, detailFragment);
            transaction.commit();
        } else {
            if (detailFragment != null && detailFragment.isAdded()) {
                detailFragment = getSupportFragmentManager().getFragment(savedInstanceState, SAVE_STATE);


            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (detailFragment != null && detailFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, SAVE_STATE, detailFragment);

        }
    }
}
