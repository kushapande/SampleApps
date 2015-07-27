package models

import models.EmloyeeModel.employees
import play.api.Play.current
import play.api.db.DB
import slick.driver.H2Driver.api._

/**
 * Created by kusha on 22/7/15.
 */
object EmployeeDao {



  val db = Database.forDataSource(DB.getDataSource())


  def insert(id : Int,name : String, address : String, dob : String, doj : String, designation : String) = {
    val emp =  (employees returning employees.map(_.id)) +=  EmloyeeModel.EmployeeMaster(id,name ,address, dob, doj ,designation)

      try db.run(emp)
      catch {
        case ex: Throwable => throw new SlickException(ex.getMessage)
    }
  }

  def list()  = {
    val empList = employees.map{ x =>
      (x.id,x.name,x.address, x.dob,x.doj,x.designation)
    }
    val action = empList.result
    try db.run(action)
    catch {
      case ex: Throwable => throw new SlickException(ex.getMessage)
    }

  }

/*  def listByBounds(lower :Int, upper: Int) = {
    val list = new ListBuffer[Employee]
    var a = lower
    for(a  <- lower+1 until upper){
      list += empList(a)
    }

  }*/

  def filterByEmpId(id: Int) = {
   val q = employees.filter( x => x.id=== id).map{ x => (x.id,x.name,x.address, x.dob,x.doj,x.designation)}
    val action = q.result
    try db.run(action)
    catch {
      case ex: Throwable => throw new SlickException(ex.getMessage)
    }
  }

  def upDateEmployeeAddress(id: Int,address : String) = {
    val q =  employees.filter( x => x.id=== id).map( y => y.address)
    val action = q.update(address)
    try db.run(action)
    catch {
      case ex: Throwable => throw new SlickException(ex.getMessage)
    }

  }

  def upDateEmployeeDOB(id: Int,dob : String) = {
    val q =  employees.filter( x => x.id=== id).map( y => y.dob)
    val action = q.update(dob)
    try db.run(action)
    catch {
      case ex: Throwable => throw new SlickException(ex.getMessage)
    }

  }

  def upDateEmployeeDesignation(id: Int,designation : String) = {
    val q =  employees.filter( x => x.id=== id).map( y => y.designation)
    val action = q.update(designation)
    try db.run(action)
    catch {
      case ex: Throwable => throw new SlickException(ex.getMessage)
    }

  }

  def deleteAnEmployee(id: Int) = {
    val q =  employees.filter{ x => x.id === id}
    val action = q.delete
    try db.run(action)
    catch {
      case ex: Throwable => throw new SlickException(ex.getMessage)
    }
  }

/*
  def paginateandFetchInNumbers (num : Int) ={
    import play.api.libs.concurrent.Execution.Implicits.defaultContext
    val input: Enumerator[Employee] = Streams.publisherToEnumerator(db.stream(employees.result))
    val slider: Enumeratee[Employee, List[Employee]] = Enumeratee.grouped[Employee](Iteratee.takeUpTo[Employee](num).map(_.toList))
    input.through(slider)
  }
*/


}
