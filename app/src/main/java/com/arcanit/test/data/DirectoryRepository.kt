package com.arcanit.test.data

import com.arcanit.test.api.RetrofitDirectoryServices

class DirectoryRepository {
    suspend fun getListDir(
        url:String
    ): List<DirectoryData> {
        return RetrofitDirectoryServices.searchDirInside.getResponseData(url)
    }
}
