package com.ds.foodreceiptapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.ds.foodreceiptapp.data.database.entity.RecipesEntity
import com.ds.foodreceiptapp.modeljson.FoodRecipe
import com.ds.foodreceiptapp.util.NetworkResult

class RecipesBinding {

    companion object{
//        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
//        @JvmStatic
//        fun errorImageViewVisibility(
//            imageView: ImageView,
//            apiResponse: NetworkResult<FoodRecipe>?,
//            database: List<RecipesEntity>?
//        ){
//            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
//                imageView.visibility = View.VISIBLE
//            }else if (apiResponse is NetworkResult.Loading){
//                imageView.visibility = View.VISIBLE
//            }else if (apiResponse is NetworkResult.Success){
//                imageView.visibility = View.VISIBLE
//            }
//        }
//        @BindingAdapter("readApiResponse2", "readDatabase2", requireAll = true)
//        @JvmStatic
//        fun errorTextViewVisibility(
//            textView: TextView,
//            apiResponse: NetworkResult<FoodRecipe>?,
//            database: List<RecipesEntity>?
//        ){
//            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
//                textView.visibility = View.VISIBLE
//                textView.text = apiResponse.message.toString()
//            }else if (apiResponse is NetworkResult.Loading){
//                textView.visibility = View.VISIBLE
//            }else if (apiResponse is NetworkResult.Success){
//                textView.visibility = View.VISIBLE
//            }
//        }

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun handleReadDataErrors(
            view: View,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ){
            when(view){
                is ImageView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                }
                is TextView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                    view.text = apiResponse?.message.toString()
                }
            }
        }
    }

}