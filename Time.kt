class Time(
    var seconds: Int
) {

    constructor(
        hour: Int,
        minutes: Int,
        seconds: Int
    ) : this(hour * 3600 + minutes * 60 + seconds)

    fun reseted(): Int {
        return seconds % 86400
    }

    override fun toString(): String {
        val hours = seconds / 3600
        val minutes = seconds % 3600 / 60
        val seconds = seconds % 60

        return "$hours:$minutes:$seconds"
    }

    operator fun compareTo(other: Time): Int {
        return when {
            this.seconds > other.seconds -> 1
            this.seconds < other.seconds -> -1
            else -> 0
        }
    }

    operator fun minus(other: Time): Time {
        return Time(this.seconds - other.seconds)
    }
}