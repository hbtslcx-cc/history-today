package com.historytoday.ui.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.historytoday.domain.model.CalendarDay

@Composable
fun CalendarDayCell(
    day: CalendarDay,
    currentMonth: Int,
    onDayClick: (CalendarDay) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCurrentMonth = day.date.monthValue == currentMonth

    Box(
        modifier = Modifier
            .size(48.dp, 56.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onDayClick(day) },
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = when {
                    day.isToday -> Color.White
                    day.isSelected -> Color(0xFF1e3a5f)
                    !isCurrentMonth -> Color(0xFF999999)
                    else -> Color(0xFF333333)
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            day.isToday -> Color(0xFFe74c3c)
                            day.isSelected -> Color(0xFFe8f0fe)
                            else -> Color.Transparent
                        }
                    )
                    .align(Alignment.CenterHorizontally),
                lineHeight = 32.sp
            )

            if (day.lunarInfo.solarTerm != null) {
                Text(
                    text = day.lunarInfo.solarTerm,
                    fontSize = 9.sp,
                    color = Color(0xFF27ae60),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            } else if (day.lunarInfo.festival != null) {
                Text(
                    text = day.lunarInfo.festival,
                    fontSize = 9.sp,
                    color = Color(0xFFe74c3c),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            } else {
                Text(
                    text = day.lunarInfo.lunarDate.substringAfter("月"),
                    fontSize = 10.sp,
                    color = Color(0xFF8b4513),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }

        if (day.eventCount > 0) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFe74c3c))
                    .align(Alignment.BottomCenter)
            )
        }
    }
}
