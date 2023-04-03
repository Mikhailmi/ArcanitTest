package com.arcanit.test.view.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.arcanit.test.adaptersAndSources.RepoPagingSource

class ReposViewModel(
    request: String, order: String = "desc",
    perPage: Int = 30, page: Int = 1
) : ViewModel() {
    val pagedRepos = Pager(
        config = PagingConfig(pageSize = perPage),
        pagingSourceFactory = {
            RepoPagingSource(request, order, perPage, page)
        }
    ).flow.cachedIn(viewModelScope)
}