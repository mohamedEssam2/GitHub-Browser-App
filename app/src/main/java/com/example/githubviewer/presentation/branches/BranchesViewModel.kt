package com.example.githubviewer.presentation.branches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Branch
import com.example.domain.usecase.GetBranchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.core.common.Result

@HiltViewModel
class BranchesViewModel @Inject constructor(
    private val getBranchesUseCase: GetBranchesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BranchesUiState())
    val uiState: StateFlow<BranchesUiState> = _uiState

    fun loadBranches(owner: String, repo: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = getBranchesUseCase(owner, repo)) {
                is Result.Success<*> -> {
                    _uiState.value = _uiState.value.copy(
                        branches = result.data as List<Branch>,
                        isLoading = false
                    )
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.exception?.message ?: "Failed to load branches",
                        isLoading = false
                    )
                }

                Result.Loading -> TODO()
            }
        }
    }
}

data class BranchesUiState(
    val branches: List<Branch> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)