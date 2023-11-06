package controllers

import models.DataModel
import org.scalatest._
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ApplicationControllerSpec @Inject()(implicit val ec: ExecutionContext) extends BaseSpecWithApplication {
  val TestApplicationController = new ApplicationController(
    component, repository
  )

  private val dataModel: DataModel = DataModel(
    "abcd",
    "test name",
    "test description",
    100
  )

  "ApplicationController .create" should {
    "create a book in the database" in {
      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      assert(status(createdResult) == Status.OK)
    }
  }

  "ApplicationController .index" should {
    val result = TestApplicationController.index()(FakeRequest())
    "return 200 OK" in {
      assert(status(result) == Status.OK)
    }
  }

  "ApplicationController .index()" should {

  }

  "ApplicationController .create()" should {

  }

  "ApplicationController .read()" should {

  }

  "ApplicationController .update()" should {

  }

  "ApplicationController .delete()" should {

  }

}
