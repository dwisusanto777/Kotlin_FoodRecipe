package com.ds.foodreceiptapp.ui.fragments.ingredient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ds.foodreceiptapp.R
import com.ds.foodreceiptapp.adapters.IngredientsAdapter
import com.ds.foodreceiptapp.databinding.FragmentIngredientBinding
import com.ds.foodreceiptapp.modeljson.Result
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.RECIPE_RESULT_KEY

class IngredientFragment : Fragment() {

    private val mAdapter:IngredientsAdapter by lazy {
        IngredientsAdapter()
    }

    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        setupRecyclerView()
        myBundle?.extendedIngredients?.let {
            mAdapter.setData(it)
        }

        return binding.root
    }

    private fun setupRecyclerView(){
        binding.ingredientsRecyclerView.adapter = mAdapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//        mAdapter.clearContextualActionMode()
    }

}