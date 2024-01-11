package repositories

import com.google.inject.ImplementedBy
import models.APIError.{ConflictError, NoContentError, ResourceNotFound}
import models.{APIError, DataModel}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model._
import org.mongodb.scala.result
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[DataRepository])
trait DataRepoTrait {
  def index(): Future[Either[APIError, Seq[DataModel]]]

  def create(book: DataModel): Future[Either[APIError, DataModel]]

  def read(id: String): Future[Either[APIError, DataModel]]

  def readAny[T](field: String, value: T): Future[Either[APIError, DataModel]]

  def update(id: String, book: DataModel): Future[Either[models.APIError, result.UpdateResult]]

  def partialUpdate[T](id: String, field: String, value: T): Future[Either[APIError, result.UpdateResult]]

  def delete(id: String): Future[Either[APIError, result.DeleteResult]]
}

@Singleton
class DataRepository @Inject()(mongoComponent: MongoComponent)(implicit ec: ExecutionContext)
    extends PlayMongoRepository[DataModel](
      collectionName = "dataModels",
      mongoComponent = mongoComponent,
      domainFormat = DataModel.formats,
      indexes = Seq(
        IndexModel(
          Indexes.ascending("_id")
        )
      ),
      replaceIndexes = false
    )
    with DataRepoTrait {

  def index(): Future[Either[APIError, Seq[DataModel]]] =
    collection.find().toFuture().map {
      case books: Seq[DataModel] => Right(books)
      case _ => Left(APIError.BadAPIResponse(404, "Books cannot be found"))
    }

  def create(book: DataModel): Future[Either[APIError, DataModel]] = {
    collection.find(byID(book._id)).headOption().flatMap {
      case Some(_) => Future.successful(Left(ConflictError()))
      case _ => collection.insertOne(book).toFuture().map(_ => Right(book))
    }
  }

  private def byAny[T](field: String, value: T): Bson = {
    Filters.and(
      Filters.equal(
        field match {
          case field if field == "id" => "_id"
          case field if List("name", "description", "numSales", "isbn").contains(field) => field
          case _ =>
            println("Invalid field name specified, defaulting to id!")
            "_id"
        },
        value
      )
    )
  }

  private def byID(id: String): Bson =
    Filters.and(
      Filters.equal("_id", id)
    )

  def readAny[T](field: String, value: T): Future[Either[APIError, DataModel]] = {
    collection
      .find(byAny(field, value))
      .headOption()
      .flatMap {
        case Some(data) =>
          Future(Right(data))
        case _ =>
          Future(Left(ResourceNotFound()))
      }
  }

  def read(id: String): Future[Either[APIError, DataModel]] =
    collection.find(byID(id)).headOption().flatMap {
      case Some(book) =>
        Future(Right(book))
      case _ =>
        Future(Left(ResourceNotFound()))
    }

  def update(id: String, book: DataModel): Future[Either[APIError, result.UpdateResult]] =
    collection
      .replaceOne(
        filter = byID(id),
        replacement = book,
        options = new ReplaceOptions().upsert(
          false
        )
      )
      .toFuture()
      .flatMap {
        case x if x.wasAcknowledged() => Future(Right(x))
        case _ => Future.successful(Left(ConflictError()))
      }

  def partialUpdate[T](id: String, field: String, value: T): Future[Either[APIError, result.UpdateResult]] = {
    collection.find(byID(id)).headOption.flatMap {
      case Some(book) =>
        val updatedBook = field match {
          case "id" => book.copy(_id = value.toString)
          case "name" => book.copy(name = value.toString)
          case "description" => book.copy(description = value.toString)
          case "numSales" => book.copy(numSales = value.asInstanceOf[Int])
          case "isbn" => book.copy(isbn = value.toString)
          case _ => book
        }
        update(id, updatedBook)
      case _ => Future.successful(Left(NoContentError()))
    }
  }

  def delete(id: String): Future[Either[APIError, result.DeleteResult]] = {
    collection.deleteOne(byID(id)).toFuture().flatMap {
      case x if x.wasAcknowledged() => Future(Right(x))
      case _ => Future.successful(Left(ConflictError()))
    }
  }

  def deleteAll(): Future[Unit] =
    collection.deleteMany(empty()).toFuture().map(_ => ())
}
