package com.ds.foodreceiptapp.data

import android.R
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.DEFAULT_DIET_TYPE
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.DEFAULT_MEAL_TYPE
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.PREFERENCES_BACK_ONLINE
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.PREFERENCES_DIET_TYPE
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.PREFERENCES_DIET_TYPE_ID
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.PREFERENCES_MEAL_TYPE
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.PREFERENCES_MEAL_TYPE_ID
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys{
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE);
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID);
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE);
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID);
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveMealAndDietType(mealType:String, mealTypeId:Int, dietType:String, dietTypeId:Int) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean){
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline] = backOnline
        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { e->
            if(e is IOException){
                emit(emptyPreferences())
            }else{
                throw  e
            }
        }
        .map { pr->
            val selectedMealType  = pr[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId  = pr[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType  = pr[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId  = pr[PreferenceKeys.selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { e->
            if(e is IOException){
                emit(emptyPreferences())
            }else{
                throw  e
            }
        }
        .map {preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?: false
            backOnline
        }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)