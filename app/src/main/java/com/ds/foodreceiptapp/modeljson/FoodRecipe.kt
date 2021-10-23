package com.ds.foodreceiptapp.modeljson


import com.google.gson.annotations.SerializedName

data class FoodRecipe(
    @SerializedName("results")
    var results:List<Result>
)