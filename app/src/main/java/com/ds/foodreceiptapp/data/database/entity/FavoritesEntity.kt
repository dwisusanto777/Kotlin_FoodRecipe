package com.ds.foodreceiptapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ds.foodreceiptapp.modeljson.Result
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity (
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var result: Result
        ){
}