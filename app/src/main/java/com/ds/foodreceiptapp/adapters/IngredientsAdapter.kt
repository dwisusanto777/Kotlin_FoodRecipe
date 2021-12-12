package com.ds.foodreceiptapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ds.foodreceiptapp.R
import com.ds.foodreceiptapp.databinding.IngredientsRowLayoutBinding
import com.ds.foodreceiptapp.modeljson.ExtendedIngredient
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.BASE_IMAGE_URL
import com.ds.foodreceiptapp.util.RecipesDiffUtil

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientList = emptyList<ExtendedIngredient>()

    class MyViewHolder(val binding:IngredientsRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientsAdapter.MyViewHolder {
        return MyViewHolder(IngredientsRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: IngredientsAdapter.MyViewHolder, position: Int) {
        val e:ExtendedIngredient = ingredientList[position]
        holder.binding.ingredientImageView.load(BASE_IMAGE_URL+e.image){
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.binding.ingredientNameTextView.text = e.name.capitalize()
        holder.binding.ingredientAmountTextView.text = e.amount.toString()
        holder.binding.ingredientUnitTextView.text = e.unit
        holder.binding.ingredientConsistencyTextView.text = e.consistency
        holder.binding.ingredientOriginalTextView.text = e.original
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    fun setData(newIngredients: List<ExtendedIngredient>){
        val ingredientsDiffUtil = RecipesDiffUtil(ingredientList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}