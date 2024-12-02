package com.example.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation

class category : Fragment() {
     lateinit var sport:CardView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         // make Actions
        val view =  inflater.inflate(R.layout.fragment_category, container, false)
          view.findViewById<CardView>(R.id.sports_cardview).setOnClickListener {
              Navigation.findNavController(view).navigate(R.id.action_category_to_sports)
          }

        view.findViewById<CardView>(R.id.science_cardview).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_category_to_sports)
        }
        view.findViewById<CardView>(R.id.technology_cardview).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_category_to_sports)
        }
        view.findViewById<CardView>(R.id.business_cardview).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_category_to_sports)
        }
        return view
    }

}