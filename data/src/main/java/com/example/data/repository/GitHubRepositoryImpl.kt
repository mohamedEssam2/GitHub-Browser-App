package com.example.data.repository

import com.example.data.remote.BranchDto
import com.example.data.remote.CommitDto
import com.example.data.remote.GitHubApi
import com.example.data.remote.RepositoryDto
import com.example.data.remote.TokenApi
import com.example.data.remote.UserDto
import com.example.domain.model.Branch
import com.example.domain.model.Commit
import com.example.domain.model.Repository
import com.example.domain.model.User
import com.example.domain.repository.GitHubRepository
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val gitHubApi: GitHubApi,
    private val tokenApi: TokenApi,
    private val secureStorage: SecureStorage
) : GitHubRepository {

    override suspend fun getRepositories(page: Int): List<Repository> {
        return gitHubApi.getRepositories(page).map { it.toDomain() }
    }

    override suspend fun getBranches(owner: String, repo: String, page: Int): List<Branch> {
        return gitHubApi.getBranches(owner, repo, page).map { it.toDomain() }
    }

    override suspend fun searchRepositories(query: String): List<Repository> {
        return gitHubApi.searchRepositories("user:${getCurrentUser()} $query").items.map { it.toDomain() }
    }

    override fun getAccessToken(): String? {
        return secureStorage.getAccessToken()
    }

    override fun saveAccessToken(token: String) {
        secureStorage.saveAccessToken(token)
    }

    override fun clearAccessToken() {
        secureStorage.clearAccessToken()
    }

    private suspend fun getCurrentUser(): String {
        // In a real app, you'd get this from user info endpoint
        return "current_user"
    }
}

private fun RepositoryDto.toDomain(): Repository {
    return Repository(
        id = id,
        name = name,
        fullName = fullName,
        description = description,
        isPrivate = isPrivate,
        stars = stars,
        forks = forks,
        language = language,
        updatedAt = updatedAt,
        owner = owner.toDomain()
    )
}

private fun UserDto.toDomain(): User {
    return User(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )
}

private fun BranchDto.toDomain(): Branch {
    return Branch(
        name = name,
        commit = commit.toDomain(),
        isProtected = isProtected
    )
}

private fun CommitDto.toDomain(): Commit {
    return Commit(
        sha = sha,
        url = url
    )
}