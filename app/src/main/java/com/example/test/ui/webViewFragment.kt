package com.example.test.ui
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString("url") ?: ""
        loadingWebView(url) // Function to load web view page
        binding.share.setOnClickListener {
            showShareDialog(url)
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

    private fun showShareDialog(newsUrl: String) {
        val shareIntend = Intent(Intent.ACTION_SEND)
        shareIntend.type = "text/plain" // type of message to send
        shareIntend.putExtra(Intent.EXTRA_TEXT, newsUrl)
        context?.startActivity(Intent.createChooser(shareIntend, ""))
    }

}