package services

import com.mongodb.client.result.DeleteResult
import models.{APIError, DataModel}
import play.api.libs.json.{JsError, JsSuccess, JsValue}
import play.api.mvc.Request
import repositories.DataRepoTrait

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RepositoryService @Inject() (
    val dataRepoTrait: DataRepoTrait
)(implicit executionContext: ExecutionContext) {

  def createGoogleBook(book: DataModel): Future[Either[String, DataModel]] = {
    dataRepoTrait.create(book).map {
      case None => Left("ERROR: Duplicate found, item not created.")
      case _ => Right(book)
    }
  }

  def create(request: Request[JsValue]): Future[Either[String, DataModel]] = {
    request.body.validate[DataModel] match {
      case JsSuccess(book, _) =>
        dataRepoTrait.create(book).map {
          case None => Left("ERROR: Duplicate found, item not created.")
          case _ => Right(book)
        }
      case JsError(_) => Future(Left("ERROR: Item not created."))
    }
  }

  def update(id: String, request: Request[JsValue]): Future[Either[String, DataModel]] = {
    request.body.validate[DataModel] match {
      case JsSuccess(book: DataModel, _) =>
        dataRepoTrait.update(id, book)
        Future(Right(book))
      case JsError(_) => Future(Left("ERROR: Item not updated."))
    }
  }

  def partialUpdate[T](
      id: String,
      field: String,
      value: T,
      request: Request[JsValue]
  ): Future[Either[String, DataModel]] = {
    dataRepoTrait.partialUpdate(id, field, value).map {
      case Some(book) => Right(book)
      case _ => Left("ERROR: Item not updated.")
    }
  }

  def index(): Future[Either[APIError, Seq[DataModel]]] = {
    dataRepoTrait.index().map {
      case Right(item: Seq[DataModel]) => Right(item)
      case Left(error: APIError.BadAPIResponse) => Left(error)
    }
  }

  def read(id: String): Future[Either[String, DataModel]] = {
    for {
      book <- dataRepoTrait.read(id)
      res = book match {
        case Some(item: DataModel) => Right(item)
        case _ | None => Left("ERROR: Unable to read book.")
      }
    } yield res
  }

  def readAny[T](field: String, value: T): Future[Either[String, DataModel]] = {
    for {
      book <- dataRepoTrait.readAny(field, value)
      res = book match {
        case Some(item: DataModel) => Right(item)
        case _ | None => Left("ERROR: Unable to read book.")
      }
    } yield res
  }

  def delete(id: String): Future[Either[String, String]] = {
    dataRepoTrait.delete(id: String).map {
      case Right(_: DeleteResult) => Right("INFO: Item was deleted successfully.")
      case Left(error) => Left(error)
    }
  }
}
