package com.example.domain.repository

import com.example.domain.model.Branch
import com.example.domain.model.Repository

interface GitHubRepository {
    suspend fun getRepositories(page: Int = 1): List<Repository>
    suspend fun getBranches(owner: String, repo: String, page: Int = 1): List<Branch>
    suspend fun searchRepositories(query: String): List<Repository>
    fun getAccessToken(): String?
    fun saveAccessToken(token: String)
    fun clearAccessToken()
}