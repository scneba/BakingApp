package com.clasence.shu.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Neba.
 */

public class RecipeHelper implements Parcelable{
    private String id, name,ingredients, steps,image;
    private int column_id;
    public RecipeHelper(int column_id,String id,String name,String ingredients, String steps,String image
    ){
        this.id=id;
        this.name=name;
        this.ingredients=ingredients;
        this.steps=steps;
        this.column_id=column_id;
        this.image=image;
    }

    protected RecipeHelper(Parcel in) {
        id = in.readString();
        name = in.readString();
        ingredients = in.readString();
        steps = in.readString();
        image = in.readString();
    }

    public static final Creator<RecipeHelper> CREATOR = new Creator<RecipeHelper>() {
        @Override
        public RecipeHelper createFromParcel(Parcel in) {
            return new RecipeHelper(in);
        }

        @Override
        public RecipeHelper[] newArray(int size) {
            return new RecipeHelper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(ingredients);
        dest.writeString(steps);
        dest.writeString(image);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Creator<RecipeHelper> getCREATOR() {
        return CREATOR;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public int getColumn_id() {
        return column_id;
    }

    public void setColumn_id(int column_id) {
        this.column_id = column_id;
    }

    public String getImage() {
        return image;
    }
}
