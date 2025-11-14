package com.example.data.remote

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("user/repos")
    suspend fun getRepositories(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): List<RepositoryDto>

    @GET("repos/{owner}/{repo}/branches")
    suspend fun getBranches(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): List<BranchDto>

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): SearchResponseDto
}

data class RepositoryDto(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "description") val description: String?,
    @Json(name = "private") val isPrivate: Boolean,
    @Json(name = "stargazers_count") val stars: Int,
    @Json(name = "forks_count") val forks: Int,
    @Json(name = "language") val language: String?,
    @Json(name = "updated_at") val updatedAt: String,
    @Json(name = "owner") val owner: UserDto
)

data class UserDto(
    @Json(name = "id") val id: Long,
    @Json(name = "login") val login: String,
    @Json(name = "avatar_url") val avatarUrl: String
)

data class BranchDto(
    @Json(name = "name") val name: String,
    @Json(name = "commit") val commit: CommitDto,
    @Json(name = "protected") val isProtected: Boolean
)

data class CommitDto(
    @Json(name = "sha") val sha: String,
    @Json(name = "url") val url: String
)

data class SearchResponseDto(
    @Json(name = "items") val items: List<RepositoryDto>
)