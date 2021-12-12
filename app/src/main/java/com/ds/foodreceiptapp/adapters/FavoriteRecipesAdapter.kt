package com.ds.foodreceiptapp.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ds.foodreceiptapp.R
import com.ds.foodreceiptapp.data.database.entity.FavoritesEntity
import com.ds.foodreceiptapp.databinding.FavoriteRecipeRowLayoutBinding
import com.ds.foodreceiptapp.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.ds.foodreceiptapp.util.RecipesDiffUtil
import com.ds.foodreceiptapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteRecipesAdapter(
    private val requireActivity:FragmentActivity,
    private val mainViewModel: MainViewModel
): RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var multiSelection = false

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var favoritesRecipes = emptyList<FavoritesEntity>()

    class MyViewHolder(val binding: FavoriteRecipeRowLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoritesEntity: FavoritesEntity){
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    private fun changeRecipeStyle(holder:MyViewHolder, background:Int, strokeColor:Int){
        holder.binding.favoriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, background)
        )
        holder.binding.favoriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        rootView = holder.itemView.rootView
        myViewHolders.add(holder)
        val selectedRecipe = favoritesRecipes[position]
        holder.bind(selectedRecipe)

        saveItemStateOnScroll(holder, selectedRecipe)

        // ini menggunakan single clicklistener
        holder.binding.favoriteRecipesRowLayout.setOnClickListener {
            if(multiSelection){
                applySelection(holder, selectedRecipe)
            }else{
                val action = FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                    selectedRecipe.result
                )
                holder.itemView.findNavController().navigate(action)
            }
        }
        // ini menggunakan long clicklistener
        holder.binding.favoriteRecipesRowLayout.setOnLongClickListener {
            if(!multiSelection){
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, selectedRecipe)
                true
            }else{
                applySelection(holder, selectedRecipe)
                true
            }
        }
    }

    private fun saveItemStateOnScroll(holder:MyViewHolder, currentRecipe:FavoritesEntity){
        if(selectedRecipes.contains(currentRecipe)){
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.pink_500)
        }else{
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
    }

    private fun applySelection(holder:MyViewHolder, currentRecipe:FavoritesEntity){
        if(selectedRecipes.contains(currentRecipe)){
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTittle()
        }else{
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.pink_500)
            applyActionModeTittle()
        }
    }

    private fun applyActionModeTittle(){
        when(selectedRecipes.size){
            0-> {
                mActionMode.finish()
                multiSelection = false
            }
            1->{
                mActionMode.title = "${selectedRecipes.size} Item Selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipes.size} Items Selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favoritesRecipes.size
    }

    fun setData(newFavoriteRecipes:List<FavoritesEntity>){
        val favoriteRecipeDiffUtil = RecipesDiffUtil(favoritesRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipeDiffUtil)
        favoritesRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        mActionMode = mode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_favorite_recipe_menu){
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipes(it)
            }
            showSnackbar("${selectedRecipes.size} Recipe/s removed")
            multiSelection = false
            selectedRecipes.clear()
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach {
            changeRecipeStyle(it, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color:Int){
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }

    private fun showSnackbar(message:String){
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }

}
