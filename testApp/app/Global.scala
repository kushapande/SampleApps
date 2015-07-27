import models.{EmployeeDao, EmloyeeModel}
import play.api.Play.current
import play.api.{Application, _}
import play.api.db._
import slick.driver.H2Driver.api._

object Global extends GlobalSettings {

  override def onStart(app: Application) = {

    val db = Database.forDataSource(DB.getDataSource())
    Logger.info("application has started")
    val createTables = DBIO.seq(
      (EmloyeeModel.employees.schema).create)
    val setupFuture = db.run(createTables)
    StartData.insert()

  }

  object StartData {
    //add hard coded data
    def insert() = {
      EmployeeDao.insert(0, "Peter", "Texas","12-04-2010","02-10-1980","Engineer")
      EmployeeDao.insert(0, "Mary", "Dallas","12-04-2010","02-10-1980","Engineer")
      EmployeeDao.insert(0, "Simpson", "SFO","12-04-2010","02-10-1980","Engineer")
      EmployeeDao.insert(0, "Michael", "Boston","12-04-2010","02-10-1980","Engineer")
      EmployeeDao.insert(0, "Watson", "Texas","12-04-2010","02-10-1980","Engineer")
      EmployeeDao.insert(0, "Shane", "Texas","12-04-2010","02-10-1980","Engineer")
      EmployeeDao.insert(0, "Brett", "Texas","12-04-2010","02-10-1980","Engineer")
      EmployeeDao.insert(0, "Steve", "Texas","12-04-2010","02-10-1980","Engineer")
      EmployeeDao.insert(0, "Mark", "Texas","12-04-2010","02-10-1980","Engineer")
    }
  }

}
