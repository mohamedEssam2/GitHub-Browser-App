package com.example.domain.usecase

import com.example.domain.model.Repository
import com.example.domain.repository.GitHubRepository
import com.example.core.common.Result

class GetRepositoriesUseCase(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(page: Int = 1): Result<List<Repository>> {
        return try {
            val repos = repository.getRepositories(page)
            Result.Success(repos)
        } catch (e: Exception) {
            Result.Failure(e.message ?: "Failed to load repositories", e)
        }
    }
}