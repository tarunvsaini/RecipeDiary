package com.tarun.saini.recipeDiary.Model;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("recipeCategory")
    @Expose
    private String recipeCategory;
    @SerializedName("recipetype")
    @Expose
    private String recipetype;
    @SerializedName("calories")
    @Expose
    private Integer calories;
    @SerializedName("serves")
    @Expose
    private Integer serves;
    @SerializedName("estimatedtime")
    @Expose
    private String estimatedtime;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("reviews")
    @Expose
    private List<Object> reviews = null;
    @SerializedName("steps")
    @Expose
    private ArrayList<String> steps = null;
    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredient> ingredients = null;
    @SerializedName("results")
    private ArrayList<Recipe> results;


    public Recipe(String image, String recipeCategory, String recipetype, Integer calories, Integer serves, String estimatedtime, String description, String name,ArrayList<String> steps,ArrayList<Ingredient> ingredients) {
        this.image = image;
        this.recipeCategory = recipeCategory;
        this.recipetype = recipetype;
        this.calories = calories;
        this.serves = serves;
        this.estimatedtime = estimatedtime;
        this.description = description;
        this.name = name;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    protected Recipe(Parcel in) {
        this.id = in.readString();
        this.image = in.readString();
        this.recipeCategory = in.readString();
        this.recipetype = in.readString();
        this.calories=in.readInt();
        this.serves=in.readInt();
        this.estimatedtime = in.readString();
        this.description = in.readString();
        this.name = in.readString();
        this.steps = in.createStringArrayList();
        this.ingredients=in.createTypedArrayList(Ingredient.CREATOR);

    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRecipeCategory() {
        return recipeCategory;
    }

    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    public String getRecipetype() {
        return recipetype;
    }

    public void setRecipetype(String recipetype) {
        this.recipetype = recipetype;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getServes() {
        return serves;
    }

    public void setServes(Integer serves) {
        this.serves = serves;
    }

    public String getEstimatedtime() {
        return estimatedtime;
    }

    public void setEstimatedtime(String estimatedtime) {
        this.estimatedtime = estimatedtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<Object> getReviews() {
        return reviews;
    }

    public void setReviews(List<Object> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


    public ArrayList<Recipe> getResults() {
        return results;
    }

    public void setResults(ArrayList<Recipe> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(this.image);
        dest.writeString(this.recipeCategory);
        dest.writeString(this.recipetype);
        dest.writeInt(this.calories);
        dest.writeInt(this.serves);
        dest.writeString(this.estimatedtime);
        dest.writeString(this.description);
        dest.writeString(this.name);
        dest.writeStringList(this.steps);
        dest.writeTypedList(this.ingredients);
    }
}


