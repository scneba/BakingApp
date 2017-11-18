package com.clasence.shu.bakingapp.models;

/**
 * Created by Neba.
 * Schematic database for Recipes
 */

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

@Database(version = RecipeDatabase.VERSION)
public class RecipeDatabase {

    public static final int VERSION = 1;

    public static final String[] _MIGRATIONS = {
            // Put DDL/DML commands here, one string per VERSION increment
    };

    @Table(RecipeColumns.class) public static final String RECIPES = "recipes";

    @OnUpgrade
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            String migration = _MIGRATIONS[i - 2];
            db.beginTransaction();
            try {
                db.execSQL(migration);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("error", "error executing database migration"+ migration);
            } finally {
                db.endTransaction();
            }
        }
    }
}
