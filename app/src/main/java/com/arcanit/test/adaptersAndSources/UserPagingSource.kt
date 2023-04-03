package com.arcanit.test.adaptersAndSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arcanit.test.data.User
import com.arcanit.test.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPagingSource(
    private val request: String, private val order: String,
    private val perPage: Int, private val page: Int
) : PagingSource<Int, User>() {

    private val repository = UserRepository()
    override fun getRefreshKey(state: PagingState<Int, User>): Int = page

    private val _isFailure = MutableStateFlow("")
    val isFailure = _isFailure.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        _isLoading.value = true
        val currPage = params.key ?: page
        return runCatching {
            repository.getListUsers(request, order, perPage, currPage).items
        }.fold(
            onSuccess = {
                _isFailure.value = ""
                _isLoading.value = false
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else currPage + 1
                )
            }, onFailure = {
                _isFailure.value = it.toString()
                _isLoading.value = false
                LoadResult.Error(it)
            }
        )
    }
}