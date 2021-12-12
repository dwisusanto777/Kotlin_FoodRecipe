package com.ds.foodreceiptapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.ds.foodreceiptapp.data.Repository
import com.ds.foodreceiptapp.data.database.entity.FavoritesEntity
import com.ds.foodreceiptapp.data.database.entity.FoodJokeEntity
import com.ds.foodreceiptapp.data.database.entity.RecipesEntity
import com.ds.foodreceiptapp.modeljson.FoodJoke
import com.ds.foodreceiptapp.modeljson.FoodRecipe
import com.ds.foodreceiptapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application) : AndroidViewModel(application) {

    // ROOM DATABASE
    val readRecipes:LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes:LiveData<List<FavoritesEntity>> = repository.local.readFavoriteRecipes().asLiveData()
    val readFoodJokes:LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoritesEntity)
        }

    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }

    fun deleteFavoriteRecipes(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipes(favoritesEntity)
        }

    fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }


    // RETROFIT
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }
    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }
    fun getFoodJoke(apiKey:String) = viewModelScope.launch {
        getFoodJokeSaveCall(apiKey)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null){
                    offLineCacheRecipes(foodRecipe)
                }
            }catch (e : Exception){
                recipesResponse.value = NetworkResult.Error("Recipes Not Found")
            }
        }else{
            recipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }
    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)
            }catch (e : Exception){
                searchRecipesResponse.value = NetworkResult.Error("Recipes Not Found")
            }
        }else{
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }
    private suspend fun getFoodJokeSaveCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)

                val foodJoke = foodJokeResponse.value!!.data
                if(foodJoke!=null){
                    offLineCacheFoodJoke(foodJoke)
                }
            }catch (e : Exception){
                foodJokeResponse.value = NetworkResult.Error("Joke Not Found")
            }
        }else{
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun offLineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun offLineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code()==402 -> {
                return  NetworkResult.Error("API Key limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes Not Found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return  NetworkResult.Success(foodRecipes)
            }
            else -> {
                return  NetworkResult.Error(response.message())
            }
        }
    }
    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.code()==402 -> {
                NetworkResult.Error("API Key limited.")
            }
            response.isSuccessful -> {
                val foodJoke = response.body()
                NetworkResult.Success(foodJoke)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}