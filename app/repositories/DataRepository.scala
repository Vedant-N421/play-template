package repositories

import models.{APIError, DataModel}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model._
import org.mongodb.scala.result
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

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
    ) {

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
    Filters.and {
      Filters.equal(
        field match {
          case field if field == "id" => "_id"
          case field if List("name", "description", "numSales").contains(field) => field
          case _ =>
            println("Invalid field name specified, defaulting to id!")
            "_id"
        },
        value
      )
    }
  }

  private def byID(id: String): Bson =
    Filters.and(
      Filters.equal("_id", id)
    )

  def readAny[T](field: String, value: T): Future[Option[DataModel]] = {
    collection.find(byAny(field, value)).headOption.flatMap {
      case Some(data) => Future(Some(data))
      case _ => Future(None)
    }
  }

  def read(id: String): Future[Option[DataModel]] =
    collection.find(byID(id)).headOption.flatMap {
      case Some(data) => Future(Some(data))
      case _ => Future(None)
    }

  def update(id: String, book: DataModel): Future[result.UpdateResult] =
    collection
      .replaceOne(
        filter = byID(id),
        replacement = book,
        options = new ReplaceOptions().upsert(
          false
        ) // What happens when we set this to false? Ans: the code works as intended, doesn't insert a new record.
      )
      .toFuture()

  def partialUpdate[T](id: String, field: String, value: T): Future[result.UpdateResult] = {
    val oldBook = read(id)
    val replacementBook = field match {
      case "id" =>
        oldBook.map { case Some(book) =>
          book.copy(_id = value.toString)
        }
      case "name" =>
        oldBook.map { case Some(book) =>
          book.copy(name = value.toString)
        }
      case "description" =>
        oldBook.map { case Some(book) =>
          book.copy(description = value.toString)
        }
      case "numSales" =>
        oldBook.map { case Some(book) =>
          book.copy(numSales = value.asInstanceOf[Int])
        }
    }
    collection
      .replaceOne(
        filter = byID(id),
        replacement = Await.result(replacementBook, 2.seconds),
        options = new ReplaceOptions().upsert(
          false
        )
      )
      .toFuture()
  }

  def delete(id: String): Future[Any] = {
    collection.find(byID(id)).headOption().flatMap {
      case Some(_) => collection.deleteOne(filter = byID(id)).toFuture()
      case _ => Future(println("INFO: Book not found, so no deletion with this operation!"))
    }
    // Old implementation used Future[Result.DeleteOne] as a return type, containing:
    // collection.deleteOne(filter = byID(id)).toFuture()
  }

  def deleteAll(): Future[Unit] =
    collection.deleteMany(empty()).toFuture().map(_ => ()) // Hint: needed for tests
}
