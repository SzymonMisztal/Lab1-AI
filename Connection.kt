import java.util.*

class Connection(
    val startStop: BusStop,
    val endStop: BusStop,
    val line: String,
    val departureTime: Time,
    val arrivalTime: Time
) {

    override fun equals(other: Any?): Boolean {
        if (other is Connection)
            if (other.startStop.equals(this.startStop) and
                other.endStop.equals(this.endStop) and
                other.line.equals(this.line) and
                other.departureTime.equals(this.departureTime) and
                other.arrivalTime.equals(this.arrivalTime))
                return true
        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(startStop, endStop, line, departureTime, arrivalTime)
    }

    override fun toString(): String {
        return startStop.name + " " +
                departureTime + " | " +
                arrivalTime + " " +
                endStop.name
    }
}