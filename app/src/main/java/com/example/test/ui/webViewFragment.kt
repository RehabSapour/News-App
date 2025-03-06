package com.example.test.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.example.test.R
import com.example.test.databinding.FragmentWebViewBinding


class webViewFragment : Fragment() {
    lateinit var binding :FragmentWebViewBinding
    private lateinit var constraintLayoutToolbar: ConstraintLayout
    private lateinit var fragmentContainer: FrameLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebViewBinding.inflate(layoutInflater,container,false)
      //  Navigation.findNavController(View).navigate(R.id.action_webViewFragment_to_sports)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString("url") ?: ""
        loadingWebView(url) // Function to load web view page

//        if (binding.webView.canGoBack()) {
//            binding.webView.goBack()
//        } else {
//            findNavController(view).navigate(R.id.action_webViewFragment_to_sports)
//        }
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String):webViewFragment{
            val fragment = webViewFragment()
            val args = Bundle()
            args.putString("url", url)
            fragment.arguments = args
            return fragment
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadingWebView(url:String){
        try {
            Log.d("webview", "loadingWebView: ")
            constraintLayoutToolbar = requireActivity().findViewById(R.id.toolbarConstariant)
            fragmentContainer = requireActivity().findViewById(R.id.fragmentContainerView)
            constraintLayoutToolbar.visibility = View.GONE

        } catch (e: Exception) {
            Log.e("WebViewFragment", "Error initializing views: ${e.message}")
        }

//        //hideToolbarAndExpand()

            binding.webView.settings.javaScriptEnabled = true
            binding.webView.webViewClient = WebViewClient()
            Log.d("urll", "loadingWebViewurl${url}: ")
           binding.webView.loadUrl(url)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webView.destroy()
       constraintLayoutToolbar.visibility = View.VISIBLE // when pressed back the toolbar visible
    }



}