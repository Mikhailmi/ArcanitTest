package com.arcanit.test.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DirectoryData(
    val name:String,
    val path:String,
    val type:String,
    val html_url:String,
    val _links:Any
)