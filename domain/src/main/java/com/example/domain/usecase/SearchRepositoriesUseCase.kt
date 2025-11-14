package com.example.domain.usecase

import com.example.domain.model.Repository
import com.example.domain.repository.GitHubRepository
import com.example.core.common.Result

class SearchRepositoriesUseCase(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(query: String): Result<List<Repository>> {
        return if (query.isBlank()) {
            invoke()
        } else {
            try {
                val repos = repository.searchRepositories(query)
                Result.Success(repos)
            } catch (e: Exception) {
                Result.Failure(e.message?:"Search failed",e)
            }
        }
    }

    private suspend fun invoke(): Result<List<Repository>> {
        return try {
            val repos = repository.getRepositories()
            Result.Success(repos)

        } catch (e: Exception) {
            Result.Failure(e.message?:"Search failed",e)

        }
    }
}