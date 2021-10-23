package com.ds.foodreceiptapp.modeljson


import com.google.gson.annotations.SerializedName

data class ResponseRecipe(
    @SerializedName("number")
    val number: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("totalResults")
    val totalResults: Int
)