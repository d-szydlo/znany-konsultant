package com.example.znanykonultant.user.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.znanykonultant.R
import com.example.znanykonultant.user.UserMainPageActivity

class FilterFragment : Fragment() {

    private lateinit var cityTextField : EditText
    private lateinit var priceMinField : EditText
    private lateinit var priceMaxField : EditText
    private lateinit var catIT : CheckBox
    private lateinit var catBusiness : CheckBox
    private lateinit var catFinance : CheckBox
    private lateinit var catMarketing : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("old_filters") { _, bundle ->
            recoverFilters(bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter, container, false)
        view.findViewById<Button>(R.id.confirmButton).setOnClickListener { onConfirmButton() }

        cityTextField = view.findViewById(R.id.cityTextField)
        priceMinField = view.findViewById(R.id.priceMinText)
        priceMaxField = view.findViewById(R.id.priceMaxText)

        catIT = view.findViewById(R.id.categoryITBox)
        catBusiness = view.findViewById(R.id.categoryBusinessBox)
        catFinance = view.findViewById(R.id.categoryFinanceBox)
        catMarketing = view.findViewById(R.id.categoryMarketingBox)

        return view
    }

    private fun recoverFilters(bundle: Bundle) {
        cityTextField.setText(bundle.getString("city", ""))
        //priceMinField.setText(bundle.getDouble("priceMin", 0.0).toString().replace('.', ','))
        //priceMaxField.setText(bundle.getDouble("priceMax", 1000.0).toString().replace('.', ','))

        catIT.isChecked = bundle.getBoolean("catIT", false)
        catBusiness.isChecked = bundle.getBoolean("catBusiness", false)
        catFinance.isChecked = bundle.getBoolean("catFinance", false)
        catMarketing.isChecked = bundle.getBoolean("catMarketing", false)
    }

    private fun onConfirmButton(){
        val bundle = Bundle()

        if (cityTextField.text != null){
            bundle.putString("city", cityTextField.text.toString())
        }

        if (priceMaxField.text.toString() != ""){
            bundle.putDouble("priceMax", priceMaxField.text.toString().replace(',', '.').toDouble())
        }

        if (priceMinField.text.toString() != ""){
            bundle.putDouble("priceMin", priceMinField.text.toString().replace(',', '.').toDouble())
        }

        if (catIT.isChecked){
            bundle.putBoolean("catIT", true)
        }
        if (catBusiness.isChecked){
            bundle.putBoolean("catBusiness", true)
        }
        if (catFinance.isChecked){
            bundle.putBoolean("catFinance", true)
        }
        if (catMarketing.isChecked){
            bundle.putBoolean("catMarketing", true)
        }

        setFragmentResult("new_filters", bundle)
        (activity as UserMainPageActivity).setFragment(SearchFragment())
    }

}