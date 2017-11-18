package com.clasence.shu.bakingapp;

/**
 * Created by Neba.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.clasence.shu.bakingapp.adapters.IngredientsAdapter;
import com.clasence.shu.bakingapp.models.IngredientsHelper;
import com.clasence.shu.bakingapp.models.RecipeColumns;
import com.clasence.shu.bakingapp.models.RecipeHelper;
import com.clasence.shu.bakingapp.models.RecipeProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        // Get all favourite recipes
        if (mCursor != null) mCursor.close();
        mCursor  = mContext.getContentResolver().query(RecipeProvider.Recipes.CONTENT_URI, null, null, null, null);

    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);
        int column_id = mCursor.getInt(mCursor.getColumnIndex(RecipeColumns.RECIPE_ID));
        int recipe_id = mCursor.getInt(mCursor.getColumnIndex(RecipeColumns.RECIPE_ID));
        String name = mCursor.getString(mCursor.getColumnIndex(RecipeColumns.NAME));
        String ingredients = mCursor.getString(mCursor.getColumnIndex(RecipeColumns.INGREDIENTS));
        String steps = mCursor.getString(mCursor.getColumnIndex(RecipeColumns.STEPS));
        String image = mCursor.getString(mCursor.getColumnIndex(RecipeColumns.IMAGE));
        RecipeHelper recipeHelper = new RecipeHelper(column_id, Integer.toString(recipe_id), name, ingredients, steps,image);


        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_helper_view);
        views.setTextViewText(R.id.recipe_name, name);

        //extract ingredients and display on view

        //empty ingredients string
        String ingredientsString="";
        try {
            JSONArray jsonArray = new JSONArray(ingredients);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = "" + i;
                    String nIngredient = jsonObject.getString("ingredient");

                    ingredientsString+=nIngredient+"\n";

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        views.setTextViewText(R.id.recipe_ingredients, ingredientsString);

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        Bundle extras = new Bundle();
        extras.putParcelable(MainActivity.SELECTED_RECIPE, recipeHelper);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_LinearLL, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

