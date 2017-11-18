package com.clasence.shu.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clasence.shu.bakingapp.adapters.IngredientsAdapter;
import com.clasence.shu.bakingapp.models.IngredientsHelper;
import com.clasence.shu.bakingapp.models.RecipeHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Neba.
 */

public class IngredientsFragment extends Fragment {

    //define recycler
    private RecyclerView recyclerView;

    //recipe list and adapter definition
    private ArrayList<IngredientsHelper> ingreList;
    private IngredientsAdapter ingredientsAdapter;

    //recycler manager
    RecyclerView.LayoutManager mLayoutManager;

    private static final String INGRE_LIST = "ingrelist";
    private static final String LAYOUT_MAN_STATE = "lay_man_state";
    private static boolean isTablet=false;

    private boolean isSavedInstance=true;

    private RecipeHelper recipeHelper;

    public  IngredientsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredients_fragment_view, container, false);




        //verify if view if in tablet mode
        if(rootView.findViewById(R.id.ingredientsTabletRecycler)!=null){
            isTablet=true;
        }

        if(isTablet){

            //if in tablet mode, set grid to 3
            recyclerView = (RecyclerView) rootView.findViewById(R.id.ingredientsTabletRecycler);
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 5, true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
        }
        else {
            recyclerView = (RecyclerView) rootView.findViewById(R.id.ingredientsRecycler);
            mLayoutManager = new GridLayoutManager(getActivity(), 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 5, true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
        }


        if(savedInstanceState!=null && savedInstanceState.containsKey(INGRE_LIST)){

            ingreList =  savedInstanceState.getParcelableArrayList(INGRE_LIST);
            ingredientsAdapter = new IngredientsAdapter(getActivity(),ingreList);
            recyclerView.setAdapter(ingredientsAdapter);
            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MAN_STATE));

        }else {
            isSavedInstance=false;
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!isSavedInstance){
            ingreList = new ArrayList<>();
            //get data from Detail Activity
            RecipeDetailActivity recipeDetailActivity = (RecipeDetailActivity) getActivity();
            recipeHelper = recipeDetailActivity.getRecipe();
            String ingredients =recipeHelper.getIngredients();
            try {
                JSONArray jsonArray = new JSONArray(ingredients);
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = "" + i;
                        String ingredient = jsonObject.getString("ingredient");
                        String measure = jsonObject.getString("measure");
                        String quantity = jsonObject.getString("quantity");

                        IngredientsHelper ingredientsHelper = new IngredientsHelper(id, quantity, measure, ingredient);
                        ingreList.add(ingredientsHelper);

                        ingredientsAdapter = new IngredientsAdapter(getActivity(), ingreList);
                        recyclerView.setAdapter(ingredientsAdapter);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(ingreList.size()>0) {
            outState.putParcelableArrayList(INGRE_LIST, ingreList);
            outState.putParcelable(LAYOUT_MAN_STATE, mLayoutManager.onSaveInstanceState());
        }

    }
}
