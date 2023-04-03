package com.arcanit.test.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arcanit.test.databinding.WebViewFileBinding

class WebViewFileFragment : Fragment() {

    private var _binding: WebViewFileBinding? = null
    private val binding get() = _binding!!

    private var htmlUrl:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
             htmlUrl = it.getString(HTML_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WebViewFileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (htmlUrl!==null)
        binding.webView.loadUrl(htmlUrl!!)
    }

    companion object {
        const val HTML_URL = "htmlUrl"
    }

}