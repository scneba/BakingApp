package com.clasence.shu.bakingapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clasence.shu.bakingapp.R;
import com.clasence.shu.bakingapp.models.RecipeHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Neba
 * Adapter for home recyclerview
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

//activity and arraylist definitions
    private ArrayList<RecipeHelper> recipeList;
    private Activity context;
    private CustomRecyclerOnClick customRecyclerOnClick=null;

    // constructor
    public  RecipeAdapter(Activity context, ArrayList<RecipeHelper> list,CustomRecyclerOnClick customRecyclerOnClick){
        this.context=context;
        this.recipeList=list;
        this.customRecyclerOnClick = customRecyclerOnClick;
    }
/**
 * Class implementation of viewholder to hangle view of recycler*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_recipe_name;
        public ImageView recipe_image;
        public RelativeLayout ll;

        public MyViewHolder(View view) {
            super(view);
            tv_recipe_name = (TextView) view.findViewById(R.id.recipe_name);
            recipe_image= view.findViewById(R.id.recipe_image);
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
                .inflate(R.layout.recipe_helper_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final RecipeHelper recipeHelper = recipeList.get(position);
        holder.tv_recipe_name.setText(recipeHelper.getName());

        //check if there is an image url


        if(recipeHelper.getImage()!=null &&recipeHelper.getImage().length()>10){
            //there is a picture, display it

            //load image or thumbnail depending on the one that is available.
            Picasso.with(context).load(recipeHelper.getImage()).fit()
                    .error(android.R.drawable.ic_menu_slideshow)
                    .placeholder(android.R.drawable.ic_menu_slideshow)
                    .into(holder.recipe_image);


        }

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(customRecyclerOnClick!=null){
                    customRecyclerOnClick.onCustomClick(position);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

}
