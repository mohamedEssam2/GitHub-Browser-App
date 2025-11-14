package com.example.domain

import com.example.core.common.Result
import com.example.domain.model.Repository
import com.example.domain.model.User
import com.example.domain.repository.GitHubRepository
import com.example.domain.usecase.GetRepositoriesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class GetRepositoriesUseCaseTest {

    private val repository = mockk<GitHubRepository>()
    private val useCase = GetRepositoriesUseCase(repository)

    @Test
    fun `invoke should return repositories when successful`(): Unit = runTest {
        // Given
        val expectedRepos = listOf(
            Repository(
                id = 1,
                name = "repo1",
                fullName = "user/repo1",
                description = "Test repo",
                isPrivate = false,
                stars = 10,
                forks = 2,
                language = "Kotlin",
                updatedAt = "2024-01-01T00:00:00Z",
                owner = User(1, "user", "avatar.jpg")
            )
        )
        coEvery { repository.getRepositories(any()) } returns expectedRepos

        // When
        val result = useCase()

        // Then
        assertTrue(result is Result.Success)
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // Given
        val expectedException = IOException("Network error")
        coEvery { repository.getRepositories(any()) } throws expectedException

        // When
        val result = useCase()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedException, (result as Result.Success).data)
    }
}