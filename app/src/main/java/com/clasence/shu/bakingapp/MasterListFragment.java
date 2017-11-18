package com.clasence.shu.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.clasence.shu.bakingapp.adapters.RecipeAdapter;
import com.clasence.shu.bakingapp.adapters.RecipeDetailsAdapter;
import com.clasence.shu.bakingapp.models.RecipeHelper;
import com.clasence.shu.bakingapp.models.StepsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Neba.
 * Code taken from course material
 */

public class MasterListFragment extends Fragment implements RecipeDetailsAdapter.CustomRecyclerOnClick{

    //define recycler
    private RecyclerView recyclerView;

    //recipe list and adapter definition
    public static ArrayList<StepsHelper> stepsList;

    private RecipeDetailsAdapter recipeDetailsAdapter;

    public static final String STEP_LIST = "steps_list";
    public static final String INGREDIENTS_STRING = "ingredients";
    private static final String LAY_MAN_STATE = "layout_state";
    public static final String LIST_POSITION = "list_post";
    private boolean state_was_saved=false;
   private  RecipeHelper recipeHelper=null;
    private CustomRecyclerOnClick mCallback;

    //recycler manager
    RecyclerView.LayoutManager mLayoutManager;

    // Mandatory empty constructor
    public MasterListFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recipe_detail_recycler_view, container, false);

        //if in tablet mode, set grid to 3
        recyclerView = (RecyclerView) rootView.findViewById(R.id.detail_view_recycler);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 5, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(savedInstanceState!=null && savedInstanceState.containsKey(STEP_LIST)){
            stepsList = savedInstanceState.getParcelableArrayList(STEP_LIST);
            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAY_MAN_STATE));
            recipeDetailsAdapter = new RecipeDetailsAdapter(getActivity(),stepsList,MasterListFragment.this);
            recyclerView.setAdapter(recipeDetailsAdapter);
            state_was_saved=true;
        }else{
            stepsList = new ArrayList<>();
            recipeDetailsAdapter = new RecipeDetailsAdapter(getActivity(),stepsList,MasterListFragment.this);
            recyclerView.setAdapter(recipeDetailsAdapter);
        }



        // Return the root view
        return rootView;
    }

    /**onclicklisterner for recycler view*/
    public interface CustomRecyclerOnClick{
        void onCustomClick(int position);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (CustomRecyclerOnClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CustomRecyclerOnClick");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(stepsList.size()>0) {
            outState.putParcelableArrayList(STEP_LIST, stepsList);
            outState.putParcelable(LAY_MAN_STATE, mLayoutManager.onSaveInstanceState());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if(!state_was_saved) {
            //get data from Detail Activity
                RecipeDetailActivity recipeDetailActivity = (RecipeDetailActivity) getActivity();
                recipeHelper = recipeDetailActivity.getRecipe();


                try {
                    JSONArray jsonArray = new JSONArray(recipeHelper.getSteps());
                    if (jsonArray.length() > 0) {
                        //clear the steplist
                        stepsList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String shortDescription = jsonObject.getString("shortDescription");
                            String description = jsonObject.getString("description");
                            String videoURL = jsonObject.getString("videoURL");
                            String thumbnailURL = jsonObject.getString("thumbnailURL");

                            StepsHelper stepsHelper = new StepsHelper(id, shortDescription, description, videoURL, thumbnailURL);
                            stepsList.add(stepsHelper);

                        }
                    }

                    //notify data set change
                    recipeDetailsAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }

    }


    @Override
    public void onCustomClick(int position) {
        mCallback.onCustomClick(position);
    }
}
