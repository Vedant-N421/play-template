package repositories

import com.google.inject.ImplementedBy
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

  def create(book: DataModel): Future[Option[DataModel]]

  def read(id: String): Future[Option[DataModel]]

  def readAny[T](field: String, value: T): Future[Option[DataModel]]

  def update(id: String, book: DataModel): Future[result.UpdateResult]

  def partialUpdate[T](id: String, field: String, value: T): Future[Option[DataModel]]

  def delete(id: String): Future[Either[String, result.DeleteResult]]
}

@Singleton
class DataRepository @Inject() (mongoComponent: MongoComponent)(implicit ec: ExecutionContext)
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

  def create(book: DataModel): Future[Option[DataModel]] = {
    collection.find(byID(book._id)).headOption().flatMap {
      case Some(_) => Future(None)
      case _ => collection.insertOne(book).toFuture().map(_ => Some(book))
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

  def readAny[T](field: String, value: T): Future[Option[DataModel]] = {
    collection
      .find(byAny(field, value))
      .headOption()
      .flatMap {
        case Some(data) =>
          Future(Some(data))
        case _ =>
          Future(None)
      }
      .recover { case error =>
        None
      }

  }

  def read(id: String): Future[Option[DataModel]] =
    collection.find(byID(id)).headOption().flatMap {
      case Some(data) =>
        Future(Some(data))
      case _ =>
        Future(None)
    }

  def update(id: String, book: DataModel): Future[result.UpdateResult] =
    collection
      .replaceOne(
        filter = byID(id),
        replacement = book,
        options = new ReplaceOptions().upsert(
          false
        ) // What happens when we set this to false? Ans: update doesn't insert a new record if it can't find it .
      )
      .toFuture()

  def partialUpdate[T](id: String, field: String, value: T): Future[Option[DataModel]] = {
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
        update(id, updatedBook).map(thing => Some(updatedBook))
      case _ => Future.successful(None)
    }
  }

  def delete(id: String): Future[Either[String, result.DeleteResult]] = {
    collection.find(byID(id)).headOption().flatMap {
      case Some(_) => (collection.deleteOne(filter = byID(id)).toFuture().map(Right(_)))
      case _ => Future(Left("INFO: Book not found, so no deletion with this operation!"))
    }
  }

  def deleteAll(): Future[Unit] =
    collection.deleteMany(empty()).toFuture().map(_ => ()) // Hint: needed for tests
}
