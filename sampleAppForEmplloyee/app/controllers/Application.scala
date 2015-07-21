package controllers

import views._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.{ DAOComponent, DAO, Employee }
import java.util.concurrent.TimeoutException
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


class Application(dao: DAOComponent) extends Controller {

//redirect to home

  val Home = Redirect(routes.Application list(0, 2, ""))


  val employeeForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText,
      "address" -> nonEmptyText,
      "dob" -> optional(date("MM/dd/yyyy")),
      "joiningDate" -> default(date("MM/dd/yyyy"), new java.util.Date),
      "designation" -> optional(text))(Employee.apply)(Employee.unapply))


  def index = Action { Home }

  def list(page: Int, orderBy: Int, filter: String): Action[AnyContent] = Action.async { implicit request =>
    dao.list(page, 10, orderBy, "%" + filter + "%").map { pageEmp =>
      Ok(html.list(pageEmp, orderBy, filter))
    }.recover {
      case ex: TimeoutException =>
        Logger.error("Problem found in employee list process")
        InternalServerError(ex.getMessage)
    }
  }

  def edit(id: Long): Action[AnyContent] = Action.async { implicit request =>
    dao.findById(id).map(employee => Ok(html.editForm(id, employeeForm.fill(employee)))).recover {
      case ex: TimeoutException =>
        Logger.error("Problem found in employee edit process")
        InternalServerError(ex.getMessage)
    }
  }

  def update(id: Long): Action[AnyContent] = Action.async { implicit request =>
    employeeForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.editForm(id, formWithErrors))),
      employee => {
        val futureEmpUpdate = dao.update(id, employee.copy(id = Some(id)))
        futureEmpUpdate.map { result =>
          Home.flashing("success" -> "Employee %s has been updated".format(employee.name))
        }.recover {
          case ex: TimeoutException =>
            Logger.error("Problem found in employee update process")
            InternalServerError(ex.getMessage)
        }
      })
  }

  def create: Action[AnyContent] = Action { implicit request =>
    Ok(html.createForm(employeeForm))
  }

  def save: Action[AnyContent] = Action.async { implicit request =>
    employeeForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.createForm(formWithErrors))),
      employee => {
        val futureEmpInsert = dao.insert(employee)
        futureEmpInsert.map { result => Home.flashing("success" -> "Employee %s has been created".format(employee.name)) }.recover {
          case ex: TimeoutException =>
            Logger.error("Problem found in employee save process")
            InternalServerError(ex.getMessage)
        }
      })
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val futureEmpDel = dao.delete(id)
    futureEmpDel.map { result => Home.flashing("success" -> "Employee has been deleted") }.recover {
      case ex: TimeoutException =>
        Logger.error("Problem found in employee delete process")
        InternalServerError(ex.getMessage)
    }
  }

}

object Application extends Application(DAO)