package com.example.githubviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.githubviewer.presentation.auth.AuthState
import com.example.githubviewer.presentation.auth.AuthViewModel
import com.example.githubviewer.presentation.ui.screens.AuthScreen
import com.example.githubviewer.presentation.ui.screens.BranchesScreen
import com.example.githubviewer.presentation.ui.screens.RepositoriesScreen
import com.example.githubviewer.ui.theme.GithubViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubViewerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GitHubBrowserApp()
                }
            }
        }
    }
}

@Composable
fun GitHubBrowserApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            LaunchedEffect(Unit) {
                when (authViewModel.authState.value) {
                    is AuthState.Authenticated -> navController.navigate("repositories")
                    is AuthState.Unauthenticated -> navController.navigate("auth")
                    else -> { /* Wait */ }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        composable("auth") {
            AuthScreen(
                onAuthSuccess = { navController.navigate("repositories") }
            )
        }

        composable("repositories") {
            RepositoriesScreen(
                onRepositoryClick = { repo ->
                    navController.navigate("branches/${repo.owner.login}/${repo.name}")
                },
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate("auth") {
                        popUpTo("repositories") { inclusive = true }
                    }
                }
            )
        }

        composable(
            "branches/{owner}/{repo}",
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""

            BranchesScreen(
                owner = owner,
                repo = repo,
                onNavigateUp = { navController.popBackStack() }
            )
        }
    }
}