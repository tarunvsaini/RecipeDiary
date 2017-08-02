package com.tarun.saini.recipeDiary.UI;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tarun.saini.recipeDiary.Model.Recipe;
import com.tarun.saini.recipeDiary.R;

import static com.tarun.saini.recipeDiary.UI.FavoriteRecipe.PANES_F;

public class FavoriteDetailActivity extends AppCompatActivity {

    private static final String SAVE_STATE = "save_state";
    private Recipe mRecipe;
    private Fragment detailFragment;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_detail);


        boolean mTwoPane = getIntent().getBooleanExtra(PANES_F, false);
        Bundle bundle = new Bundle();

        if (savedInstanceState == null) {
            detailFragment = new FavoriteDetailFragment();
            bundle.putBoolean(PANES_F, mTwoPane);
            detailFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.favorite_detail_container, detailFragment);
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
