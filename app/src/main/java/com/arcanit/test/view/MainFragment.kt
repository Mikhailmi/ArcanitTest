package com.arcanit.test.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import com.arcanit.test.R
import com.arcanit.test.adaptersAndSources.RepoAdapter
import com.arcanit.test.adaptersAndSources.UserAdapter
import com.arcanit.test.databinding.FragmentMainBinding
import com.arcanit.test.view.models.ReposViewModel
import com.arcanit.test.view.models.UsersViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UsersViewModel
    private lateinit var repoViewModel: ReposViewModel
    private var userAdapter = UserAdapter { string -> onUserClick(string) }
    private var repoAdapter = RepoAdapter { string -> onRepoClick(string) }

    private var jobFailure: Job? = null
    private var isLoading: Job? = null

    private fun onUserClick(htmlUrl: String?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(htmlUrl))
        jobFailure?.cancel()
        isLoading?.cancel()
        startActivity(browserIntent)
    }

    private fun onRepoClick(contentsUrl: String?) {
        val outPut = contentsUrl?.substring(0, contentsUrl.length - 7)
        jobFailure?.cancel()
        isLoading?.cancel()
        parentFragmentManager.commit {
            val bundle = Bundle().apply {
                putString(CONTENTS_URL, outPut)
            }
            replace<RepoDirViewFragment>(R.id.container, args = bundle)
            addToBackStack(RepoDirViewFragment::class.java.simpleName)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = ConcatAdapter(userAdapter, repoAdapter)

        binding.request.addTextChangedListener {
            binding.button.isEnabled = binding.request.text.toString().length > 2
        }

        binding.button.setOnClickListener {
            repoViewModel = ReposViewModel(binding.request.text.toString())
            userViewModel = UsersViewModel(binding.request.text.toString())
            repoViewModel.pagedRepos.onEach {
                repoAdapter.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            userViewModel.pagedUsers.onEach {
                userAdapter.submitData(it)

            }.launchIn(viewLifecycleOwner.lifecycleScope)
            jobFailure?.cancel()

            jobFailure = viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    userViewModel.pSource.isFailure.collect {
                        if (it.endsWith("403 ", true))
                            Toast.makeText(requireContext(), "403 Forbidden!", Toast.LENGTH_SHORT)
                                .show()
                        else if (it != "") {
                            binding.tryAgain.visibility = View.VISIBLE
                            binding.tryAgain.isEnabled = true
                            binding.errorMessage.text = it
                            binding.errorMessage.visibility = View.VISIBLE
                        } else {
                            binding.tryAgain.visibility = View.GONE
                            binding.errorMessage.visibility = View.GONE
                            binding.errorMessage.text = ""
                            binding.tryAgain.isEnabled = false
                        }
                    }
                }
            }
            isLoading?.cancel()
            isLoading = viewLifecycleOwner.lifecycleScope.launch {
                userViewModel.pSource.isLoading.collect {
                    if (it) {
                        binding.progress.visibility = View.VISIBLE
                        binding.button.isEnabled = false
                        binding.request.isEnabled = false
                    } else {
                        binding.progress.visibility = View.GONE
                        binding.request.isEnabled = true
                        if (binding.request.text.toString().length > 2)
                            binding.button.isEnabled = true
                    }
                }
            }
            binding.tryAgain.setOnClickListener {
                binding.button.callOnClick()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        fun newInstance() = MainFragment()
        const val CONTENTS_URL = "contentsUrl"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        jobFailure?.cancel()
        isLoading?.cancel()
    }
}