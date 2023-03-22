package Algorithms

import BusStop
import Connection
import Functions
import TIME
import Time

class ATransfer (
    val graph: HashMap<String, BusStop>,
    val startStop: BusStop,
    val endStop: BusStop,
    val startingTime: Time
) {

    private fun costFunction(connection: Connection, time: Time, origin: BusStop): Int {
        if (time.seconds > connection.departureTime.seconds - startingTime.seconds)
            return Int.MAX_VALUE
        return (connection.arrivalTime - startingTime).seconds + if (origin.currentBestConnectionIn?.line.equals(connection.line)) 0 else 3600
    }

    private fun heuristics(busStop: BusStop, endStop: BusStop): Int {
        return (Math.sqrt(Math.pow(busStop.x - endStop.x, 2.0) + Math.pow(busStop.y - endStop.y, 2.0)) * 500).toInt()
    }

    fun run(): List<Connection> {

        val busStops: HashSet<BusStop> = hashSetOf()
        startStop.currentBestTimeIn.seconds = 0;
        busStops.add(startStop)
        val visitedBusStops: HashSet<BusStop> = hashSetOf()

        while (busStops.isNotEmpty() or busStops.contains(endStop)) {

            val currentBusStop = busStops.minBy { it.currentBestTimeIn.seconds.toLong() + heuristics(it, endStop) }
            visitedBusStops.add(currentBusStop)
            busStops.remove(currentBusStop)

            for (busStopNext in currentBusStop.connectionsFrom.keys) {

                if (visitedBusStops.contains(busStopNext))
                    continue

                val bestConnection = currentBusStop.connectionsFrom[busStopNext]!!.minBy { costFunction(it, currentBusStop.currentBestTimeIn, currentBusStop).toDouble() }

                if (costFunction(bestConnection, currentBusStop.currentBestTimeIn, currentBusStop) < busStopNext.currentBestTimeIn.seconds) {
                    busStopNext.currentBestTimeIn = Time(costFunction(bestConnection, currentBusStop.currentBestTimeIn, currentBusStop))
                    busStopNext.currentBestConnectionIn = bestConnection
                }
                if (!busStops.contains(busStopNext))
                    busStops.add(busStopNext)
            }
        }

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