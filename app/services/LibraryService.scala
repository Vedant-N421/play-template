package services

import cats.data.EitherT
import connectors.LibraryConnector
import models.{APIError, DataModel}
import play.api.libs.json.JsValue

//import java.awt.print.Book
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LibraryService @Inject()(connector: LibraryConnector) {

  case class response(res: JsValue) // Very confused why I need a case class for a response? Is this "Book"?

  def getGoogleBook(urlOverride: Option[String] = None, search: String, term: String)(implicit ec: ExecutionContext): EitherT[Future, APIError, DataModel] =
    connector.get[DataModel](urlOverride.getOrElse(s"https://www.googleapis.com/books/v1/volumes?q=$search%$term"))
}
