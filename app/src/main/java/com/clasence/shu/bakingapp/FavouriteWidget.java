package com.clasence.shu.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.clasence.shu.bakingapp.models.RecipeColumns;

/**
 * Implementation of App Widget functionality.
 */
public class FavouriteWidget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent my_intent = new Intent(context,AppWidgetIntentService.class);
        my_intent.setAction(AppWidgetIntentService.MY_INTENT_ACTION);
        context.startService(my_intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Intent my_intent = new Intent(context,AppWidgetIntentService.class);
        my_intent.setAction(AppWidgetIntentService.MY_INTENT_ACTION);
        context.startService(my_intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (AppWidgetIntentService.MY_INTENT_ACTION.equals(intent.getAction())) {

            Intent my_intent = new Intent(context,AppWidgetIntentService.class);
            my_intent.setAction(AppWidgetIntentService.MY_INTENT_ACTION);
            context.startService(my_intent);          }
    }


    /**
     * Creates and returns the RemoteViews to be displayed in the GridView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the GridView mode widget
     */
    public static RemoteViews getRecipeGridRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favourite_grid_widget);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);
        // Set the RecipeDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        // Handle empty favourites
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        return views;
    }


    /**
     * Creates and returns the RemoteViews to be displayed in the textview mode
     *
     * @param context The context
     * @return The RemoteViews for the GridView mode widget
     */
    public static RemoteViews getRecipeSingleRemoteView(Context context, Cursor data) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favourite_widget);
        String allfavs = context.getString(R.string.favourites)+"\n\n";
        for (int i = 0; i < data.getCount(); i++) {
            allfavs = allfavs + " " + data.getString(data.getColumnIndex(RecipeColumns.NAME))+"\n";
            data.moveToNext();
        }

        views.setTextViewText(R.id.appwidget_text, allfavs);
        // Create an Intent to launch MatchesActivity
        Intent launchIntent = new Intent(context, FavouriteActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
        views.setOnClickPendingIntent(R.id.app_widget_rl, pendingIntent);
        return  views;
    }
}

