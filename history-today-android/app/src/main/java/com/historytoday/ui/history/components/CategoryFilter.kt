package com.historytoday.ui.history.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.historytoday.domain.model.EventCategory
import com.historytoday.domain.model.HistoryPeriod
import com.historytoday.domain.model.RegionType

@Composable
fun CategoryFilter(
    selectedCategory: EventCategory,
    selectedRegion: RegionType,
    selectedPeriod: HistoryPeriod,
    onCategorySelected: (EventCategory) -> Unit,
    onRegionSelected: (RegionType) -> Unit,
    onPeriodSelected: (HistoryPeriod) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            RegionType.values().forEach { region ->
                Button(
                    onClick = { onRegionSelected(region) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRegion == region) Color(0xFF1e3a5f) else Color(0xFFf0f0f0),
                        contentColor = if (selectedRegion == region) Color.White else Color(0xFF666666)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = region.getDisplayName(),
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (selectedRegion == RegionType.DOMESTIC) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                HistoryPeriod.values().forEach { period ->
                    Button(
                        onClick = { onPeriodSelected(period) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedPeriod == period) Color(0xFF9b59b6) else Color(0xFFf0f0f0),
                            contentColor = if (selectedPeriod == period) Color.White else Color(0xFF666666)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = period.getDisplayName(),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            EventCategory.values().forEach { category ->
                Button(
                    onClick = { onCategorySelected(category) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategory == category) Color(0xFF3498db) else Color(0xFFf0f0f0),
                        contentColor = if (selectedCategory == category) Color.White else Color(0xFF666666)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = category.getDisplayName(),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
