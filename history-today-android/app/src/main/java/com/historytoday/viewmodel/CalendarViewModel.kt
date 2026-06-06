package com.historytoday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.historytoday.domain.model.CalendarDay
import com.historytoday.domain.model.LunarInfo
import com.historytoday.domain.usecase.GetCalendarMonthUseCase
import com.historytoday.domain.usecase.GetLunarInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarMonth: GetCalendarMonthUseCase,
    private val getLunarInfo: GetLunarInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        loadCalendarMonth()
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update {
            it.copy(selectedDate = date)
        }
        loadLunarInfo(date)
    }

    fun onMonthChanged(year: Int, month: Int) {
        _uiState.update {
            it.copy(currentYear = year, currentMonth = month)
        }
        loadCalendarMonth()
    }

    fun goToToday() {
        val today = LocalDate.now()
        _uiState.update {
            it.copy(
                currentYear = today.year,
                currentMonth = today.monthValue,
                selectedDate = today
            )
        }
        loadCalendarMonth()
        loadLunarInfo(today)
    }

    fun loadCalendarMonth() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val days = getCalendarMonth(
                    _uiState.value.currentYear,
                    _uiState.value.currentMonth
                )
                val updatedDays = days.map { day ->
                    day.copy(isSelected = day.date == _uiState.value.selectedDate)
                }
                _uiState.update {
                    it.copy(calendarDays = updatedDays, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message)
                }
            }
        }
    }

    private fun loadLunarInfo(date: LocalDate) {
        viewModelScope.launch {
            val lunarInfo = getLunarInfo(date)
            _uiState.update {
                it.copy(currentLunarInfo = lunarInfo)
            }
        }
    }
}

data class CalendarUiState(
    val currentYear: Int = LocalDate.now().year,
    val currentMonth: Int = LocalDate.now().monthValue,
    val selectedDate: LocalDate = LocalDate.now(),
    val calendarDays: List<CalendarDay> = emptyList(),
    val currentLunarInfo: LunarInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
