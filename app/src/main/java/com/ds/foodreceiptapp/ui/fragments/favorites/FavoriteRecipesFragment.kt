package com.ds.foodreceiptapp.ui.fragments.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ds.foodreceiptapp.R
import com.ds.foodreceiptapp.adapters.FavoriteRecipesAdapter
import com.ds.foodreceiptapp.databinding.FragmentFavoriteRecipesBinding
import com.ds.foodreceiptapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private val mainViewModel:MainViewModel by viewModels()
    private val mAdapter:FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(requireActivity(), mainViewModel) }

    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        setHasOptionsMenu(true)

        // remark karna menggunakan binding fragment
//        val view = inflater.inflate(R.layout.fragment_favorite_recipes, container, false)

        setupRecyclerView(binding.favoriteRecipesRecyclerView)

        // remark karna sudah menggunakan binding adapter
//        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner,{ favoritesEntity->
//            mAdapter.setData(favoritesEntity)
//        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.deleteAll_favorite_recipe_menu){
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackbar("All Recipes removed")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView){
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter.clearContextualActionMode()
    }

    private fun showSnackbar(message:String){
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

}