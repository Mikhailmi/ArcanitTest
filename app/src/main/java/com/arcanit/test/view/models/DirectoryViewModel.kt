package com.arcanit.test.view.models

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arcanit.test.data.DirectoryRepository
import com.arcanit.test.data.DirectoryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DirectoryViewModel private constructor(

    private val repository: DirectoryRepository
) : ViewModel() {
    constructor() : this(DirectoryRepository())

    var initialString: String? = null

    private val _isFailure = MutableStateFlow("")
    val isFailure = _isFailure.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _content = MutableStateFlow<List<DirectoryData>>(emptyList())
    val content: StateFlow<List<DirectoryData>> = _content.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _content.value
    )

    fun loadPremieres() {
        _isLoading.value = true
        val listString = initialString?.let { parseUrl(it) } as MutableList<String>
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                if (listString[3]=="")
                repository.getListDir(listString)
                else
                    repository.getListInsideDir(listString)
            }.fold(
                onSuccess = {
                    _isFailure.value = ""
                    _isLoading.value = false
                    _content.value = it
                },
                onFailure = {
                    _isFailure.value = it.toString()
                    _isLoading.value = false
                    Log.d("ResponseRepoInsideData", it.message ?: "") }
            )
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun parseUrl(url: String): List<String> {
        val firstResult = mutableListOf<String>()
        for (s: String in url.split("https://api.github.com/repos/"))
            firstResult.add(s)
        val result = mutableListOf<String>()
        for (s: String in firstResult[1].split("/"))
        result.add(s)
        if (result[3]!=""){
            val listSplitQuestion = result.last().split("?")
            result[result.lastIndex] = listSplitQuestion[0]
            result.add(listSplitQuestion[1].split("=")[1])
        }
        return result
    }
}

