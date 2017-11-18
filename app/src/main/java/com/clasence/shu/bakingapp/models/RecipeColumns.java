package com.clasence.shu.bakingapp.models;

/**
 * Created by Neba.
 * Class contains columns for schematic Recipe table
 */


import android.support.annotation.Nullable;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;
public interface RecipeColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(DataType.Type.INTEGER) @NotNull String RECIPE_ID = "recipe_id";
    @DataType(DataType.Type.TEXT) @NotNull String NAME = "name";
    @DataType(DataType.Type.TEXT) @NotNull String INGREDIENTS = "ingredients";
    @DataType(DataType.Type.TEXT) @NotNull String STEPS = "steps";
    @DataType(DataType.Type.TEXT) @NotNull String IMAGE = "image";
}

