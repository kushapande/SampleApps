package models

import play.api.libs.json.Json

/**
 * Created by kusha on 23/7/15.
 */

case class Employee(id :Option[Int] ,name : String, address: String, doj : String, dob : String, designation : String)


object  Employee {

  implicit val employeeData = Json.writes[Employee]

}


