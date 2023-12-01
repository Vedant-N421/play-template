package models

import play.api.libs.json.{Json, OFormat}

case class Book(kind: String, id: String, etag: String, selfLink: String, volumeInfo: VolumeInfo)

case class VolumeInfo(title: String, subtitle: String, authors: List[String], description: String)

case class BookList(items: Seq[Book])

object Book {
  implicit val formats: OFormat[Book] = Json.format[Book]
}

object VolumeInfo {
  implicit val formats: OFormat[VolumeInfo] = Json.format[VolumeInfo]
}
