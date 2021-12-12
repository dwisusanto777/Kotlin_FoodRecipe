package com.ds.foodreceiptapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.ds.foodreceiptapp.R
import com.ds.foodreceiptapp.adapters.PagerAdapter
import com.ds.foodreceiptapp.data.database.entity.FavoritesEntity
import com.ds.foodreceiptapp.databinding.ActivityDetailsBinding
import com.ds.foodreceiptapp.ui.fragments.ingredient.IngredientFragment
import com.ds.foodreceiptapp.ui.fragments.instruction.InstructionsFragment
import com.ds.foodreceiptapp.ui.fragments.overview.OverviewFragment
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.RECIPE_RESULT_KEY
import com.ds.foodreceiptapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0

    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle();
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        // remark karna perbaikkan dari student
//        val adapter = PagerAdapter(
//            resultBundle,
//            fragments,
//            titles,
//            supportFragmentManager
//        )
//        binding.viewPager.adapter = adapter
//        binding.tabLayout.setupWithViewPager(binding.viewPager)
        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )
        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.apply {
            adapter = pagerAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager2){tab, position ->
            tab.text = titles[position]
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_favorites_menu)
        checkSavedRecipes(menuItem)
        return true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, {favoritesEntity ->
            try{
                for(saveRecipe in favoritesEntity){
                    if(saveRecipe.result.id == args.result.id){
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = saveRecipe.id
                        recipeSaved = true
                    }// remark perbaikkan code dari commentar
//                    else{
//                        changeMenuItemColor(menuItem, R.color.white)
//                    }
                }
            }catch (e:Exception){
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            finish()
        }else if(item.itemId==R.id.save_to_favorites_menu && !recipeSaved){
            saveToFavorites(item)
        }else if(item.itemId==R.id.save_to_favorites_menu && recipeSaved){
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoriteEntity = FavoritesEntity(
            0,
            args.result
        )
        mainViewModel.insertFavoriteRecipes(favoriteEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe Saved.")
        recipeSaved = true
    }

    private fun removeFromFavorites(menuItem: MenuItem){
        val favoritesEntity = FavoritesEntity(
            savedRecipeId,
            args.result
        )
        mainViewModel.deleteFavoriteRecipes(favoritesEntity)
        changeMenuItemColor(menuItem, R.color.white)
        showSnackBar("Removed from favorites")
        recipeSaved = false
    }

    private fun showSnackBar(s: String) {
        Snackbar.make(
            binding.detailsLayout,
            s,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, yellow: Int) {
        item.icon.setTint(ContextCompat.getColor(this, yellow))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.white)
    }

}