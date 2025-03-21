package dev.mingaliev.wtf.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mingaliev.wtf.ui.feature.accounts.AccountsScreen
import dev.mingaliev.wtf.ui.feature.add.AddTransactionScreen
import dev.mingaliev.wtf.ui.feature.add.AddTransactionViewModel
import dev.mingaliev.wtf.ui.feature.main.MainScreen
import dev.mingaliev.wtf.ui.feature.main.MainViewModel
import dev.mingaliev.wtf.ui.feature.main.Screen
import dev.mingaliev.wtf.ui.feature.profile.ProfileScreen
import dev.mingaliev.wtf.ui.feature.profile.ProfileViewModel
import dev.mingaliev.wtf.ui.feature.statistics.StatisticsScreen
import dev.mingaliev.wtf.ui.feature.statistics.StatisticsViewModel

@Composable
fun NavigationContainer(mainViewModel: MainViewModel) {
    val uiState by mainViewModel.uiState.collectAsState()
    val statisticsViewModel = remember { StatisticsViewModel(mainViewModel.repository) }
    val profileViewModel = remember { ProfileViewModel() }
    val addTransactionViewModel = remember { AddTransactionViewModel() }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                BottomNavigationItem(
                    selected = uiState.currentScreen == Screen.HOME,
                    onClick = { mainViewModel.onScreenSelected(Screen.HOME) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = when (uiState.currentScreen == Screen.HOME) {
                                true -> MaterialTheme.colors.primary
                                false -> MaterialTheme.colors.onSurface
                            }
                        )
                    },
                    label = {
                        Text(
                            text = "Home",
                            maxLines = 1,
                            style = MaterialTheme.typography.caption
                        )
                    }
                )

                BottomNavigationItem(
                    selected = uiState.currentScreen == Screen.STATISTICS,
                    onClick = { mainViewModel.onScreenSelected(Screen.STATISTICS) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Analytics",
                            tint = when (uiState.currentScreen == Screen.STATISTICS) {
                                true -> MaterialTheme.colors.primary
                                false -> MaterialTheme.colors.onSurface
                            }
                        )
                    },
                    label = {
                        Text(
                            text = "Analytics",
                            maxLines = 1,
                            style = MaterialTheme.typography.caption
                        )
                    }
                )

                BottomNavigationItem(
                    selected = uiState.currentScreen == Screen.TRANSACTIONS,
                    onClick = { mainViewModel.onScreenSelected(Screen.TRANSACTIONS) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = when (uiState.currentScreen == Screen.TRANSACTIONS) {
                                true -> MaterialTheme.colors.primary
                                false -> MaterialTheme.colors.onSurface
                            }
                        )
                    },
                    label = {
                        Text(
                            text = "Add",
                            maxLines = 1,
                            style = MaterialTheme.typography.caption
                        )
                    }
                )

                BottomNavigationItem(
                    selected = uiState.currentScreen == Screen.ACCOUNTS,
                    onClick = { mainViewModel.onScreenSelected(Screen.ACCOUNTS) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Accounts",
                            tint = when (uiState.currentScreen == Screen.ACCOUNTS) {
                                true -> MaterialTheme.colors.primary
                                false -> MaterialTheme.colors.onSurface
                            }
                        )
                    },
                    label = {
                        Text(
                            text = "Accounts",
                            maxLines = 1,
                            style = MaterialTheme.typography.caption
                        )
                    }
                )

                BottomNavigationItem(
                    selected = uiState.currentScreen == Screen.PROFILE,
                    onClick = { mainViewModel.onScreenSelected(Screen.PROFILE) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = when (uiState.currentScreen == Screen.PROFILE){
                                true -> MaterialTheme.colors.primary
                                false -> MaterialTheme.colors.onSurface
                            }
                        )
                    },
                    label = {
                        Text(
                            text = "Profile",
                            maxLines = 1,
                            style = MaterialTheme.typography.caption
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        when (uiState.currentScreen) {
            Screen.HOME -> MainScreen(
                viewModel = mainViewModel,
                paddingValues = paddingValues,
            )

            Screen.STATISTICS -> StatisticsScreen(
                viewModel = statisticsViewModel,
                paddingValues = paddingValues,
            )

            Screen.TRANSACTIONS -> AddTransactionScreen(
                viewModel = addTransactionViewModel,
                onDismiss = { mainViewModel.onNavigateBack() },
                onAddTransaction = { transaction -> mainViewModel.onAddTransaction(transaction) },
                paddingValues = paddingValues,
            )

            Screen.ACCOUNTS -> AccountsScreen(
                paddingValues = paddingValues,
            )

            Screen.PROFILE -> ProfileScreen(
                viewModel = profileViewModel,
                paddingValues = paddingValues
            )
        }
    }
}
