package controllers

import play.api.libs.json.{Json, JsValue}

/**
 * Created by kusha on 22/7/15.
 */
object Response {

  def jsonResponse(status: String, msg: String, data: JsValue) = Json.prettyPrint(Json.obj(
    "Status" -> Json.toJson(status),
    "Msg" -> Json.toJson(msg),
    "Data" -> data))
}