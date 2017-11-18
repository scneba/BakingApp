package com.clasence.shu.bakingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.clasence.shu.bakingapp.models.RecipeColumns;
import com.clasence.shu.bakingapp.models.RecipeHelper;
import com.clasence.shu.bakingapp.models.RecipeProvider;
import com.clasence.shu.bakingapp.models.StepsHelper;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;

/**
 * Created by Neba.
 */

public class RecipeDetailActivity extends AppCompatActivity implements MasterListFragment.CustomRecyclerOnClick,LoaderManager.LoaderCallbacks<Cursor> {

    //list of recipes from previous activity;
   RecipeHelper recipeHelper;
    public static boolean isTabletView=false;

    private static int step_position=0;

    private static boolean inDatabase = false;
    //menu
    private Menu menu=null;

    //loader id
    private static final int LOADER_ID = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recipeHelper = getIntent().getExtras().getParcelable(MainActivity.SELECTED_RECIPE);
        getSupportActionBar().setTitle(recipeHelper.getName());


        if(findViewById(R.id.player_container)!=null){
            isTabletView=true;
        }

         //loader to check if recipe is already in favourites
        if(getSupportLoaderManager().getLoader(LOADER_ID)==null){
            getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        }else{
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this).forceLoad();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isTabletView) {
            StepsHelper stepsHelper = MasterListFragment.stepsList.get(step_position);

            MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
            Bundle bundle = new Bundle();
            bundle.putString(MediaPlayerFragment.VIDEO_URL, stepsHelper.getVideoUrl());
            bundle.putString(MediaPlayerFragment.STEP_DETAILS, stepsHelper.getDescription());
            bundle.putString(MediaPlayerFragment.THUMBNAIL, stepsHelper.getThumbnailUrl());
            mediaPlayerFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.player_container, mediaPlayerFragment)
                    .commit();

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu=menu;
        getMenuInflater().inflate(R.menu.fav_menu,menu);

        //menu can be created when loader call back has already been received. Update ui accordingly
        if(inDatabase){
            this.menu.getItem(0).setIcon(getResources().getDrawable(android.R.drawable.btn_star_big_on));

        }else{
            this.menu.getItem(0).setIcon(getResources().getDrawable(android.R.drawable.btn_star_big_off));

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case  R.id.fav_menu_button:{

                if(!inDatabase) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(RecipeColumns.RECIPE_ID, Integer.parseInt(recipeHelper.getId()));
                    contentValues.put(RecipeColumns.NAME, recipeHelper.getName());
                    contentValues.put(RecipeColumns.INGREDIENTS, recipeHelper.getIngredients());
                    contentValues.put(RecipeColumns.STEPS, recipeHelper.getSteps());
                    contentValues.put(RecipeColumns.IMAGE, recipeHelper.getImage());
                    Uri uri = getContentResolver().insert(RecipeProvider.Recipes.CONTENT_URI, contentValues);
                    if (uri != null) {
                        this.menu.getItem(0).setIcon(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                        inDatabase = true;

                        Snackbar.make(findViewById(android.R.id.content), getString(R.string.addedtofav), Snackbar.LENGTH_LONG).show();


                    }
                }else{
                    Uri uri =  RecipeProvider.Recipes.uriWithId(recipeHelper.getColumn_id());
                    int number = getContentResolver().delete(uri,null,null);
                    if(number>0){
                        this.menu.getItem(0).setIcon(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                        inDatabase=false;
                       Snackbar.make(findViewById(android.R.id.content), getString(R.string.removedfromfav), Snackbar.LENGTH_LONG).show();


                    }
                }

                //update widgets accordingly
                Intent intent = new Intent(getApplicationContext(),AppWidgetIntentService.class);
                intent.setAction(AppWidgetIntentService.MY_INTENT_ACTION);
                startService(intent);

            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    //gets arraylist of recipes pass from previous activity
    RecipeHelper getRecipe(){
        return recipeHelper;
    }

    @Override
    public void onCustomClick(int position) {

        if(isTabletView){

             //position zero is ingredients
            if (position == 0) {
                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.player_container, ingredientsFragment)
                        .commit();

            }else {

                //create and replace media player fragment
                 step_position = position-1;
                StepsHelper stepsHelper = MasterListFragment.stepsList.get(step_position);
                MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
                Bundle bundle = new Bundle();
                bundle.putString(MediaPlayerFragment.VIDEO_URL, stepsHelper.getVideoUrl());
                bundle.putString(MediaPlayerFragment.STEP_DETAILS, stepsHelper.getDescription());
                mediaPlayerFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.player_container, mediaPlayerFragment)
                        .commit();

            }

        }else {
            if (position == 0) {

                final Intent intent = new Intent(this, IngredientsActivity.class);
                intent.putExtra(MasterListFragment.INGREDIENTS_STRING, recipeHelper.getIngredients());
                startActivity(intent);
            } else {
                final Intent intent = new Intent(this, StepsDetailActivity.class);
                intent.putParcelableArrayListExtra(MasterListFragment.STEP_LIST, MasterListFragment.stepsList);
                intent.putExtra(MasterListFragment.LIST_POSITION, position - 1);
                startActivity(intent);

            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = RecipeProvider.Recipes.uriWithRecipeId(Long.parseLong(recipeHelper.getId()));
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
            if(menu!=null)
                this.menu.getItem(0).setIcon(getResources().getDrawable(android.R.drawable.btn_star_big_on));
            recipeHelper.setColumn_id(data.getInt(data.getColumnIndex(RecipeColumns._ID)));
            inDatabase=true;
        }else{
            if(menu!=null)
                this.menu.getItem(0).setIcon(getResources().getDrawable(android.R.drawable.btn_star_big_off));
            inDatabase=false;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
