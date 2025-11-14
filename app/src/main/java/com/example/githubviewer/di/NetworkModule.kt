package com.example.githubviewer.di

import com.example.data.remote.GitHubApi
import com.example.data.remote.TokenApi
import com.example.data.repository.SecureStorage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.github.com/"
    private const val AUTH_BASE_URL = "https://github.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(
        secureStorage: SecureStorage
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val token = secureStorage.getAccessToken()

                val requestBuilder = original.newBuilder()
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("User-Agent", "GitHubBrowser-App")

                token?.let {
                    requestBuilder.header("Authorization", "token $it")
                }

                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideGitHubApi(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): GitHubApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GitHubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenApi(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): TokenApi {
        return Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TokenApi::class.java)
    }
}