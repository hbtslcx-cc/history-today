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

@Composable
fun YiJiCard(lunarInfo: LunarInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "宜",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF27ae60),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = lunarInfo.yi.joinToString("  "),
                fontSize = 14.sp,
                color = Color(0xFF27ae60)
            )

            Text(
                text = "忌",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFe74c3c),
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            Text(
                text = lunarInfo.ji.joinToString("  "),
                fontSize = 14.sp,
                color = Color(0xFFe74c3c)
            )
        }
    }
}
