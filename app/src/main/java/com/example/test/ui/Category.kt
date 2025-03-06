package com.example.test.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.test.R
import com.example.test.api.NewsViewModel

class category : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         // make Actions
        Log.d("categoryFragment", "onCreateView: ")

        val view =  inflater.inflate(R.layout.fragment_category, container, false)

//        val args=Bundle()
//        args.putString("category","sports")
      //  findNavController().navigate(R.id.action_category_to_sports,args)
          view.findViewById<CardView>(R.id.sports_cardview).setOnClickListener{

//                      val args=Bundle()
//        args.putString("category","sports")
            //  Navigation.findNavController(view).navigate(R.id.action_category_to_sports)
              navigatetosportsFragment("sports")
          }

        view.findViewById<CardView>(R.id.science_cardview).setOnClickListener {
            navigatetosportsFragment("science")
           // Navigation.findNavController(view).navigate(R.id.action_category_to_sports)
        }
        view.findViewById<CardView>(R.id.technology_cardview).setOnClickListener {
            navigatetosportsFragment("technology")
            //Navigation.findNavController(view).navigate(R.id.action_category_to_sports)
        }
        view.findViewById<CardView>(R.id.business_cardview).setOnClickListener {
            navigatetosportsFragment("business")
          //  Navigation.findNavController(view).navigate(R.id.action_category_to_sports)
        }
        view.findViewById<CardView>(R.id.health_cardView).setOnClickListener {
            navigatetosportsFragment("health")
            //  Navigation.findNavController(view).navigate(R.id.action_category_to_sports)
        }
        view.findViewById<CardView>(R.id.entertainment_cardView).setOnClickListener {
            navigatetosportsFragment("entertainment")
            //  Navigation.findNavController(view).navigate(R.id.action_category_to_sports)
        }
        return view
    }

    private fun navigatetosportsFragment(category: String) {
        val bundle =Bundle().apply {
            putString("category",category)
        }
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_category_to_sports ,bundle)
        }

    }

}