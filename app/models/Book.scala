package models

import play.api.libs.json.{Json, OFormat}

case class Book(kind: String, id: String, etag: String, selfLink: String, volumeInfo: VolumeInfo)

case class VolumeInfo(
    title: String,
    subtitle: Option[String],
    authors: List[String],
    description: Option[String],
    industryIdentifiers: List[IndustryIdentifiers],
)

case class IndustryIdentifiers(identifier: String, `type`: String)

case class BookList(items: List[Book])

object Book {
  implicit val formats: OFormat[Book] = Json.format[Book]
}

object VolumeInfo {
  implicit val formats: OFormat[VolumeInfo] = Json.format[VolumeInfo]
}

object IndustryIdentifiers{
  implicit val formats: OFormat[IndustryIdentifiers] = Json.format[IndustryIdentifiers]
}
