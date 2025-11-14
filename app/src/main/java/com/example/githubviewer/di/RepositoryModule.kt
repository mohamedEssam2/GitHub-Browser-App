package com.example.githubviewer.di

import com.example.data.remote.GitHubApi
import com.example.data.remote.TokenApi
import com.example.data.repository.GitHubRepositoryImpl
import com.example.data.repository.SecureStorage
import com.example.domain.repository.GitHubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGitHubRepository(
        gitHubApi: GitHubApi,
        tokenApi: TokenApi,
        secureStorage: SecureStorage
    ): GitHubRepository {
        return GitHubRepositoryImpl(
            gitHubApi = gitHubApi,
            tokenApi = tokenApi,
            secureStorage = secureStorage
        )
    }
}