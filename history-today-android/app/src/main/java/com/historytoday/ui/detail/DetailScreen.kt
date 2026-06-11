package com.historytoday.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.historytoday.viewmodel.DetailViewModel

class DetailScreen {

    @Composable
    fun Content(
        navController: NavController,
        eventId: String,
        viewModel: DetailViewModel = hiltViewModel()
    ) {
        val uiState = viewModel.uiState.value

        LaunchedEffect(eventId) {
            viewModel.loadEvent(eventId)
        }

        Scaffold(
            topBar = {
                TopBar(
                    category = uiState.event?.category?.getDisplayName() ?: "",
                    onBackClick = { navController.popBackStack() }
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
                } else if (uiState.event == null) {
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        Text(
                            text = "事件不存在",
                            fontSize = 18.sp,
                            color = Color(0xFF999999)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(androidx.compose.foundation.rememberScrollState())
                    ) {
                        EventHeader(event = uiState.event)
                        EventDescription(description = uiState.event.description)
                        RelatedEvents()
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    category: String,
    onBackClick: () -> Unit
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

        Surface(
            color = Color.White.copy(alpha = 0.2f),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
        ) {
            Text(
                text = category,
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {}) {
            Icon(Icons.Default.Share, contentDescription = "分享", tint = Color.White)
        }

        IconButton(onClick = {}) {
            Icon(Icons.Default.Favorite, contentDescription = "收藏", tint = Color.White)
        }
    }
}

@Composable
fun EventHeader(event: com.historytoday.domain.model.HistoryEvent) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = event.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Text(
            text = "${event.year}年${event.date.substringBefore("-")}月${event.date.substringAfter("-")}日",
            fontSize = 16.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(top = 8.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFe0e0e0))
    ) {
        Text(
            text = "图片区域",
            fontSize = 14.sp,
            color = Color(0xFF999999),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun EventDescription(description: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = description,
            fontSize = 16.sp,
            color = Color(0xFF333333),
            lineHeight = 24.sp
        )
    }
}

@Composable
fun RelatedEvents() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "📅 同日期其他事件",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))

        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "1919年 - 五四运动爆发",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(12.dp)
            )
        }

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))

        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "1901年 - 清政府签订《辛丑条约》",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}
