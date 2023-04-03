package com.arcanit.test.view.models

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
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                repository.getListDir(initialString!!.substring(23))
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
}

