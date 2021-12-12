package com.ds.foodreceiptapp.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ds.foodreceiptapp.modeljson.FoodJoke
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity (
    @Embedded
    var foodJoke: FoodJoke
        ){
    @PrimaryKey(autoGenerate = false)
    var id:Int = 0
}