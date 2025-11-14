package com.example.domain.usecase

import com.example.domain.model.Branch
import com.example.domain.repository.GitHubRepository
import com.example.core.common.Result

class GetBranchesUseCase(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(owner: String, repo: String, page: Int = 1): Result<List<Branch>> {
        return try {
            val branches = repository.getBranches(owner, repo, page)
            Result.Success(branches)
        } catch (e: Exception) {
            Result.Failure(e.message ?: "Failed to load branches", e)
        }
    }
}