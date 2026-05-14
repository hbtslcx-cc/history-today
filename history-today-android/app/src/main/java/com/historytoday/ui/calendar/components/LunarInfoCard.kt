package com.historytoday.ui.calendar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.historytoday.domain.model.LunarInfo
import java.time.LocalDate

@Composable
fun LunarInfoCard(
    date: LocalDate,
    lunarInfo: LunarInfo
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${date.monthValue}月${date.dayOfMonth}日 ${getWeekday(date)}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            
            Text(
                text = "${lunarInfo.lunarYear} · ${lunarInfo.zodiac}年",
                fontSize = 16.sp,
                color = Color(0xFF8b4513),
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Text(
                text = lunarInfo.lunarDate,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF8b4513),
                modifier = Modifier.padding(top = 2.dp)
            )

            if (!lunarInfo.solarTerm.isNullOrEmpty() || !lunarInfo.festival.isNullOrEmpty()) {
                Text(
                    text = "${lunarInfo.solarTerm ?: ""}${if (!lunarInfo.solarTerm.isNullOrEmpty() && !lunarInfo.festival.isNullOrEmpty()) " · " else ""}${lunarInfo.festival ?: ""}",
                    fontSize = 14.sp,
                    color = Color(0xFF4a90d9),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
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
