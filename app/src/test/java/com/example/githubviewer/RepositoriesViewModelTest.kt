package com.example.githubviewer

import app.cash.turbine.test
import com.example.domain.model.Repository
import com.example.domain.usecase.GetRepositoriesUseCase
import com.example.domain.usecase.SearchRepositoriesUseCase
import com.example.githubviewer.presentation.repositories.RepositoriesViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoriesViewModelTest {

    private val getRepositoriesUseCase = mockk<GetRepositoriesUseCase>()
    private val searchRepositoriesUseCase = mockk<SearchRepositoriesUseCase>()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `initial state should load repositories`(): Unit = runTest {
        // Given
        val expectedRepos = listOf(mockk<Repository>())
        coEvery { getRepositoriesUseCase() } returns Result.success(expectedRepos)

        // When
        val viewModel = RepositoriesViewModel(getRepositoriesUseCase, searchRepositoriesUseCase)

        // Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(emptyList<Repository>(), initialState.repositories)

            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertEquals(expectedRepos, successState.repositories)
            assertFalse(successState.isLoading)
        }
    }
}