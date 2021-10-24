package com.ds.foodreceiptapp.ui.fragments.ingredient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ds.foodreceiptapp.R
import com.ds.foodreceiptapp.adapters.IngredientsAdapter
import com.ds.foodreceiptapp.modeljson.Result
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_ingredient.view.*

class IngredientFragment : Fragment() {

    private val mAdapter:IngredientsAdapter by lazy {
        IngredientsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ingredient, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        setupRecyclerView(view)
        myBundle?.extendedIngredients?.let {
            mAdapter.setData(it)
        }

        return view
    }

    private fun setupRecyclerView(view: View){
        view.ingredients_recyclerView.adapter = mAdapter
        view.ingredients_recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}