package com.ds.foodreceiptapp.data.database

import androidx.room.TypeConverter
import com.ds.foodreceiptapp.modeljson.FoodRecipe
import com.ds.foodreceiptapp.modeljson.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe):String{
        return gson.toJson((foodRecipe))
    }

    @TypeConverter
    fun stringToFoodRecipe(data:String):FoodRecipe{
        var listType = object : TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun resultToString(result: Result):String{
        return gson.toJson((result))
    }

    @TypeConverter
    fun stringToResult(data:String):Result{
        var listType = object : TypeToken<Result>(){}.type
        return gson.fromJson(data, listType)
    }
}