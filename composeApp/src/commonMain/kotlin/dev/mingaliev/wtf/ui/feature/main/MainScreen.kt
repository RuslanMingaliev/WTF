package dev.mingaliev.wtf.ui.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
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
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello, ${uiState.userName}!",
                        style = MaterialTheme.typography.h5
                    )
                    Text(
                        text = greeting,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
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
                                style = MaterialTheme.typography.subtitle2
                            )
                            Text(
                                text = "${uiState.totalBalance.toInt()} ₽",
                                style = MaterialTheme.typography.h4
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Total expenses",
                                style = MaterialTheme.typography.subtitle2
                            )
                            Text(
                                text = "-${uiState.totalExpense.toInt()} ₽",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.error
                            )
                        }
                    }

                    GoalProgressBar(
                        totalExpense = uiState.totalExpense,
                        targetExpense = uiState.targetExpense
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
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
                                style = MaterialTheme.typography.subtitle2
                            )
                            Text(
                                text = "${uiState.weeklyIncome.toInt()} ₽",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.primary
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Weekly expenses",
                                style = MaterialTheme.typography.subtitle2
                            )
                            Text(
                                text = "-${uiState.weeklyExpense.toInt()} ₽",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.error
                            )
                        }
                    }
                }
            }
        }

        item {
            TabRow(
                selectedTabIndex = when (uiState.selectedPeriod) {
                    Period.DAILY -> 0
                    Period.WEEKLY -> 1
                    Period.MONTHLY -> 2
                },
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.primary,
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(
                                tabPositions[when (uiState.selectedPeriod) {
                                    Period.DAILY -> 0
                                    Period.WEEKLY -> 1
                                    Period.MONTHLY -> 2
                                }]
                            )
                            .height(2.dp)
                            .background(MaterialTheme.colors.primary)
                    )
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
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        Column {
                            Text(
                                text = transaction.title,
                                style = MaterialTheme.typography.subtitle1
                            )
                            Text(
                                text = transaction.category.name,
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    Text(
                        text = "${transaction.amount.toInt()} ₽",
                        style = MaterialTheme.typography.subtitle1,
                        color = if (transaction.amount < 0) MaterialTheme.colors.error else MaterialTheme.colors.primary
                    )
                }
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                )
            }
        }
    }
}

@Composable
private fun GoalProgressBar(
    totalExpense: Double,
    targetExpense: Double = 30000.0 // Целевое значение расходов в месяц
) {
    val progress = (totalExpense / targetExpense).coerceIn(0.0, 1.0)
    val percentage = (progress * 100).toInt()

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = when {
                percentage >= 90 -> "Attention! You are approaching the expense limit"
                percentage >= 75 -> "You have already spent $percentage% of the monthly limit"
                percentage >= 50 -> "Expenses within normal range, $percentage% of the limit"
                else -> "Excellent result! Spent only $percentage% of the limit"
            },
            style = MaterialTheme.typography.subtitle2,
            color = when {
                percentage >= 90 -> MaterialTheme.colors.error
                percentage >= 75 -> MaterialTheme.colors.error.copy(alpha = 0.7f)
                percentage >= 50 -> MaterialTheme.colors.primary
                else -> MaterialTheme.colors.primary
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.toFloat())
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        when {
                            percentage >= 90 -> MaterialTheme.colors.error
                            percentage >= 75 -> MaterialTheme.colors.error.copy(alpha = 0.7f)
                            percentage >= 50 -> MaterialTheme.colors.primary
                            else -> MaterialTheme.colors.primary
                        }
                    )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${totalExpense.toInt()} ₽",
                style = MaterialTheme.typography.caption
            )
            Text(
                text = "${targetExpense.toInt()} ₽",
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
private fun Modifier.tabIndicatorOffset(
    currentTabPosition: TabPosition
): Modifier = this.then(
    Modifier.fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(
            x = currentTabPosition.left,
            y = (-2).dp
        )
        .width(currentTabPosition.width)
)
