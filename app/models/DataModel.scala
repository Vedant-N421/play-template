package models

import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.{Json, OFormat}

import scala.language.implicitConversions

case class DataModel(_id: String, name: String, description: String, numSales: Int, isbn: String)

object DataModel {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]

  implicit val dataForm: Form[DataModel] = Form(
    mapping(
      "_id" -> text,
      "name" -> text,
      "description" -> text,
      "numSales" -> number,
      "isbn" -> text
    )(DataModel.apply)(DataModel.unapply)
  )
}
