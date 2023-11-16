package controllers

import cats.data.EitherT
import connectors.LibraryConnector
import models.{APIError, Book}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsValue, Json, OFormat}
import services.LibraryService

import scala.concurrent.ExecutionContext

class LibraryServiceSpec
    extends BaseSpec
    with MockFactory
    with ScalaFutures
    with GuiceOneAppPerSuite {
  val mockConnector: LibraryConnector = mock[LibraryConnector]
  implicit val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  val testService = new LibraryService(mockConnector)

  val gameOfThrones: JsValue = Json.obj(
    "_id" -> "someId",
    "name" -> "A Game of Thrones",
    "description" -> "The best book!!!",
    "numSales" -> 100
  )

  "getGoogleBook" should {
    val url: String = "testUrl"
    "return a book" in {
      (mockConnector
        .get[Book](_: String)(_: OFormat[Book], _: ExecutionContext))
        .expects(url, *, *)
        .returning(EitherT.rightT(gameOfThrones.as[Book]))
        .once()
      whenReady(testService.getGoogleBook(urlOverride = Some(url), search = "", term = "").value) {
        result =>
          assert(result == Right(gameOfThrones.as[Book]))
      }
    }
  }

  "an error case" should {
    "return an error" in {
      val url: String = "testUrl"
      (mockConnector
        .get[Book](_: String)(_: OFormat[Book], _: ExecutionContext))
        .expects(url, *, *)
        .returning(EitherT.leftT(APIError.BadAPIResponse(500, "Could not connect")))
        .once()
      whenReady(testService.getGoogleBook(urlOverride = Some(url), search = "", term = "").value) {
        result =>
          assert(result == Left(APIError.BadAPIResponse(500, "Could not connect")))
      }
    }
  }
}
