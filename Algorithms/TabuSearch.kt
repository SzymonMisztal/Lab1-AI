package Algorithms

import BusStop
import Connection
import TIME

class TabuSearch(
    val graph: HashMap<String, BusStop>,
    val milestones: ArrayList<BusStop>
) {
    //           [milestones]
    val tabuSet: HashSet<ArrayList<BusStop>> = hashSetOf()
    //            [[milestones], [path]]
    var bestMilestones: ArrayList<BusStop> = ArrayList(milestones)
    var bestPath: ArrayList<Connection> = arrayListOf()
    var bestTime = Int.MAX_VALUE


    fun run(repetitions: Int): ArrayList<Connection> {

        for (i in 1..repetitions)
            iteration(bestMilestones)

        return bestPath
    }

    fun iteration(milestones: ArrayList<BusStop>) {

        val neighbours: MutableList<ArrayList<BusStop>> = mutableListOf()

        for (i in 1..milestones.size - 2) {

            val newNeighbour: ArrayList<BusStop> = ArrayList(milestones)
            swapMilestones(newNeighbour, i, i + 1)
            neighbours.add(newNeighbour)
        }

        for (neighbour in neighbours)
            if (!tabuSet.contains(neighbour))
                bestPath(neighbour)
    }

    private fun bestPath(milestones: ArrayList<BusStop>) {

        var currentTime = TIME
        val currentPath: ArrayList<Connection> = arrayListOf()

        for (i in 0..milestones.size - 1) {

            val path = ATime(graph, milestones[i], milestones[(i + 1)%milestones.size], currentTime).run()
            currentTime = path.last().arrivalTime
            for (p in path)
                currentPath.add(p)
        }

        if ((currentTime - TIME).seconds < bestTime) {
            bestTime = (currentTime - TIME).seconds
            bestMilestones = ArrayList(milestones)
            bestPath = currentPath
        }
        tabuSet.add(bestMilestones)
    }



    private fun swapMilestones(milestones: ArrayList<BusStop>, index1: Int, index2: Int) {
        val temp = milestones[index1]
        milestones[index1] = milestones[index2]
        milestones[index2] = temp
    }
}