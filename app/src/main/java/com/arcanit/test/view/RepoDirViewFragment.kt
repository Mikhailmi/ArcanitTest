package com.arcanit.test.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import com.arcanit.test.R
import com.arcanit.test.adaptersAndSources.RepoDirAdapter
import com.arcanit.test.adaptersAndSources.RepoFileAdapter
import com.arcanit.test.databinding.RepoViewFragmentBinding
import com.arcanit.test.view.models.DirectoryViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RepoDirViewFragment : Fragment() {

    private var _binding: RepoViewFragmentBinding? = null
    private val binding get() = _binding!!

    private var contentsUrl: String? = null

    private var viewModel = DirectoryViewModel()
    private var repoDirAdapter = RepoDirAdapter { string -> onDirClick(string) }
    private var repoFileAdapter = RepoFileAdapter { string -> onFileClick(string) }

    private var jobFailure: Job? = null
    private var jobLoading: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contentsUrl = it.getString(CONTENTS_URL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initialString = contentsUrl
        viewModel.loadPremieres()
        binding.recyclerView.adapter = ConcatAdapter(repoDirAdapter, repoFileAdapter)

        viewModel.content.onEach { it ->
            if (it.isNotEmpty()) {
                repoDirAdapter.submitList(it.filter { it.type == DIR })
                repoFileAdapter.submitList(it.filter { it.type == FILE })
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        jobFailure?.cancel()
        jobFailure = viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFailure.collect {
                    checkFailure(it)
                }

            }
        }
        jobLoading?.cancel()
        jobLoading = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect {
                binding.progress.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
        binding.tryAgain.setOnClickListener {
            jobFailure?.cancel()
            jobLoading?.cancel()
            viewModel = DirectoryViewModel()
            viewModel.initialString = contentsUrl
            viewModel.loadPremieres()
            viewModel.content.onEach { it ->
                if (it.isNotEmpty()) {
                    repoDirAdapter.submitList(it.filter { it.type == DIR })
                    repoFileAdapter.submitList(it.filter { it.type == FILE })
                }
                jobFailure = viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.isFailure.collect { checkFailure(it) }
                    }
                }
                jobLoading = viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.isLoading.collect {
                        binding.progress.visibility = if (it) View.VISIBLE else View.GONE
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun checkFailure(it: String) {
        if (it != "") {
            binding.tryAgain.visibility = View.VISIBLE
            binding.tryAgain.isEnabled = true
            binding.errorMessage.text = it
            binding.errorMessage.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.recyclerView.isEnabled = false
        } else {
            binding.tryAgain.visibility = View.GONE
            binding.errorMessage.visibility = View.GONE
            binding.errorMessage.text = ""
            binding.tryAgain.isEnabled = false
            binding.recyclerView.visibility = View.VISIBLE
            binding.recyclerView.isEnabled = true
        }
    }

    private fun onFileClick(htmlUrl: String?) {
        jobFailure?.cancel()
        jobLoading?.cancel()
        parentFragmentManager.commit {
            val bundle = Bundle().apply {
                putString(HTML_URL, htmlUrl)
            }
            replace<WebViewFileFragment>(R.id.container, args = bundle)
            addToBackStack(WebViewFileFragment::class.java.simpleName)
        }
    }

    private fun onDirClick(htmlUrl: String?) {
        jobFailure?.cancel()
        jobLoading?.cancel()
        parentFragmentManager.commit {
            val bundle = Bundle().apply {
                putString(CONTENTS_URL, htmlUrl)
            }
            replace<RepoDirViewFragment>(R.id.container, args = bundle)
            addToBackStack(RepoDirViewFragment::class.java.simpleName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RepoViewFragmentBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        const val CONTENTS_URL = "contentsUrl"
        const val HTML_URL = "htmlUrl"
        const val DIR = "dir"
        const val FILE = "file"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        jobFailure?.cancel()
        jobLoading?.cancel()
    }
}