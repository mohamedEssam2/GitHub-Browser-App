package com.example.domain.model


data class Repository(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val isPrivate: Boolean,
    val stars: Int,
    val forks: Int,
    val language: String?,
    val updatedAt: String,
    val owner: User
)

data class User(
    val id: Long,
    val login: String,
    val avatarUrl: String
)

data class Branch(
    val name: String,
    val commit: Commit,
    val isProtected: Boolean
)

data class Commit(
    val sha: String,
    val url: String
)