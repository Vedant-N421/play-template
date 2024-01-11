package services

import com.mongodb.client.result.DeleteResult
import models.APIError.BadRequestError
import models.{APIError, DataModel}
import org.mongodb.scala.result.UpdateResult
import play.api.libs.json.{JsError, JsSuccess, JsValue}
import play.api.mvc.Request
import repositories.DataRepoTrait

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RepositoryService @Inject()(
    val dataRepoTrait: DataRepoTrait
)(implicit executionContext: ExecutionContext) {

  def createGoogleBook(book: DataModel): Future[Either[APIError, DataModel]] = dataRepoTrait.create(book)

  def create(request: Request[JsValue]): Future[Either[APIError, DataModel]] = {
    request.body.validate[DataModel] match {
      case JsSuccess(book, _) => dataRepoTrait.create(book)
      case JsError(_) => Future(Left(BadRequestError()))
    }
  }

  def update(id: String, request: Request[JsValue]): Future[Either[APIError, Boolean]] = {
    request.body.validate[DataModel] match {
      case JsSuccess(book: DataModel, _) =>
        dataRepoTrait.update(id, book).map {
          case Right(x: UpdateResult) => Right(x.wasAcknowledged())
        }
      case JsError(_) => Future(Left(BadRequestError()))
    }
  }

  def partialUpdate[T](
      id: String,
      field: String,
      value: T
  ): Future[Either[APIError, Boolean]] = {
    dataRepoTrait.partialUpdate(id, field, value).map {
      case Right(x) => Right(x.wasAcknowledged())
      case Left(error) => Left(error)
    }
  }

  def index(): Future[Either[APIError, Seq[DataModel]]] = dataRepoTrait.index()

  def read(id: String): Future[Either[APIError, DataModel]] = dataRepoTrait.read(id)

  def readAny[T](field: String, value: T): Future[Either[APIError, DataModel]] = dataRepoTrait.readAny(field, value)

  def delete(id: String): Future[Either[APIError, Boolean]] = {
    dataRepoTrait.delete(id: String).map {
      case Right(x: DeleteResult) => Right(x.wasAcknowledged())
      case Left(error) => Left(error)
    }
  }
}
