package models

import play.api.libs.json.{Json, OFormat}

class DataModel {
  case class DataModel(_id: String,
                       name: String,
                       description: String,
                       numSales: Int)

  object DataModel {
    implicit val formats: OFormat[DataModel] = Json.format[DataModel]
  }
}
