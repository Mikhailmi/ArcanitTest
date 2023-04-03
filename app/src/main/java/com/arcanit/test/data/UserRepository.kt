package com.arcanit.test.data

import com.arcanit.test.api.RetrofitUserServices

class UserRepository {
    suspend fun getListUsers(request: String, order:String, perPage: Int, page: Int): ResponseUserData {
        return RetrofitUserServices.searchUsersApi.getResponseData(request, order, perPage, page)
    }
}