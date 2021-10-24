package com.ds.foodreceiptapp.ui.fragments.instruction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.ds.foodreceiptapp.R
import com.ds.foodreceiptapp.modeljson.Result
import com.ds.foodreceiptapp.util.ParameterSetting
import kotlinx.android.synthetic.main.fragment_instructions.view.*

class InstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_instructions, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(ParameterSetting.RECIPE_RESULT_KEY)

        view.instruction_webView.webViewClient = object : WebViewClient(){

        }
        val websiteUrl:String = myBundle!!.sourceUrl
        view.instruction_webView.loadUrl(websiteUrl)

        return  view
    }

}