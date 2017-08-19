package model

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

case class Station(id: Int, name: String)

case class StationFormData(week: Int, time: String, direction: Int)

object StationForm {
  val form = Form(
    mapping(
      "week" -> number,
      "time" -> nonEmptyText,
      "direction" -> number
    )(StationFormData.apply)(StationFormData.unapply)
  )
}

class StationTableDef(tag: Tag) extends Table[Station](tag, "station") {

  def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
  def name = column[String]("name")

  override def * =
    (id,name) <>(Station.tupled, Station.unapply)
}

object Stations {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val stations = TableQuery[StationTableDef]

  def add(station: Station): Future[String] = {
    dbConfig.db.run(stations += station).map(res => "Station successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Int): Future[Int] = {
    dbConfig.db.run(stations.filter(_.id === id).delete)
  }

  def get(id: Int): Future[Option[Station]] = {
    dbConfig.db.run(stations.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Station]] = {
    dbConfig.db.run(stations.result)
  }

  def temp() = {
    stations.map(_.name)
  }


}
