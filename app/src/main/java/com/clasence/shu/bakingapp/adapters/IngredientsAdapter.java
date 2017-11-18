package com.clasence.shu.bakingapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clasence.shu.bakingapp.R;
import com.clasence.shu.bakingapp.models.IngredientsHelper;
import com.clasence.shu.bakingapp.models.RecipeHelper;

import java.util.ArrayList;

/**
 * Created by Neba.
 * Adapter for home recyclerview
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {

    //activity and arraylist definitions
    private ArrayList<IngredientsHelper> ingreList;
    private Activity context;

    // constructor
    public  IngredientsAdapter(Activity context, ArrayList<IngredientsHelper> list){
        this.context=context;
        this.ingreList=list;
    }
    /**
     * Class implementation of viewholder to hangle view of recycler*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_ingredient;
        public TextView tv_measure;
        public TextView tv_quantity;

        public MyViewHolder(View view) {
            super(view);
            tv_ingredient = (TextView) view.findViewById(R.id.tvingredient);
            tv_measure = (TextView) view.findViewById(R.id.tvmeasure);
            tv_quantity = (TextView) view.findViewById(R.id.tvquantity);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_detail_helper, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final IngredientsHelper ingredientsHelper = ingreList.get(position);
        holder.tv_ingredient.setText(ingredientsHelper.getIngredient());
        holder.tv_measure.setText(context.getString(R.string.measure)+ingredientsHelper.getMeasure());
        holder.tv_quantity.setText(context.getString(R.string.quantity)+ingredientsHelper.getQuantity());

    }

    @Override
    public int getItemCount() {
        return ingreList.size();
    }

}
