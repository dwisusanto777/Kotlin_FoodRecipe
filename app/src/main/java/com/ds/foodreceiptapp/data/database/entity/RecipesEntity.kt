package com.ds.foodreceiptapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ds.foodreceiptapp.modeljson.FoodRecipe
import com.ds.foodreceiptapp.util.ParameterSetting

@Entity(tableName = ParameterSetting.RECIPES_TABLE)
class RecipesEntity (
    var foodRecipe: FoodRecipe
    ){
    @PrimaryKey(autoGenerate = false)
    var id:Int = 0
}