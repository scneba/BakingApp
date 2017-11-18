package com.clasence.shu.bakingapp;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.clasence.shu.bakingapp.models.RecipeColumns;
import com.clasence.shu.bakingapp.models.RecipeProvider;

/**
 * Created by Neba.
 */

public class AppWidgetIntentService extends IntentService {

    //intent action
    public static String MY_INTENT_ACTION    = "com.clasence.shu.mywidget_intent_service";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public AppWidgetIntentService() {
        super("AppWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent.getAction().equalsIgnoreCase(MY_INTENT_ACTION)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                FavouriteWidget.class));

            // Get current width to decide on single plant vs garden grid view


            for (int appWidgetId : appWidgetIds) {

              Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
              int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
              RemoteViews rv;
              if (width < 200) {
                 Cursor data = getContentResolver().query(RecipeProvider.Recipes.CONTENT_URI, null, null, null, null);
                 if (data == null) {
                   return;
                 }
                 if (!data.moveToFirst()) {
                   data.close();
                  return;
                 }
                 rv = FavouriteWidget.getRecipeSingleRemoteView(getApplicationContext(), data);
              } else {
                rv = FavouriteWidget.getRecipeGridRemoteView(getApplicationContext());
              }

                // Tell the AppWidgetManager to perform an update on the current app widget
                appWidgetManager.updateAppWidget(appWidgetId, rv);
            }
        }

    }
}
