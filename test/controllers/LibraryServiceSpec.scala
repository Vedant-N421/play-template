package controllers

import connectors.LibraryConnector
import models.DataModel
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status._
import play.api.libs.json.{JsValue, Json, OFormat}
import services.LibraryService

import scala.concurrent.{ExecutionContext, Future}

class LibraryServiceSpec extends BaseSpec with MockFactory with ScalaFutures with GuiceOneAppPerSuite {
  val mockConnector = mock[LibraryConnector]
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
      (mockConnector.get[DataModel](_: String)(_: OFormat[DataModel], _: ExecutionContext))
        .expects(url, *, *)
        .returning(Future(gameOfThrones.as[DataModel]))
        .once()

      whenReady(testService.getGoogleBook(urlOverride = Some(url), search = "", term = "")) { result =>
        assert(result == gameOfThrones.as[DataModel])
      }
    }
  }

  "return an error" in {
    val url: String = "testUrl"

    (mockConnector.get[DataModel](_: String)(_: OFormat[DataModel], _: ExecutionContext))
      .expects(url, *, *)
      .returning(Future[Error]) // How do we return an error?
      .once()

    whenReady(testService.getGoogleBook(urlOverride = Some(url), search = "", term = "")) { result =>
      assert(result == (s"https://www.googleapis.com/books/v1/volumes?q="))
    }
  }

}
