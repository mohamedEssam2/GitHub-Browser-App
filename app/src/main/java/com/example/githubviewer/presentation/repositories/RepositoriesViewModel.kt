package com.example.githubviewer.presentation.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Repository
import com.example.domain.usecase.GetRepositoriesUseCase
import com.example.domain.usecase.SearchRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.core.common.Result;

@HiltViewModel
class RepositoriesViewModel @Inject constructor(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RepositoriesUiState())
    val uiState: StateFlow<RepositoriesUiState> = _uiState

    private val searchQuery = MutableStateFlow("")

    init {
        loadRepositories()
        observeSearch()
    }

    fun onEvent(event: RepositoriesEvent) {
        when (event) {
            is RepositoriesEvent.Refresh -> refreshRepositories()
            is RepositoriesEvent.Search -> searchQuery.value = event.query
            is RepositoriesEvent.LoadMore -> loadMoreRepositories()
            RepositoriesEvent.Retry -> loadRepositories()
        }
    }

    private fun observeSearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotEmpty()) {
                        searchRepositories(query)
                    } else {
                        loadRepositories()
                    }
                }
        }
    }

    private fun loadRepositories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = getRepositoriesUseCase()) {
                is Result.Success<*> -> {
                    _uiState.value = _uiState.value.copy(
                        repositories = result.data as List<Repository>,
                        isLoading = false
                    )
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.exception?.message ?: "Failed to load repositories",
                        isLoading = false
                    )
                }

                else -> {}
            }


        }
    }

    private fun refreshRepositories() {
        // Implementation for refresh
        loadRepositories()
    }

    private fun loadMoreRepositories() {
        // Implementation for pagination
    }

    private fun searchRepositories(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = searchRepositoriesUseCase(query)) {
                is Result.Success<*> -> {
                    _uiState.value = _uiState.value.copy(
                        repositories = result.data as List<Repository>,
                        isLoading = false
                    )
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.exception?.message ?: "Search failed",
                        isLoading = false
                    )
                }

                Result.Loading -> TODO()
            }

        }
    }
}

data class RepositoriesUiState(
    val repositories: List<Repository> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = true
)

sealed class RepositoriesEvent {
    data object Refresh : RepositoriesEvent()
    data class Search(val query: String) : RepositoriesEvent()
    data object LoadMore : RepositoriesEvent()
    data object Retry : RepositoriesEvent()
}