package tech.bedson.roomassistant.data.model

data class ScheduleEvent(
    val time: String,
    val event: String
)

data class DaySchedule(
    val events: List<ScheduleEvent>
)

data class MealTimes(
    val breakfast: String?,
    val lunch: String?,
    val dinner: String?
)
