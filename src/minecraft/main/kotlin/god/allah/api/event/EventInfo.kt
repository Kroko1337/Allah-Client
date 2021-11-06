package god.allah.api.event

annotation class EventInfo(val priority: EventPriority = EventPriority.NORMAL)

enum class EventPriority {
    LOW, NORMAL, HIGH
}