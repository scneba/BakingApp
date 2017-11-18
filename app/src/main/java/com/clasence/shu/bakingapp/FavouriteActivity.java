package com.clasence.shu.bakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.clasence.shu.bakingapp.adapters.RecipeAdapter;
import com.clasence.shu.bakingapp.models.RecipeColumns;
import com.clasence.shu.bakingapp.models.RecipeHelper;
import com.clasence.shu.bakingapp.models.RecipeProvider;

import java.util.ArrayList;

/**
 * Created by Neba.
 */

public class FavouriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,RecipeAdapter.CustomRecyclerOnClick {

    //define recycler
    private RecyclerView recyclerView;

    //loader id
    private static int MY_LOADER_ID=100;

    //recipe list and adapter definition
    private ArrayList<RecipeHelper> recipeList;
    private RecipeAdapter recipeAdapter;

    //recycler manager
    RecyclerView.LayoutManager mLayoutManager;

    private static final String RECIPES_LIST = "recipeList";
    public static final String SELECTED_RECIPE = "selected_recipe";
    private static final String LAYOUT_MAN_STATE = "lay_man_state";
    private static boolean isTablet=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //verify if view if in tablet mode
        if(findViewById(R.id.tabletRecycler)!=null){
            isTablet=true;
        }

        if(isTablet){

            //if in tablet mode, set grid to 3
            recyclerView = (RecyclerView) findViewById(R.id.tabletRecycler);
            mLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 5, true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        else {
            recyclerView = (RecyclerView) findViewById(R.id.homeRecycler);
            mLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 5, true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }



        //restore state if already saved
        if(savedInstanceState!=null && savedInstanceState.containsKey(RECIPES_LIST)){

            recipeList =  savedInstanceState.getParcelableArrayList(RECIPES_LIST);
            recipeAdapter = new RecipeAdapter(FavouriteActivity.this,recipeList,this);
            recyclerView.setAdapter(recipeAdapter);
            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MAN_STATE));

        }else {

            recipeList = new ArrayList<>();
            recipeAdapter = new RecipeAdapter(this,recipeList,this);
            recyclerView.setAdapter(recipeAdapter);

            if (getSupportLoaderManager().getLoader(MY_LOADER_ID) == null) {
                getSupportLoaderManager().initLoader(MY_LOADER_ID, null, FavouriteActivity.this).forceLoad();
            } else {
                getSupportLoaderManager().restartLoader(MY_LOADER_ID, null, FavouriteActivity.this).forceLoad();
            }

        }

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save parcelable movie list and the position of the current list item
        if(recipeList.size()>0) {
            outState.putParcelableArrayList(RECIPES_LIST, recipeList);
            outState.putParcelable(LAYOUT_MAN_STATE,mLayoutManager.onSaveInstanceState());
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = RecipeProvider.Recipes.CONTENT_URI;
        Log.e("generated_uri",uri.toString());
        return new CursorLoader(this,
                uri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data!=null &&data.moveToFirst()){
            //already in database
            for(int i=0;i<data.getCount();i++) {
                int column_id = data.getInt(data.getColumnIndex(RecipeColumns.RECIPE_ID));
                int recipe_id = data.getInt(data.getColumnIndex(RecipeColumns.RECIPE_ID));
                String name = data.getString(data.getColumnIndex(RecipeColumns.NAME));
                String ingredients = data.getString(data.getColumnIndex(RecipeColumns.INGREDIENTS));
                String steps = data.getString(data.getColumnIndex(RecipeColumns.STEPS));
                RecipeHelper recipeHelper = new RecipeHelper(column_id, Integer.toString(recipe_id), name, ingredients, steps,"");
                recipeList.add(recipeHelper);
                data.moveToNext();
            }
            recipeAdapter.notifyDataSetChanged();



        }else{
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.nofavavailable), Snackbar.LENGTH_LONG).show();

        }

        //destroy loader to avoid any problems
        getSupportLoaderManager().destroyLoader(MY_LOADER_ID);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onCustomClick(int position) {
        final Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(SELECTED_RECIPE,recipeList.get(position));
        startActivity(intent);
    }
}
