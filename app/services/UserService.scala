package service

import model.{Station, Stations}
import scala.concurrent.Future

object StationService {

  def addStation(station: Station): Future[String] = {
    Stations.add(station)
  }

  def deleteStation(id: Int): Future[Int] = {
    Stations.delete(id)
  }

  def getStation(id: Int): Future[Option[Station]] = {
    Stations.get(id)
  }

  def listAllStations: Future[Seq[Station]] = {
    Stations.listAll
  }
}
