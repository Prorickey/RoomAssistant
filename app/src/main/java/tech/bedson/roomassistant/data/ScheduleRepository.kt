package tech.bedson.roomassistant.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tech.bedson.roomassistant.data.model.DaySchedule
import tech.bedson.roomassistant.data.model.MealTimes
import tech.bedson.roomassistant.data.model.ScheduleEvent
import java.io.InputStreamReader
import java.util.Calendar
import java.util.Locale

class ScheduleRepository(private val context: Context) {

    private var scheduleMap: Map<String, List<ScheduleEvent>>? = null

    private fun loadSchedule(): Map<String, List<ScheduleEvent>> {
        if (scheduleMap != null) return scheduleMap!!

        val inputStream = context.assets.open("schedule_normal.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<Map<String, List<ScheduleEvent>>>() {}.type
        scheduleMap = Gson().fromJson(reader, type)
        reader.close()
        return scheduleMap!!
    }

    fun getTodaySchedule(): DaySchedule {
        val schedule = loadSchedule()
        val dayName = getDayName()
        val events = schedule[dayName] ?: emptyList()
        return DaySchedule(events)
    }

    fun getCurrentEvent(): ScheduleEvent? {
        val schedule = getTodaySchedule()
        val now = getCurrentTimeMinutes()

        for (i in schedule.events.indices.reversed()) {
            val eventMinutes = parseTimeToMinutes(schedule.events[i].time)
            if (now >= eventMinutes) {
                return schedule.events[i]
            }
        }
        return null
    }

    fun getNextEvent(): ScheduleEvent? {
        val schedule = getTodaySchedule()
        val now = getCurrentTimeMinutes()

        for (event in schedule.events) {
            val eventMinutes = parseTimeToMinutes(event.time)
            if (eventMinutes > now) {
                return event
            }
        }
        return null
    }

    fun getMinutesUntilNextEvent(): Int {
        val nextEvent = getNextEvent() ?: return 0
        val now = getCurrentTimeMinutes()
        val eventMinutes = parseTimeToMinutes(nextEvent.time)
        return eventMinutes - now
    }

    fun getMealTimes(): MealTimes {
        val schedule = getTodaySchedule()
        val breakfast = schedule.events.find {
            it.event.equals("Breakfast", ignoreCase = true) ||
            it.event.equals("Brunch", ignoreCase = true)
        }?.time
        val lunch = schedule.events.find {
            it.event.equals("Lunch", ignoreCase = true)
        }?.time
        val dinner = schedule.events.find {
            it.event.equals("Dinner", ignoreCase = true)
        }?.time
        return MealTimes(breakfast, lunch, dinner)
    }

    private fun getDayName(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "monday"
            Calendar.TUESDAY -> "tuesday"
            Calendar.WEDNESDAY -> "wednesday"
            Calendar.THURSDAY -> "thursday"
            Calendar.FRIDAY -> "friday"
            Calendar.SATURDAY -> "saturday"
            Calendar.SUNDAY -> "sunday"
            else -> "monday"
        }
    }

    private fun getCurrentTimeMinutes(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    }

    companion object {
        fun parseTimeToMinutes(time: String): Int {
            val parts = time.split(":")
            return parts[0].toInt() * 60 + parts[1].toInt()
        }
    }
}
