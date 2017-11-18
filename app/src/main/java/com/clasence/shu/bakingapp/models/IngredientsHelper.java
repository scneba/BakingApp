package com.clasence.shu.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Neba.
 */

public class IngredientsHelper implements Parcelable{
    private String id, quantity,measure,ingredient;

    public IngredientsHelper(String id, String quantity, String measure,String ingredient){
        this.id=id;
        this.quantity=quantity;
        this.measure=measure;
        this.ingredient=ingredient;
    }

    protected IngredientsHelper(Parcel in) {
        id = in.readString();
        quantity = in.readString();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IngredientsHelper> CREATOR = new Creator<IngredientsHelper>() {
        @Override
        public IngredientsHelper createFromParcel(Parcel in) {
            return new IngredientsHelper(in);
        }

        @Override
        public IngredientsHelper[] newArray(int size) {
            return new IngredientsHelper[size];
        }
    };


    public String getId() {
        return id;
    }

    public static Creator<IngredientsHelper> getCREATOR() {
        return CREATOR;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public String getQuantity() {
        return quantity;
    }
}
