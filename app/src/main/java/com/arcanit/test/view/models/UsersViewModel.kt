package com.arcanit.test.view.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.arcanit.test.adaptersAndSources.UserPagingSource

class UsersViewModel(
    request: String, order: String = "desc",
    perPage: Int = 30, page: Int = 1
) : ViewModel() {
    var pSource = UserPagingSource(request, order, perPage, page)
    val pagedUsers = Pager(
        config = PagingConfig(pageSize = perPage),
        pagingSourceFactory = {
            pSource
        }
    ).flow.cachedIn(viewModelScope)
}