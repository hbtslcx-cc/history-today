package com.historytoday.data.mapper

import com.historytoday.data.local.EventEntity
import com.historytoday.domain.model.EventCategory
import com.historytoday.domain.model.HistoryEvent
import com.historytoday.domain.model.HistoryPeriod
import com.historytoday.domain.model.RegionType

fun EventEntity.toDomain(): HistoryEvent {
    return HistoryEvent(
        id = id,
        title = title,
        date = date,
        year = year,
        category = runCatching { EventCategory.valueOf(category) }.getOrDefault(EventCategory.CULTURE),
        region = runCatching { RegionType.valueOf(region) }.getOrDefault(RegionType.INTERNATIONAL),
        period = runCatching { HistoryPeriod.valueOf(period) }.getOrDefault(HistoryPeriod.ALL),
        importance = runCatching { HistoryEvent.ImportanceLevel.valueOf(importance) }
            .getOrDefault(HistoryEvent.ImportanceLevel.C),
        description = description,
        shortDesc = shortDesc,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun HistoryEvent.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        title = title,
        date = date,
        year = year,
        category = category.name,
        region = region.name,
        period = period.name,
        importance = importance.name,
        description = description,
        shortDesc = shortDesc,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
