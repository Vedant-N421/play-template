package controllers

import com.mongodb.client.result.UpdateResult
import models.{APIError, DataModel}
import org.mongodb.scala.result.DeleteResult
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.AnyContentAsEmpty
import play.api.test.CSRFTokenHelper.CSRFFRequestHeader
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, POST}
import repositories.DataRepoTrait
import services.RepositoryService

import scala.concurrent.{ExecutionContext, Future}

class RepositoryServiceSpec extends BaseSpec with MockFactory with ScalaFutures with GuiceOneAppPerSuite {

  val mockRepository: DataRepoTrait = mock[DataRepoTrait]
  implicit val executionContext: ExecutionContext =
    app.injector.instanceOf[ExecutionContext]
  val testService = new RepositoryService(mockRepository)

  def buildPost(url: String): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(POST, url).withCSRFToken.asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  def buildGet(url: String): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, url).withCSRFToken.asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  val book1: JsValue = Json.obj(
    "_id" -> "001",
    "name" -> "A song of fire and ice",
    "description" -> "Just some book series with a bad ending, don't read.",
    "numSales" -> 100,
    "isbn" -> "00001"
  )

  val book2: JsValue = Json.obj(
    "_id" -> "002",
    "name" -> "Another one",
    "description" -> "Testing something go away.",
    "numSales" -> 76,
    "isbn" -> "00002"
  )

  val emptyBookSeq: Seq[DataModel] = Seq()

  "index" should {
    "return an empty sequence of datamodel" in {
      (() => mockRepository.index()).expects().returning(Future(Right(emptyBookSeq))).once()
      whenReady(testService.index()) {
        case Left(err) => assert(err == APIError.BadAPIResponse(404, "Books cannot be found"))
        case Right(books) => assert(books == emptyBookSeq)
      }
    }
  }

  "create" should {
    "return the datamodel after succeeding in creating an entry in the DB" in {
      (mockRepository.create _)
        .expects(*)
        .returning(Future(Right(book1.as[DataModel])))
        .once()
      val request: FakeRequest[JsValue] =
        buildPost("/create")
          .withBody[JsValue](Json.toJson(book1))

      whenReady(testService.create(request)) {
        case Left(_) => assert(false)
        case Right(dataModel) => assert(Json.toJson(dataModel) == book1)
      }
    }
  }

  "read" should {
    "return the datamodel after succeeding in reading an entry in the DB" in {
      (mockRepository.read _)
        .expects(*)
        .returning(Future(Right(book1.as[DataModel])))
        .once()
      whenReady(testService.read("001")) {
        case Left(_) => assert(false)
        case Right(dataModel) => assert(Json.toJson(dataModel) == book1)
      }
    }
  }

  "update" should {
    "return the new datamodel after succeeding in updating an entry in the DB" in {
      val mockUpdateResult = mock[UpdateResult]
      (mockRepository
        .update(_: String, _: DataModel))
        .expects(*, *)
        .returning(Future(Right(mockUpdateResult)))
        .once()
      (mockUpdateResult.wasAcknowledged _)
        .expects()
        .returning(true)
        .once()
      val updateRequest: FakeRequest[JsValue] =
        buildPost(s"/update/${book1.as[DataModel]._id}")
          .withBody[JsValue](Json.toJson(book2.as[DataModel]))
      whenReady(testService.update(id = "001", request = updateRequest)) {
        case Left(_) => assert(false)
        case Right(updated) => assert(updated)
      }
    }
  }

  "delete" should {
    "return the result of deleting an entry in the DB" in {
      val mockDeleteResult = mock[DeleteResult]
      (mockRepository.delete _)
        .expects(*)
        .returning(Future.successful(Right(mockDeleteResult)))
        .once()

      (mockDeleteResult.wasAcknowledged _)
        .expects()
        .returning(true)
        .once()

      whenReady(testService.delete("001")) {
        case Left(_) => assert(false)
        case Right(deleted) => assert(deleted)
      }
    }
  }
}
