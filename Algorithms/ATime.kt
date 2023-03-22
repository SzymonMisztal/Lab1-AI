package Algorithms

import BusStop
import Connection
import Functions
import TIME
import Time

class ATime (
    val graph: HashMap<String, BusStop>,
    val startStop: BusStop,
    val endStop: BusStop,
    val startingTime: Time
) {

    private fun costFunction(connection: Connection): Int {

        if(connection.startStop.currentBestConnectionIn!!.arrivalTime > connection.departureTime)
            return Int.MAX_VALUE / 2

        return connection.startStop.currentBestTimeIn.seconds +
                connection.arrivalTime.seconds - connection.startStop.currentBestConnectionIn!!.arrivalTime.seconds

    }

    private fun heuristics(busStop: BusStop): Int {
        return (Math.sqrt(Math.pow(busStop.x - endStop.x, 2.0) + Math.pow(busStop.y - endStop.y, 2.0)) * 500).toInt()
    }

    fun run(): List<Connection> {

        val busStops: HashSet<BusStop> = hashSetOf()
        startStop.currentBestTimeIn.seconds = 0;
        busStops.add(startStop)
        startStop.currentBestConnectionIn = Connection(startStop, startStop, "XD", startingTime, startingTime)
        val visitedBusStops: HashSet<BusStop> = hashSetOf()

        while (busStops.isNotEmpty() or busStops.contains(endStop)) {

            val currentBusStop = busStops.minBy { it.currentBestTimeIn.seconds.toLong() + heuristics(it) }
            visitedBusStops.add(currentBusStop)
            busStops.remove(currentBusStop)

            for (busStopNext in currentBusStop.connectionsFrom.keys) {

                if (visitedBusStops.contains(busStopNext))
                    continue

                val bestConnection = currentBusStop.connectionsFrom[busStopNext]!!.minBy { costFunction(it).toDouble() }

                if (costFunction(bestConnection) < busStopNext.currentBestTimeIn.seconds) {
                    busStopNext.currentBestTimeIn = Time(costFunction(bestConnection))
                    busStopNext.currentBestConnectionIn = bestConnection
                }
                if (!busStops.contains(busStopNext))
                    busStops.add(busStopNext)
            }
        }

        startStop.currentBestConnectionIn = null

        var path = mutableListOf<Connection>()
        if (endStop.currentBestConnectionIn == null) return listOf()
        else path.add(endStop.currentBestConnectionIn!!)

        while (true) {
            if (path.last().startStop.currentBestConnectionIn == null)
                break
            path.add(path.last().startStop.currentBestConnectionIn!!)
        }

        Functions.resetGraph(graph)
        return path.reversed()
    }
}