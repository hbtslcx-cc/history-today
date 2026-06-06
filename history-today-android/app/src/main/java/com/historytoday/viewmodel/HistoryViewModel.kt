package com.historytoday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.historytoday.domain.model.EventCategory
import com.historytoday.domain.model.HistoryEvent
import com.historytoday.domain.model.HistoryPeriod
import com.historytoday.domain.model.RegionType
import com.historytoday.domain.usecase.GetEventsByDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getEventsByDate: GetEventsByDateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    fun loadEvents(
        date: LocalDate,
        category: EventCategory = EventCategory.ALL,
        region: RegionType = RegionType.ALL,
        period: HistoryPeriod = HistoryPeriod.ALL
    ) {
        val dateStr = String.format("%02d-%02d", date.monthValue, date.dayOfMonth)
        _uiState.update {
            it.copy(
                currentDate = date,
                selectedCategory = category,
                selectedRegion = region,
                selectedPeriod = period,
                isLoading = true
            )
        }
        viewModelScope.launch {
            try {
                val events = getEventsByDate(dateStr, category, region, period)
                _uiState.update {
                    it.copy(
                        events = events,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun onCategorySelected(category: EventCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadEvents(
            _uiState.value.currentDate,
            category,
            _uiState.value.selectedRegion,
            _uiState.value.selectedPeriod
        )
    }

    fun onRegionSelected(region: RegionType) {
        _uiState.update {
            it.copy(
                selectedRegion = region,
                selectedPeriod = if (region != RegionType.DOMESTIC) HistoryPeriod.ALL else it.selectedPeriod
            )
        }
        loadEvents(
            _uiState.value.currentDate,
            _uiState.value.selectedCategory,
            region,
            if (region != RegionType.DOMESTIC) HistoryPeriod.ALL else _uiState.value.selectedPeriod
        )
    }

    fun onPeriodSelected(period: HistoryPeriod) {
        _uiState.update { it.copy(selectedPeriod = period) }
        loadEvents(
            _uiState.value.currentDate,
            _uiState.value.selectedCategory,
            _uiState.value.selectedRegion,
            period
        )
    }

    fun onDateChanged(date: LocalDate) {
        loadEvents(
            date,
            _uiState.value.selectedCategory,
            _uiState.value.selectedRegion,
            _uiState.value.selectedPeriod
        )
    }
}

data class HistoryUiState(
    val currentDate: LocalDate = LocalDate.now(),
    val selectedCategory: EventCategory = EventCategory.ALL,
    val selectedRegion: RegionType = RegionType.ALL,
    val selectedPeriod: HistoryPeriod = HistoryPeriod.ALL,
    val events: List<HistoryEvent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
