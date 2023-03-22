import Algorithms.*
import kotlin.system.measureTimeMillis

const val START = "Tramwajowa"
const val END = "Dolmed"
val TIME = Time(15, 43, 33)
val MILESTONES = arrayListOf("Tramwajowa", "Dolmed", "PL. GRUNWALDZKI", "pl. Orląt Lwowskich", "Pomorska")

fun main(args: Array<String>) {

    val records = Functions.readCSV("/home/Johanes/Downloads/csv.csv") ?: listOf()

    val busStops: HashMap<String, BusStop> = hashMapOf()

    for (record in records) {

        var startBusStop = BusStop(
            record.startBusStop,
            record.startX,
            record.startY
        )
        var endBusStop = BusStop(
            record.endBusStop,
            record.endX,
            record.endY
        )

        if (!busStops.containsKey(startBusStop.name))
            busStops[startBusStop.name] = startBusStop
        else
            startBusStop = busStops[startBusStop.name]!!
        if (!busStops.containsKey(endBusStop.name))
            busStops[endBusStop.name] = endBusStop
        else
            endBusStop = busStops[endBusStop.name]!!

        val connection = Connection(
            startBusStop,
            endBusStop,
            record.line,
            record.departureTime,
            record.arrivalTime
        )
        if (busStops[startBusStop.name]!!.connectionsFrom.containsKey(endBusStop))
            busStops[startBusStop.name]!!.connectionsFrom.get(endBusStop)!!.add(connection)
        else
            busStops[startBusStop.name]!!.connectionsFrom.put(endBusStop, mutableListOf(connection))
    }

    val milestones: ArrayList<BusStop> = arrayListOf()
    for (milestone in MILESTONES)
        milestones.add(busStops[milestone]!!)

    var pathDijkstraTime = listOf<Connection>()
    var pathATime = listOf<Connection>()
    var pathATransfer = listOf<Connection>()
    var pathTabuSearch = listOf<Connection>()

    val executionTimeDijkstraTime = measureTimeMillis { pathDijkstraTime = DijkstraTime(busStops, busStops[START]!!, busStops[END]!!, TIME).run() }
    val executionTimeATime = measureTimeMillis { pathATime = ATime(busStops, busStops[START]!!, busStops[END]!!, TIME).run() }
    val executionTimeATransfer = measureTimeMillis { pathATransfer = ATransfer(busStops, busStops[START]!!, busStops[END]!!, TIME).run() }
    val executionTimeTabuSearch = measureTimeMillis { pathTabuSearch = TabuSearch(busStops, milestones).run(30) }


    Functions.showPath(pathDijkstraTime)
    Functions.showPath(pathATime)
    Functions.showPath(pathATransfer)
    Functions.showPath(pathTabuSearch)

    println("Dijkstra (time) execution time: $executionTimeDijkstraTime ms")
    println("A* (time) execution time: $executionTimeATime ms")
    println("A* (transfer) execution time: $executionTimeATransfer ms")
    println("Tabu Search execution time: $executionTimeTabuSearch ms")
}
