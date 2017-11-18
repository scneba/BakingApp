package com.clasence.shu.bakingapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clasence.shu.bakingapp.R;
import com.clasence.shu.bakingapp.RecipeDetailActivity;
import com.clasence.shu.bakingapp.models.IngredientsHelper;
import com.clasence.shu.bakingapp.models.RecipeHelper;
import com.clasence.shu.bakingapp.models.StepsHelper;

import java.util.ArrayList;

/**
 * Created by Neba.
 * Adapter for home recyclerview
 */

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.MyViewHolder> {

    //activity and arraylist definitions
    private ArrayList<StepsHelper> stepsList;
    private Activity context;
    private CustomRecyclerOnClick customRecyclerOnClick=null;
    private int selected_position = 1;
    private boolean isInitialized = false;

    // constructor
    public  RecipeDetailsAdapter(Activity context, ArrayList<StepsHelper> list, CustomRecyclerOnClick customRecyclerOnClick){
        this.context=context;
        this.stepsList=list;
        this.customRecyclerOnClick = customRecyclerOnClick;
    }
    /**
     * Class implementation of viewholder to hangle view of recycler*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public RelativeLayout ll;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.recipe_name);
            ll = (RelativeLayout) itemView.findViewById(R.id.card_linear_layout);
        }
    }

    /**onclicklisterner for recycler view*/
    public interface CustomRecyclerOnClick{
        void onCustomClick(int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_detail_view_helper, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //position zero signifies ingredients
        if(position==0){

            holder.tv_name.setText(context.getString(R.string.recipe_ingredients));

        }else
        {
            StepsHelper stepsHelper = stepsList.get(position-1);
            holder.tv_name.setText( context.getString(R.string.step)+" "+position+": "+stepsHelper.getShortDescription());

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customRecyclerOnClick.onCustomClick(position);

                // Updating old as well as new positions
                notifyItemChanged(selected_position);
                selected_position = position;
                notifyItemChanged(selected_position);
            }
        });

        if(RecipeDetailActivity.isTabletView) {
            holder.ll.setBackgroundColor(selected_position == position ? context.getResources().getColor(R.color.colorAccent) : Color.TRANSPARENT);
        }

    }

    @Override
    public int getItemCount() {
        return stepsList.size()+1;
    }

}
