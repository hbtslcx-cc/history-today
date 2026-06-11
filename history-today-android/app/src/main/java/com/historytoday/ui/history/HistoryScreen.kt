package com.historytoday.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.historytoday.domain.model.EventCategory
import com.historytoday.domain.model.RegionType
import com.historytoday.ui.history.components.CategoryFilter
import com.historytoday.ui.history.components.EventCard
import com.historytoday.viewmodel.HistoryViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HistoryScreen {

    @Composable
    fun Content(
        navController: NavController,
        year: Int,
        month: Int,
        day: Int,
        viewModel: HistoryViewModel = hiltViewModel()
    ) {
        val uiState = viewModel.uiState.value
        val date = LocalDate.of(year, month, day)

        LaunchedEffect(date) {
            viewModel.loadEvents(date)
        }

        Scaffold(
            topBar = {
                TopBar(
                    date = date,
                    onBackClick = { navController.popBackStack() },
                    onPrevDay = {
                        val prevDay = date.minusDays(1)
                        navController.navigate("history/${prevDay.year}/${prevDay.monthValue}/${prevDay.dayOfMonth}")
                    },
                    onNextDay = {
                        val nextDay = date.plusDays(1)
                        navController.navigate("history/${nextDay.year}/${nextDay.monthValue}/${nextDay.dayOfMonth}")
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFf8f9fa))
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.events.isEmpty()) {
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        Text(
                            text = "暂无历史事件",
                            fontSize = 18.sp,
                            color = Color(0xFF999999)
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            CategoryFilter(
                                selectedCategory = uiState.selectedCategory,
                                selectedRegion = uiState.selectedRegion,
                                selectedPeriod = uiState.selectedPeriod,
                                onCategorySelected = { viewModel.onCategorySelected(it) },
                                onRegionSelected = { viewModel.onRegionSelected(it) },
                                onPeriodSelected = { viewModel.onPeriodSelected(it) }
                            )
                        }

                        items(uiState.events) { event ->
                            EventCard(event = event) {
                                navController.navigate("detail/${event.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    date: LocalDate,
    onBackClick: () -> Unit,
    onPrevDay: () -> Unit,
    onNextDay: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF1e3a5f))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回", tint = Color.White)
        }

        IconButton(onClick = onPrevDay) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "前一天", tint = Color.White)
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${date.monthValue}月${date.dayOfMonth}日 ${getWeekday(date)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = getLunarDate(date),
                fontSize = 14.sp,
                color = Color(0xFFb8c5d6)
            )
        }

        IconButton(onClick = onNextDay) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "后一天", tint = Color.White)
        }

        IconButton(onClick = {}) {
            Icon(Icons.Default.Share, contentDescription = "分享", tint = Color.White)
        }
    }
}

private fun getWeekday(date: LocalDate): String {
    return when (date.dayOfWeek.value) {
        1 -> "星期一"
        2 -> "星期二"
        3 -> "星期三"
        4 -> "星期四"
        5 -> "星期五"
        6 -> "星期六"
        7 -> "星期日"
        else -> ""
    }
}

private fun getLunarDate(date: LocalDate): String {
    return "农历四月初六"
}
