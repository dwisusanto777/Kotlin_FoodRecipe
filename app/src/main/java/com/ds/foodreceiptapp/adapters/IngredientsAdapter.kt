package com.ds.foodreceiptapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ds.foodreceiptapp.R
import com.ds.foodreceiptapp.modeljson.ExtendedIngredient
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.BASE_IMAGE_URL
import com.ds.foodreceiptapp.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientsAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredients_row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: IngredientsAdapter.MyViewHolder, position: Int) {
        val e:ExtendedIngredient = ingredientList[position]
        holder.itemView.ingredient_imageView.load(BASE_IMAGE_URL+e.image){
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.itemView.ingredient_name_textView.text = e.name.capitalize()
        holder.itemView.ingredient_amount_textView.text = e.amount.toString()
        holder.itemView.ingredient_unit_textView.text = e.unit
        holder.itemView.ingredient_consistency_textView.text = e.consistency
        holder.itemView.ingredient_original_textView.text = e.original
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