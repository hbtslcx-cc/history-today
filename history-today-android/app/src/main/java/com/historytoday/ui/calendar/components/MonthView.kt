package com.historytoday.ui.calendar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.historytoday.domain.model.CalendarDay

@Composable
fun MonthView(
    days: List<CalendarDay>,
    currentMonth: Int,
    onDayClick: (CalendarDay) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            for (weekIndex in days.indices step 7) {
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (dayIndex in weekIndex until minOf(weekIndex + 7, days.size)) {
                        CalendarDayCell(
                            day = days[dayIndex],
                            currentMonth = currentMonth,
                            onDayClick = onDayClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
