import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object Functions {
    fun readCSV(fileName: String?): List<Record>? {
        val records: MutableList<Record> = LinkedList()
        val pathToFile = Paths.get(fileName)
        try {
            Files.newBufferedReader(pathToFile).use { br ->
                br.readLine()
                var line = br.readLine()
                while (line != null) {
                    val attributes =
                        line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val time1 =
                        attributes[3].split(":".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    val time2 =
                        attributes[4].split(":".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    val record = Record(
                        attributes[0].toInt(),
                        attributes[1],
                        attributes[2],
                        Time(time1[0].toInt(), time1[1].toInt(), time1[2].toInt()),
                        Time(time2[0].toInt(), time2[1].toInt(), time2[2].toInt()),
                        attributes[5],
                        attributes[6],
                        attributes[7].toDouble(),
                        attributes[8].toDouble(),
                        attributes[9].toDouble(),
                        attributes[10].toDouble()
                    )
                    records.add(record)
                    //System.out.println(record.toString());
                    line = br.readLine()
                }
            }
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
        return records
    }

    fun showPath(path: List<Connection>) {

        val alignFormat = "|%-17s|%-7s|%-7s|%-17s| %4s |%n"

        System.out.format("+-------------------------+-------------------------+------+%n")
        System.out.format("|        Departure        |         Arrival         |      |%n")
        System.out.format("+-----------------+-------+-------+-----------------+ Line |%n")
        System.out.format("|    Bus stop     | Time  | Time  |    Bus stop     |      |%n")
        System.out.format("+-----------------+-------+-------+-----------------+------+%n")

        for (connection in path)
            System.out.format(
                alignFormat,
                connection.startStop.toString().substring(0, Math.min(connection.startStop.toString().length, 17)),
                connection.departureTime,
                connection.arrivalTime,
                connection.endStop.toString().substring(0, Math.min(connection.endStop.toString().length, 17)),
                connection.line)

        System.out.format("+----------------+--------+--------+----------------+------+%n")
    }

    fun resetGraph(graph: HashMap<String, BusStop>) {
        for (busStop in graph.values) {
            busStop.currentBestTimeIn = Time(Int.MAX_VALUE)
            busStop.currentBestConnectionIn = null
        }
    }
}