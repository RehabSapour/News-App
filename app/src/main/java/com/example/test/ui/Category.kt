package com.example.test.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import com.example.test.R

class category : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("categoryFragment", "onCreateView: ")

        val view =  inflater.inflate(R.layout.fragment_category, container, false)
        val isArabic = isArabicLocale()

          view.findViewById<CardView>(R.id.sports_cardview).setOnClickListener{
              handleCategoryClick(isArabic, "sports")
          }

        view.findViewById<CardView>(R.id.science_cardview).setOnClickListener {
            handleCategoryClick(isArabic,"science")
        }

        view.findViewById<CardView>(R.id.technology_cardview).setOnClickListener {
            handleCategoryClick(isArabic,"technology")
        }

        view.findViewById<CardView>(R.id.business_cardview).setOnClickListener {
            handleCategoryClick(isArabic,"business")
        }
        view.findViewById<CardView>(R.id.health_cardView).setOnClickListener {
            handleCategoryClick(isArabic,"health")

        }
        view.findViewById<CardView>(R.id.entertainment_cardView).setOnClickListener {
            handleCategoryClick(isArabic,"entertainment")
        }

        return view
    }


    private fun handleCategoryClick(isArabic: Boolean, category: String) {
        if (isArabic) {
            // Show "Fragment not found" dialog in Arabic
            showFragmentNotFoundDialog()
        } else {
            // Navigate to the news fragment for non-Arabic locales
            navigatetosportsFragment(category)
        }
    }

    private fun isArabicLocale(): Boolean {
        val currentLocale = requireContext().resources.configuration.locales[0]
        return currentLocale.language == "ar" // Check if language is Arabic
    }

    @SuppressLint("MissingInflatedId")
    private fun showFragmentNotFoundDialog() {
        // Inflate custom layout
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.custom_dialog, null)

        // Build dialog
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomDialogStyle)
            .setView(dialogView)
            .create()

        // Set transparent background to show rounded corners
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Handle OK button click
        dialogView.findViewById<Button>(R.id.dialog_ok_button).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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