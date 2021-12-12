package com.ds.foodreceiptapp.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.ds.foodreceiptapp.R
import com.ds.foodreceiptapp.bindingadapters.RecipesRowBinding
import com.ds.foodreceiptapp.databinding.FragmentInstructionsBinding
import com.ds.foodreceiptapp.databinding.FragmentOverviewBinding
import com.ds.foodreceiptapp.modeljson.Result
import com.ds.foodreceiptapp.util.ParameterSetting.Companion.RECIPE_RESULT_KEY
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding  = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(RECIPE_RESULT_KEY) as Result

        binding.mainImageView.load(myBundle.image)
        binding.titleTextView.text = myBundle.title
        binding.likesTextView.text = myBundle.aggregateLikes.toString()
        binding.timeTextView.text = myBundle.readyInMinutes.toString()
//        myBundle?.summary.let {
//            val summary = Jsoup.parse(it).text()
//            binding.summarryTextView.text = summary
//        }
        RecipesRowBinding.parseHtml(binding.summarryTextView, myBundle.summary)
        binding.summarryTextView.text = myBundle?.summary

        updateColors(myBundle.vegetarian, binding.vegetarianTextView, binding.vegetarianImageView)
        updateColors(myBundle.vegan, binding.veganTextView, binding.veganImageView)
        updateColors(myBundle.glutenFree, binding.glutenTextView, binding.glutenImageView)
        updateColors(myBundle.dairyFree, binding.diaryTextView, binding.diaryImageView)
        updateColors(myBundle.veryHealthy, binding.healtyTextView, binding.healtyImageView)
        updateColors(myBundle.cheap, binding.cheapTextView, binding.cheapImageView)

        return binding.root
    }

    private fun updateColors(stateIsOn: Boolean, tv: TextView, iv:ImageView){
        if (stateIsOn){
            iv.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
}