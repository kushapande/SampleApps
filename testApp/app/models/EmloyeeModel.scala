package models

import slick.driver.H2Driver.api._
import slick.lifted.TableQuery

/**
 * Created by kusha on 22/7/15.
 */

object EmloyeeModel {

  case class EmployeeMaster(id: Int,name : String, addr : String, dob : String,
                            doj : String, designation : String)


  class Employees(tag: Tag) extends Table[EmployeeMaster](tag, "EMPLOYEES") {

    def id = column[Int]("ID",O.PrimaryKey, O.AutoInc)


    def name = column[String]("NAME")

    def address = column[String]("ADDRESS")

    def doj = column[String]("DOJ")

    def dob = column[String]("DOB")

    def designation = column[String]("DESIGNATION")

    def * = ( id,name, address, doj, dob, designation) <> (EmployeeMaster.tupled, EmployeeMaster.unapply)

  }

  val employees = TableQuery[Employees]
}
