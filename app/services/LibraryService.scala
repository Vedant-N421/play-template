package services

import cats.data.EitherT
import connectors.LibraryConnector
import models._

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LibraryService @Inject()(connector: LibraryConnector) {
  def getGoogleBook(search: String, term: String, urlOverride: Option[String] = None)(
      implicit ec: ExecutionContext
  ): EitherT[Future, APIError, List[Book]] = {
    connector
      .get[Book](
        urlOverride.getOrElse(s"https://www.googleapis.com/books/v1/volumes?q=$search%$term")
      )
  }
}
