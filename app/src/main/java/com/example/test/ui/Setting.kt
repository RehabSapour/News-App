package com.example.test.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import com.example.test.R
import com.example.test.sharedPreferences
import java.util.Locale


class setting : Fragment() {
   lateinit var languageSpinner :Spinner
   private lateinit var seekBar: SeekBar
    private lateinit var sharedPreferencesforFont: SharedPreferences

    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_setting, container, false)

        val languages = arrayOf("English" ,"Arabic")
        languageSpinner = view.findViewById(R.id.languageSpinner)

        val adapterlanguage:ArrayAdapter<String>? = activity?.let {
            ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, languages)
        }

        adapterlanguage?.setDropDownViewResource(R.layout.drop_down_item)

          languageSpinner.adapter=adapterlanguage
          languageSpinner.setBackgroundResource(R.drawable.custom_spinner_shape)

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


        /////////////////////////////////////////////////////////////////////////

         FontSetUp(view)

        return view
    }

    private fun FontSetUp(view: View) {
        seekBar = view.findViewById(R.id.fontSizeSeekBar)
        val seekBarCounter = view.findViewById<TextView>(R.id.seekBarCounter)

        sharedPreferencesforFont =
            requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val savedFontSize = sharedPreferencesforFont.getInt("font_size", 16)
        seekBar.progress = savedFontSize

        // Set the initial value
        seekBarCounter.text = seekBar.progress.toString()
        // تغيير حجم الخط عند تحريك الـ SeekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBarCounter.text = progress.toString()
                saveFontSize(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun saveFontSize(size: Int) {
        sharedPreferencesforFont.edit().putInt("font_size", size).apply()
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



