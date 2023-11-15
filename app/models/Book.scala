package models

import play.api.libs.json.{Json, OFormat}

case class Book(_id: String, name: String, description: String, numSales: Int) {
//  def apply(body: JsValue): Unit = {
//    val book_ids: collection.Seq[JsValue] = body \\ "id"
//    print(book_ids)
//  }

//  def apply(body: JsValue): Unit = {
//    print(body)
//  }
}

object Book {
  implicit val formats: OFormat[Book] = Json.format[Book]
//  implicit val writes: Writes[Book] = Json.writes[Book]
}
