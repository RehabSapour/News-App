package com.example.test.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.test.R
import com.example.test.sharedPreferences
import java.util.Locale


class setting : Fragment() {
   lateinit var languageSpinner :Spinner
   // lateinit var modeSpinner: Spinner
//    private lateinit var themeSpinner: Spinner

    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_setting, container, false)

      //  sharedPreferences = requireContext().getSharedPreferences("setting", Context.MODE_PRIVATE)
        val languages = arrayOf("English" ,"Arabic")
        languageSpinner = view.findViewById(R.id.languageSpinner)

        val adapterlanguage:ArrayAdapter<String>? = activity?.let {
            ArrayAdapter(
                requireContext()
                ,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
               // R.layout.drop_down_item
                ,
                languages
            )
        }

            adapterlanguage?.setDropDownViewResource(R.layout.drop_down_item)
          languageSpinner.adapter=adapterlanguage
          languageSpinner.setBackgroundResource(R.drawable.custom_spinner_shape) //
        val savedLanguage= sharedPreferences?.getString("Language","English")
        val positionLanguage= adapterlanguage?.getPosition(savedLanguage)
        positionLanguage?.let { languageSpinner.setSelection(it) }

        languageSpinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectLanguage=parent?.getItemAtPosition(position).toString()
                if(savedLanguage != selectLanguage){
                    changeLanguage(selectLanguage)
                    savedLanguage(selectLanguage)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        return view
    }

    private fun changeLanguage(selectLanguage: String) {
      val local:Locale = when(selectLanguage){
          "English" -> Locale("en")
          "Arabic" -> Locale("ar")
          else -> Locale.getDefault()
      }
        val config = resources.configuration
        config.setLocale(local)
        resources.updateConfiguration(config,resources.displayMetrics)
        requireActivity().recreate()
    }

    private fun savedLanguage(selectedLanguage:String){
        sharedPreferences?.edit()?.putString("Language" , selectedLanguage)?.apply()
    }



}



