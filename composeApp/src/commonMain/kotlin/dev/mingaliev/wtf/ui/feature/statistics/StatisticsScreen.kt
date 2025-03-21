package dev.mingaliev.wtf.ui.feature.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    paddingValues: PaddingValues
) {
    val uiState = viewModel.uiState.collectAsState()
    val errorColor = MaterialTheme.colors.error
    val gridLineColor = Color.Gray.copy(alpha = 0.2f)

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
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.h5
                )

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
                                    text = "Total income",
                                    style = MaterialTheme.typography.subtitle2
                                )
                                Text(
                                    text = "${uiState.value.totalIncome.toInt()} ₽",
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.primary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Total expenses",
                                    style = MaterialTheme.typography.subtitle2
                                )
                                Text(
                                    text = "-${uiState.value.totalExpense.toInt()} ₽",
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.error
                                )
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Expenses for the week",
                            style = MaterialTheme.typography.subtitle2
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        ) {
                            val maxAmount = uiState.value.weeklyTransactions
                                .map { it.amount.absoluteValue }
                                .maxOrNull() ?: 0.0

                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val canvasWidth = size.width
                                val canvasHeight = size.height
                                val columnWidth = canvasWidth / 7
                                val columnPadding = columnWidth * 0.1f

                                for (i in 0..5) {
                                    val y = canvasHeight - (canvasHeight * (i / 5f))
                                    drawLine(
                                        color = gridLineColor,
                                        start = Offset(0f, y),
                                        end = Offset(canvasWidth, y),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                }

                                uiState.value.weeklyTransactions.groupBy {
                                    it.timestamp.date
                                }.toList().takeLast(7).forEachIndexed { index, (_, transactions) ->
                                    val expense = transactions
                                        .filter { it.amount < 0 }
                                        .sumOf { it.amount.absoluteValue }

                                    val columnHeight =
                                        (canvasHeight * (expense / maxAmount)).toFloat()
                                    val x = index * columnWidth

                                    drawRect(
                                        color = errorColor,
                                        topLeft = Offset(
                                            x + columnPadding,
                                            canvasHeight - columnHeight
                                        ),
                                        size = Size(
                                            columnWidth - (columnPadding * 2),
                                            columnHeight
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        items(uiState.value.weeklyTransactions) { transaction ->
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
