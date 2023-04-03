package com.arcanit.test.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Repo(
    val name: String,
    val description: String?,
    val forks_count: Int,
    val html_url: String,
    val contents_url: String,
    val private:Boolean
)

@JsonClass(generateAdapter = true)
data class ResponseRepoData(
    val total_count: String,
    val incomplete_results: Boolean,
    val items: List<Repo>
)