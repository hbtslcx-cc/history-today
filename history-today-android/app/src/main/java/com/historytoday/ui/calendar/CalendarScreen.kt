package com.historytoday.ui.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.historytoday.domain.model.CalendarDay
import com.historytoday.ui.calendar.components.CalendarDayCell
import com.historytoday.ui.calendar.components.HistoryEntryCard
import com.historytoday.ui.calendar.components.LunarInfoCard
import com.historytoday.ui.calendar.components.MonthView
import com.historytoday.ui.calendar.components.WeekdayHeader
import com.historytoday.ui.calendar.components.YiJiCard
import com.historytoday.viewmodel.CalendarViewModel
import dagger.hilt.android.lifecycle.ViewModelScoped
import java.time.LocalDate
import javax.inject.Inject

class CalendarScreen @Inject constructor() {

    @Composable
    fun Content(
        navController: NavController,
        viewModel: CalendarViewModel = viewModel()
    ) {
        val uiState = viewModel.uiState.value

        LaunchedEffect(Unit) {
            viewModel.loadCalendarMonth()
        }

        Scaffold(
            topBar = {
                TopBar(
                    year = uiState.currentYear,
                    month = uiState.currentMonth,
                    onPrevMonth = {
                        if (uiState.currentMonth == 1) {
                            viewModel.onMonthChanged(uiState.currentYear - 1, 12)
                        } else {
                            viewModel.onMonthChanged(uiState.currentYear, uiState.currentMonth - 1)
                        }
                    },
                    onNextMonth = {
                        if (uiState.currentMonth == 12) {
                            viewModel.onMonthChanged(uiState.currentYear + 1, 1)
                        } else {
                            viewModel.onMonthChanged(uiState.currentYear, uiState.currentMonth + 1)
                        }
                    },
                    onTodayClick = { viewModel.goToToday() },
                    onSettingsClick = {}
                )
            },
            bottomBar = {
                BottomBar(onTodayClick = { viewModel.goToToday() })
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFf8f9fa))
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        WeekdayHeader()

                        MonthView(
                            days = uiState.calendarDays,
                            currentMonth = uiState.currentMonth,
                            onDayClick = { day ->
                                viewModel.onDateSelected(day.date)
                            }
                        )

                        uiState.currentLunarInfo?.let { lunarInfo ->
                            LunarInfoCard(date = uiState.selectedDate, lunarInfo = lunarInfo)
                            YiJiCard(lunarInfo = lunarInfo)
                        }

                        val selectedDay = uiState.calendarDays.find { it.date == uiState.selectedDate }
                        HistoryEntryCard(
                            eventCount = selectedDay?.eventCount ?: 0,
                            onClick = {
                                navController.navigate("history/${uiState.selectedDate.year}/${uiState.selectedDate.monthValue}/${uiState.selectedDate.dayOfMonth}")
                            }
                        )

                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    year: Int,
    month: Int,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onTodayClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF1e3a5f))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevMonth) {
            Icon(Icons.Default.ArrowLeft, contentDescription = "上一月", tint = Color.White)
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${year}年${month}月",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Button(
            onClick = onTodayClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4a90d9)),
            modifier = Modifier.height(36.dp)
        ) {
            Text(text = "今日", fontSize = 14.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onSettingsClick) {
            Icon(Icons.Default.Settings, contentDescription = "设置", tint = Color.White)
        }
    }
}

@Composable
fun BottomBar(onTodayClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onTodayClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4a90d9)),
            modifier = Modifier.size(200.dp, 44.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp)
        ) {
            Image(
                imageVector = androidx.compose.material.icons.Icons.Default.Home,
                contentDescription = "返回今天",
                modifier = Modifier.size(20.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "返回今天", fontSize = 16.sp, color = Color.White)
        }
    }
}
