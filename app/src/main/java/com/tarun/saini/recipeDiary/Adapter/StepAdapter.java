package com.tarun.saini.recipeDiary.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tarun.saini.recipeDiary.R;
import com.tarun.saini.recipeDiary.UI.RecipeActivity;

import java.util.ArrayList;

/**
 * Created by Tarun on 10-07-2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>  {

    private ArrayList<String> mSteps;
    private Context mContext;

    public StepAdapter(ArrayList<String> steps,Context context)
    {
        this.mSteps=steps;
        this.mContext=context;
    }

    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_view_layout,parent,false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepAdapter.StepViewHolder holder, int position) {

        String current=mSteps.get(position);



        holder.stepDescription.setText(current);

        holder.stepNumber.setText(String.valueOf(position+1)+".");
        if (position % 2==0)
        {
            holder.mStepLayout.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
        else
        {
            holder.mStepLayout.setBackgroundColor(Color.parseColor("#fafafa"));

        }

        RecipeActivity.setLatoRegular(mContext,holder.stepDescription);
        RecipeActivity.setLatoBold(mContext,holder.stepNumber);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {

        TextView stepNumber,stepDescription;
        LinearLayout mStepLayout;

        public StepViewHolder(View itemView) {
            super(itemView);

            stepNumber= (TextView) itemView.findViewById(R.id.step_number);
            stepDescription= (TextView) itemView.findViewById(R.id.step_description);
            mStepLayout= (LinearLayout) itemView.findViewById(R.id.step_container);
        }
    }
}
