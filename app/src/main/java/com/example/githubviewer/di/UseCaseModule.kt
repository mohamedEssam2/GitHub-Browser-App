package com.example.githubviewer.di

import com.example.domain.repository.GitHubRepository
import com.example.domain.usecase.GetBranchesUseCase
import com.example.domain.usecase.GetRepositoriesUseCase
import com.example.domain.usecase.SearchRepositoriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetRepositoriesUseCase(
        repository: GitHubRepository
    ): GetRepositoriesUseCase {
        return GetRepositoriesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetBranchesUseCase(
        repository: GitHubRepository
    ): GetBranchesUseCase {
        return GetBranchesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchRepositoriesUseCase(
        repository: GitHubRepository
    ): SearchRepositoriesUseCase {
        return SearchRepositoriesUseCase(repository)
    }
}