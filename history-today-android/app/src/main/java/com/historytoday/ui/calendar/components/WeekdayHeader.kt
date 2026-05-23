package com.historytoday.ui.calendar.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.historytoday.ui.theme.Secondary

@Composable
fun WeekdayHeader() {
    val weekdays = listOf("日", "一", "二", "三", "四", "五", "六")
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        weekdays.forEach { weekday ->
            Text(
                text = weekday,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = if (weekday == "日" || weekday == "六") Secondary else TextPrimary
            )
        }
    }
}

val TextPrimary = androidx.compose.ui.graphics.Color(0xFF333333)
