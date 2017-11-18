package com.clasence.shu.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.clasence.shu.bakingapp.adapters.IngredientsAdapter;
import com.clasence.shu.bakingapp.adapters.RecipeAdapter;
import com.clasence.shu.bakingapp.models.IngredientsHelper;
import com.clasence.shu.bakingapp.models.StepsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Neba.
 */

public class IngredientsActivity extends AppCompatActivity {

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.recipe_ingredients));

        //verify if view if in tablet mode
        if(findViewById(R.id.ingredientsTabletRecycler)!=null){
            isTablet=true;
        }

        if(isTablet){

            //if in tablet mode, set grid to 3
            recyclerView = (RecyclerView) findViewById(R.id.ingredientsTabletRecycler);
            mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 5, true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
        }
        else {
            recyclerView = (RecyclerView) findViewById(R.id.ingredientsRecycler);
            mLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 5, true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
        }


        if(savedInstanceState!=null && savedInstanceState.containsKey(INGRE_LIST)){

            ingreList =  savedInstanceState.getParcelableArrayList(INGRE_LIST);
            ingredientsAdapter = new IngredientsAdapter(this,ingreList);
            recyclerView.setAdapter(ingredientsAdapter);
            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MAN_STATE));

        }else {
            ingreList = new ArrayList<>();
            String ingredients = getIntent().getExtras().getString(MasterListFragment.INGREDIENTS_STRING);
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

                        ingredientsAdapter = new IngredientsAdapter(this, ingreList);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
