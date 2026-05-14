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
        category = EventCategory.valueOf(category),
        region = RegionType.valueOf(region),
        period = HistoryPeriod.valueOf(period),
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
        description = description,
        shortDesc = shortDesc,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
