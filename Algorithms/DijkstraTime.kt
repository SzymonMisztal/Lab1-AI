package Algorithms

import BusStop
import Connection
import Functions
import Time

class DijkstraTime(
    val graph: HashMap<String, BusStop>,
    val startStop: BusStop,
    val endStop: BusStop,
    val startingTime: Time
) {

    private fun costFunction(connection: Connection, time: Time): Int {
        if (time.seconds > connection.departureTime.seconds - startingTime.seconds)
            return Int.MAX_VALUE
        return (connection.arrivalTime - startingTime).seconds
    }

    fun run(): List<Connection> {

        val busStops: HashSet<BusStop> = hashSetOf()
        startStop.currentBestTimeIn.seconds = 0;
        busStops.add(startStop)
        val visitedBusStops: HashSet<BusStop> = hashSetOf()

        while (busStops.isNotEmpty() or busStops.contains(endStop)) {

            val currentBusStop = busStops.minBy { it.currentBestTimeIn.seconds }
            visitedBusStops.add(currentBusStop)
            busStops.remove(currentBusStop)

            for (busStopNext in currentBusStop.connectionsFrom.keys) {

                val bestConnection = currentBusStop.connectionsFrom[busStopNext]!!.minBy { costFunction(it, currentBusStop.currentBestTimeIn) }

                if (visitedBusStops.contains(busStopNext))
                    continue

                if (costFunction(bestConnection, currentBusStop.currentBestTimeIn) < busStopNext.currentBestTimeIn.seconds) {
                    busStopNext.currentBestTimeIn = Time(costFunction(bestConnection, currentBusStop.currentBestTimeIn))
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


