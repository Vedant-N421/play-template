package controllers

import models.DataModel
import play.api.http.{HttpConfiguration, Status}
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc.{AnyContent, Result}
import play.api.mvc.Results.BadRequest
import play.api.mvc.request.DefaultRequestFactory
import play.api.test.{FakeRequest, FakeRequestFactory}
import play.api.test.Helpers._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ApplicationControllerSpec extends BaseSpecWithApplication {
  override def beforeEach(): Unit = repository.deleteAll()
  override def afterEach(): Unit = repository.deleteAll()

  val TestApplicationController = new ApplicationController(
    component, repository
  )

  private val dataModel: DataModel = DataModel(_id = "abcd", name = "test name", description = "test description", numSales = 100)
  private val baddy: String = "Hello 123"

  "ApplicationController .index" should {
    "return 200 OK" in {
      beforeEach()
      val result = TestApplicationController.index()(FakeRequest())
      assert(status(result) == Status.OK)
      afterEach()
    }
  }

  "ApplicationController .create" should {
    "create a book in the database" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      assert(status(createdResult) == Status.CREATED)
      afterEach()
    }
  }

  "ApplicationController .create with a bad request" should {
    "return an error" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(baddy))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      assert(status(createdResult) == Status.BAD_REQUEST)
      afterEach()
    }
  }

  "ApplicationController .read" should {
    "find a book in the database by id" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildGet(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      assert(status(createdResult) == Status.CREATED)

      val readResult: Future[Result] = TestApplicationController.read("abcd")(FakeRequest())
      assert(status(readResult) == Status.OK)
      assert(contentAsJson(readResult).as[JsValue] == Json.toJson(dataModel))
      afterEach()
    }
  }

  "ApplicationController .read with a bad request" should {
    "return an error" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildGet(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      assert(status(createdResult) == Status.CREATED)

//      val readRequest: FakeRequest[JsValue] = buildGet(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val readResult: Future[Result] = TestApplicationController.read(baddy)(FakeRequest())
      assert(status(readResult) == Status.BAD_REQUEST)
      assert(contentAsJson(readResult).as[JsValue] == Json.toJson("Unable to read book"))
      afterEach()
    }
  }

  "ApplicationController .update()" should {
    "update a book in the database by id" in {
      beforeEach()
      // First we need to create a book
      val request: FakeRequest[JsValue] = buildGet(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      assert(status(createdResult) == Status.CREATED)

      // To then be updated by the following code
      val updateRequest: FakeRequest[JsValue] = buildPost(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val updateResult: Future[Result] = TestApplicationController.update(id = "abcd")(updateRequest)

      // Check if request was accepted
      assert(status(updateResult) == Status.ACCEPTED)
      afterEach()
    }
  }

  "ApplicationController .update() with a bad request" should {
    "return an error" in {
      beforeEach()
      // First we need to create a book
      val request: FakeRequest[JsValue] = buildGet(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      assert(status(createdResult) == Status.CREATED)

      // To then be updated by the following code
      val updateRequest: FakeRequest[JsValue] = buildPost(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(baddy))
      val updateResult: Future[Result] = TestApplicationController.update(id = baddy)(updateRequest)

      // Check if request was accepted
      assert(status(updateResult) == Status.BAD_REQUEST)
      afterEach()
    }
  }

  "ApplicationController .delete()" should {
    "delete a book in the database by id" in {
      beforeEach()
      // First we create a book to be deleted
      val request: FakeRequest[JsValue] = buildGet(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      // and we check that it actually does get made
      assert(status(createdResult) == Status.CREATED)

      // and then we check if it gets deleted
      val deleteResult: Future[Result] = TestApplicationController.delete("abcd")(FakeRequest())
      assert(status(deleteResult) == Status.ACCEPTED)

      afterEach()
    }
  }

  "ApplicationController .delete() with a bad request" should {
    "return an error" in {
      beforeEach()
      // First we create a book to be deleted
      val request: FakeRequest[JsValue] = buildGet(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      // and we check that it actually does get made
      assert(status(createdResult) == Status.CREATED)

      // and the code will still return an accepted if it found nothing to delete
      val deleteResult: Future[Result] = TestApplicationController.delete(baddy)(FakeRequest())
      assert(status(deleteResult) == Status.ACCEPTED)

      afterEach()
    }
  }
}
