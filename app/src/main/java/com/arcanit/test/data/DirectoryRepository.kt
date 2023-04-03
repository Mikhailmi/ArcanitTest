package com.arcanit.test.data

import com.arcanit.test.api.RetrofitDirectoryServices

class DirectoryRepository {
    suspend fun getListDir(
        listString: List<String>
    ): List<DirectoryData> {
        return RetrofitDirectoryServices.searchDirInside.getResponseData(
            listString[0], listString[1], listString[2])
    }
    suspend fun getListInsideDir(
    listString:List<String>
    ): List<DirectoryData> {
        val string4 = if (listString.size > 4 && listString[3] != "") listString[3] else ""
        val string5 = if (listString.size > 5 && listString[4] != "") listString[4] else ""
        val string6 = if (listString.size > 6 && listString[5] != "") listString[5] else ""
        val string7 = if (listString.size > 7 && listString[6] != "") listString[6] else ""
        val string8 = if (listString.size > 8 && listString[7] != "") listString[7] else ""
        return RetrofitDirectoryServices.searchDirInsideDir.getResponseData(
            listString[0], listString[1], listString[2], string4, string5,
            string6, string7, string8, listString.last()
        )
    }
}
