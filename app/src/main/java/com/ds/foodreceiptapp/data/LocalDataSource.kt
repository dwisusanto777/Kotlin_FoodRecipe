package com.ds.foodreceiptapp.data

import com.ds.foodreceiptapp.data.database.RecipesDao
import com.ds.foodreceiptapp.data.database.entity.FavoritesEntity
import com.ds.foodreceiptapp.data.database.entity.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    fun readRecipes(): Flow<List<RecipesEntity>>{
        return recipesDao.readRecipes()
    }

    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>>{
        return recipesDao.readFavoriteRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity){
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity){
        recipesDao.insertFavoriteRecipes(favoritesEntity)
    }

    suspend fun deleteFavoriteRecipes(favoritesEntity: FavoritesEntity){
        recipesDao.deleteFavoriteRecipes(favoritesEntity)
    }

    suspend fun deleteAllFavoriteRecipes(){
        recipesDao.deleteAllFavoritesRecipes()
    }
}