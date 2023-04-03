package com.arcanit.test.data

import com.arcanit.test.api.RetrofitRepoServices

class RepoRepository {
    suspend fun getListRepo(request: String, order:String, perPage: Int, page: Int): ResponseRepoData {
        return RetrofitRepoServices.searchReposApi.getResponseData(request, order, perPage, page)
    }
}