package models

import play.api.libs.json.{JsValue, Json, OFormat}

case class Book(body: JsValue, book_ids: List[String], book_names: List[String]){
  def get_book_names(body: JsValue): Unit = {
    val root: Seq[JsValue] = (body \\ "id")
    print(root)
  }
}

object Book {
  implicit val formats: OFormat[Book] = Json.format[Book]
}
