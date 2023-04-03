package com.arcanit.test.adaptersAndSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arcanit.test.data.Repo
import com.arcanit.test.data.RepoRepository

class RepoPagingSource(
    private val request: String, private val order: String,
    private val perPage: Int, private val page: Int
) : PagingSource<Int, Repo>() {

    private val repository = RepoRepository()
    override fun getRefreshKey(state: PagingState<Int, Repo>): Int = page

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val currPage = params.key ?: page
        return runCatching {
            repository.getListRepo(request, order, perPage, currPage).items
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else currPage + 1
                )
            }, onFailure = {
                LoadResult.Error(it)  }
        )
    }
}