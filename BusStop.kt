class BusStop(
    public val name: String,
    public val x: Double,
    public val y: Double
) {

    public val connectionsFrom: HashMap<BusStop, MutableList<Connection>> = hashMapOf()
    public var currentBestTimeIn: Time = Time(Int.MAX_VALUE)
    public var currentBestConnectionIn: Connection? = null

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (other is BusStop)
            if (other.name.equals(this.name))
                return true
        return false
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}