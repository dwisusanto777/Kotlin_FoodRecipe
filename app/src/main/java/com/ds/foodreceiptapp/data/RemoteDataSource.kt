 package com.ds.foodreceiptapp.data

 import com.ds.foodreceiptapp.modeljson.FoodJoke
 import com.ds.foodreceiptapp.modeljson.FoodRecipe
 import com.ds.foodreceiptapp.network.FoodRecipesApi
 import retrofit2.Response
 import javax.inject.Inject

 class RemoteDataSource @Inject constructor (
     private val foodRecipesApi: FoodRecipesApi
         ) {

     suspend fun getRecipes(queries:Map<String, String>):Response<FoodRecipe>{
         return foodRecipesApi.getRecipes(queries)
     }

     suspend fun searchRecipes(searchQuery:Map<String, String>):Response<FoodRecipe>{
         return foodRecipesApi.searchRecipes(searchQuery)
     }

     suspend fun getFoodJoke(apiKey:String): Response<FoodJoke>{
         return foodRecipesApi.getFoodJoke(apiKey)
     }

}