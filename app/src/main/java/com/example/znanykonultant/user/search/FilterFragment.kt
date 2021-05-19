package com.example.znanykonultant.user.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.znanykonultant.R
import com.example.znanykonultant.user.UserMainPageActivity

class FilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filter, container, false)
        view.findViewById<Button>(R.id.confirmButton).setOnClickListener {
            (activity as UserMainPageActivity).setFragment(SearchFragment())
        }
        return view
    }

}