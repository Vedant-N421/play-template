package models

import play.api.libs.json.{Json, OFormat}

import scala.language.implicitConversions

case class Book(kind: String, id: String, etag: String, selfLink: String, volumeInfo: VolumeInfo)

case class BookList(ls: List[Book])

case class VolumeInfo(
    title: String,
    subtitle: Option[String],
    authors: List[String],
    description: Option[String],
    industryIdentifiers: List[IndustryIdentifiers],
)

case class IndustryIdentifiers(identifier: String, `type`: String)

object Book {
  implicit val formats: OFormat[Book] = Json.format[Book]
  implicit def convertToDataModel(book: Book): DataModel = {
    DataModel(
      book.id,
      book.volumeInfo.title,
      book.volumeInfo.description.getOrElse("Empty description."),
      1,
      book.volumeInfo.industryIdentifiers.head.identifier
    )
  }
}

object BookList {
  implicit val formats: OFormat[BookList] = Json.format[BookList]
}

object VolumeInfo {
  implicit val formats: OFormat[VolumeInfo] = Json.format[VolumeInfo]
}

object IndustryIdentifiers {
  implicit val formats: OFormat[IndustryIdentifiers] = Json.format[IndustryIdentifiers]
}
