package com.ds.foodreceiptapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ds.foodreceiptapp.data.DataStoreRepository
import com.ds.foodreceiptapp.data.MealAndDietType
import com.ds.foodreceiptapp.util.ParameterSetting
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.DEFAULT_DIET_TYPE
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.DEFAULT_MEAL_TYPE
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.DEFAULT_RECIPES_NUMBER
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.QUERY_SEARCH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

//    private var mealType = DEFAULT_MEAL_TYPE
//    private var dietType = DEFAULT_DIET_TYPE

    private lateinit var mealAndDiet:MealAndDietType

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealAndDietType() =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(
                mealAndDiet.selectedMealType,
                mealAndDiet.selectedMealTypeId,
                mealAndDiet.selectedDietType,
                mealAndDiet.selectedDietTypeId
            )
        }

    fun saveMealAndDietTypeTemp(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int){
        mealAndDiet = MealAndDietType(
            mealType,
            mealTypeId,
            dietType,
            dietTypeId
        )
    }


    fun saveBackOnline(backOnline:Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun applyQueries(): HashMap<String, String> {
        val quaries: HashMap<String, String> = HashMap()

//        viewModelScope.launch {
//            readMealAndDietType.collect { value ->
//                mealType = value.selectedMealType
//                dietType = value.selectedDietType
//            }
//        }

        quaries[ParameterSetting.QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        quaries[ParameterSetting.QUERY_API_KEY] = ParameterSetting.KEY
        quaries[ParameterSetting.QUERY_TYPE] = mealAndDiet.selectedMealType
        quaries[ParameterSetting.QUERY_DIET] = mealAndDiet.selectedDietType
        quaries[ParameterSetting.QUERY_ADD_RECIPE_INFORMATION] = "true"
        quaries[ParameterSetting.QUERY_FILL_INGREDIENTS] = "true"
        return quaries
    }

    fun applySearchQuery(searchQuery:String): HashMap<String,String>{
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[ParameterSetting.QUERY_API_KEY] = ParameterSetting.KEY
        queries[ParameterSetting.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[ParameterSetting.QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

    fun showNetworkStatus(){
        if(!networkStatus){
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        }else if(networkStatus){
            if(backOnline){
                Toast.makeText(getApplication(), "We're back online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

}