package controllers

import controllers.Response.jsonResponse
import models.EmployeeDao
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Controller, _}

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your application is ready."))
  }

  case class Employee( id: Int,
                       name: String,
                      address: String,
                      dob: String,
                      doj: String,
                      designation: String)


  implicit val tupleWrit = new Writes[List[Employee]] {
    override def writes(o: List[Employee]) = {
      Json.toJson(
        o.map { x =>
          Json.obj(
            "id" -> x.id,
            "name" -> x.name,
            "address" -> x.address,
            "dob" -> x.dob,
            "doj" -> x.doj,
            "designation" -> x.designation)
        })
    }
  }

  //POST

  var employeeSet = scala.collection.mutable.Set[String]()
  //adding hardcoded data in the employee set
  employeeSet += ("peter","mary","simpson","michael","watson","shane","brett","steve","mark")

  def addEmployee = Action.async(parse.json) { implicit request =>
    val name = request.body \ "name"
    val address = request.body \ "address"
    val doj = request.body \ "doj"
    val dob = request.body \ "dob"
    val designation = request.body \ "designation"

    val empDoj = parseDate(doj.toString())
    val empDob = parseDate(dob.toString())
    println("doj " + empDoj)
    println("dob " + empDob)

    name.asOpt[String] match {
      case Some(empName) if (!empName.isEmpty && !empDoj.isEmpty && employeeSet.add(empName.toLowerCase)) =>
        val empAddr = address.asOpt[String].getOrElse("")
        val empDesignation = designation.asOpt[String].getOrElse("")
        val emp = EmployeeDao.insert(0, empName, empAddr, empDoj, empDob, empDesignation)

        emp.map { x =>
          Ok(jsonResponse("Success",
            "Added new employee ", Json.toJson(Json.obj("id" -> x))))
        }
      case _ => Future {
        BadRequest(jsonResponse("Failure",
          "Failed to insert a new employee", Json.toJson("")))
      }
    }
  }

  def parseDate(value: String) = {
    val d = value.split("-")
    if(d.length == 3)
      value
    else
      ""
  }
  //GET
  def listEmployees = Action.async { implicit request =>

    val emp = EmployeeDao.list()
    val empList = new ListBuffer[Employee]

    emp.map { x =>
      x.map { y =>
        empList += Employee(y._1, y._2, y._3, y._4, y._5, y._6)
      }

      Ok(jsonResponse("Success", "List of project", Json.toJson(empList.toList)))
    }
  }

  def getEmployee(id: Int) = Action.async { implicit request =>

    val emp = EmployeeDao.filterByEmpId(id)
    val empDetails = new ListBuffer[Employee]

    emp.map { x =>
      x.map { y =>
        empDetails += Employee(y._1, y._2, y._3, y._4, y._5, y._6)
      }

      Ok(jsonResponse("Success", "Employee Details ", Json.toJson(empDetails.toList)))
    }
  }

  def updateAddress(id: Int, address: String) = Action.async { implicit request =>
    val numfRows = EmployeeDao.upDateEmployeeAddress(id, address)
    numfRows.map { n =>
      Ok(jsonResponse("Sucess", s"${n} rows updated", Json.toJson(Json.obj("numOfRows" -> n))))
    }
  }

  def updateDOB(id: Int, dob: String) = Action.async { implicit request =>
    val numfRows = EmployeeDao.upDateEmployeeDOB(id, dob)
    numfRows.map { n =>
      Ok(jsonResponse("Sucess", s"${n} rows updated", Json.toJson(Json.obj("numOfRows" -> n))))
    }
  }

  def updateDesignation(id: Int, designation: String) = Action.async { implicit request =>
    val numfRows = EmployeeDao.upDateEmployeeDesignation(id, designation)
    numfRows.map { n =>
      Ok(jsonResponse("Sucess", s"${n} rows updated", Json.toJson(Json.obj("numOfRows" -> n))))
    }
  }

  def deleteEmployeeRecord(id: Int) = Action.async { implicit request =>
    val numOfRows = EmployeeDao.deleteAnEmployee(id)
    numOfRows.map { n =>
      Ok(jsonResponse("Success",
        s"${n} rows deleted", Json.toJson(Json.obj("numOfRows" -> n))))
    }
  }

  def listEmployeesByBounds(lower : Int, higher : Int) = Action.async { implicit request =>


    val emp = EmployeeDao.list()
    val empList = new ListBuffer[Employee]

    emp.map { x =>
      x.map { y =>
        empList += Employee(y._1, y._2, y._3, y._4, y._5, y._6)
      }

      Ok(jsonResponse("Success", "List of project", Json.toJson(empList.toList.range(lower,higher))))
    }
  }


}
