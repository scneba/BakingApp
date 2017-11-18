package com.clasence.shu.bakingapp.models;

/**
 * Created by Neba.
 * Schematics provider for Recipes
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.NotifyBulkInsert;
import net.simonvt.schematic.annotation.NotifyDelete;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = RecipeProvider.AUTHORITY,
        database = RecipeDatabase.class)
public final class RecipeProvider {
    public static final String AUTHORITY = "com.clasence.shu.bakingapp.models.myrecipeprovider";

    //base Uri
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String RECIPES = "recipes";
        String PATH_WITH_RECIPE_ID = "recipes_recipe_id";
        String BY_RECIPE_ID = "by_recipe_id";
        String FROM_LIST = "from_list";

    }

    private  RecipeProvider(){

    }

    //Uri builder for provider

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = RecipeDatabase.RECIPES) public static class Recipes {

        //query all
        @ContentUri(
                path = Path.RECIPES,
                type = "vnd.android.cursor.dir/recipes",
                defaultSort = RecipeColumns._ID + " ASC")

        public static final Uri CONTENT_URI = buildUri(Path.RECIPES);


        //query by
        @InexactContentUri(
                path = Path.PATH_WITH_RECIPE_ID+ "/#",
                name = "RECIPE_ID",
                type = "vnd.android.cursor.item/recipes",
                whereColumn = RecipeColumns.RECIPE_ID,
                pathSegment = 1)
        public static Uri uriWithRecipeId(long id) {
            return buildUri(Path.PATH_WITH_RECIPE_ID, String.valueOf(id));
        }

        @InexactContentUri(
                path = Path.RECIPES + "/#",
                name = "BY_ID",
                type = "vnd.android.cursor.item/recipes",
                whereColumn = RecipeColumns._ID,
                pathSegment = 1)
        public static Uri uriWithId(long id) {
            return buildUri(Path.RECIPES, String.valueOf(id));
        }


        @NotifyBulkInsert(paths = Path.RECIPES)
        public static Uri[] onBulkInsert(Context context, Uri uri, ContentValues[] values, long[] ids) {
            return new Uri[] {
                    uri,
            };
        }
    }
    }