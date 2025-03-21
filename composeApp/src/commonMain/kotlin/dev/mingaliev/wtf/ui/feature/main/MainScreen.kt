package dev.mingaliev.wtf.ui.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    paddingValues: PaddingValues
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentTime =
        remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }
    val greeting = when (currentTime.hour) {
        in 5..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        in 18..22 -> "Good evening"
        else -> "Good night"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hello, ${uiState.userName}!",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = greeting,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Total balance",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "${uiState.totalBalance.toInt()} ₽",
                                    style = MaterialTheme.typography.headlineLarge
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Total expenses",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "-${uiState.totalExpense.toInt()} ₽",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        GoalProgressBar(
                            totalExpense = uiState.totalExpense,
                            targetExpense = uiState.targetExpense
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Weekly income",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "${uiState.weeklyIncome.toInt()} ₽",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Weekly expenses",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "-${uiState.weeklyExpense.toInt()} ₽",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }


            }

            TabRow(
                selectedTabIndex = when (uiState.selectedPeriod) {
                    Period.DAILY -> 0
                    Period.WEEKLY -> 1
                    Period.MONTHLY -> 2
                }
            ) {
                Tab(
                    selected = uiState.selectedPeriod == Period.DAILY,
                    onClick = { viewModel.onPeriodSelected(Period.DAILY) },
                    text = { Text("Day") }
                )
                Tab(
                    selected = uiState.selectedPeriod == Period.WEEKLY,
                    onClick = { viewModel.onPeriodSelected(Period.WEEKLY) },
                    text = { Text("Week") }
                )
                Tab(
                    selected = uiState.selectedPeriod == Period.MONTHLY,
                    onClick = { viewModel.onPeriodSelected(Period.MONTHLY) },
                    text = { Text("Month") }
                )
            }
        }

        items(uiState.filteredTransactions) { transaction ->
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(transaction.category.color))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = transaction.category.emoji,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Column {
                            Text(
                                text = transaction.title,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = transaction.category.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    Text(
                        text = "${transaction.amount.toInt()} ₽",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (transaction.amount < 0) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
            }
        }
    }
}
