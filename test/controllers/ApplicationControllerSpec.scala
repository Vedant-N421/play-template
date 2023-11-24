package controllers

import models.DataModel
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach}
import play.api.http.Status
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class ApplicationControllerSpec extends BaseSpecWithApplication with BeforeAndAfterEach {
  override def beforeEach(): Unit = await(repository.deleteAll())
//  override def afterEach(): Unit = await(repository.deleteAll())

  val TestApplicationController = new ApplicationController(
    component,
    repository,
    service
  )

  private val dataModel: DataModel =
    DataModel(_id = "abcd", name = "test name", description = "test description", numSales = 100)
  private val baddy: String = "Hello 123"

  "ApplicationController .index" should {
    "return 200 OK" in {
      beforeEach()
      val result = TestApplicationController.index()(FakeRequest())
      assert(status(result) == Status.OK)
    }
  }

  "ApplicationController .create" should {
    "create a book in the database" in {
      beforeEach()
      val request: FakeRequest[JsValue] =
        buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      assert(createdResult.header.status == Status.CREATED)
    }
  }

  "ApplicationController .create with a bad request" should {
    "return an error" in {
      beforeEach()
      val request: FakeRequest[JsValue] =
        buildPost("/create").withBody[JsValue](Json.toJson(baddy))
      val createdResult: Result = await(TestApplicationController.create()(request))
      assert(createdResult.header.status == Status.BAD_REQUEST)
    }
  }

  "ApplicationController .create duplicate request" should {
    "create a book in the database and catch the duplicate request" in {
      beforeEach()
      val request: FakeRequest[JsValue] =
        buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      assert(createdResult.header.status == Status.CREATED)

      val anotherResult: Result = await(TestApplicationController.create()(request))
      assert(anotherResult.header.status == Status.BAD_REQUEST)

    }
  }

  "ApplicationController .read" should {
    "find a book in the database by id" in {
      beforeEach()
      val request: FakeRequest[JsValue] =
        buildPost(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      assert(createdResult.header.status == Status.CREATED)

      val readResult: Future[Result] = TestApplicationController.read("abcd")(FakeRequest())
      assert(status(readResult) == Status.OK)
      assert(contentAsJson(readResult).as[JsValue] == Json.toJson(dataModel))

    }
  }

  "ApplicationController .read with a bad request" should {
    "return an error" in {
      beforeEach()
      val request: FakeRequest[JsValue] =
        buildPost(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      assert(createdResult.header.status == Status.CREATED)

      val readResult: Future[Result] = TestApplicationController.read(baddy)(FakeRequest())
      assert(status(readResult) == Status.BAD_REQUEST)
      assert(contentAsJson(readResult).as[JsValue] == Json.toJson("Unable to read book"))
    }
  }

  "ApplicationController .readAny" should {
    "find a book in the database by name" in {
      beforeEach()
      val request: FakeRequest[JsValue] =
        buildPost(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      assert(createdResult.header.status == Status.CREATED)

      val readResult: Future[Result] =
        TestApplicationController.readAny("name", "test name")(FakeRequest())
      assert(status(readResult) == Status.OK)
      assert(contentAsJson(readResult).as[JsValue] == Json.toJson(dataModel))
    }
  }

  "ApplicationController .readAny" should {
    "find a book in the database by numSales" in {
      beforeEach()
      val request: FakeRequest[JsValue] =
        buildPost(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      assert(createdResult.header.status == Status.CREATED)

      val readResult: Future[Result] =
        TestApplicationController.readAny("numSales", 100)(FakeRequest())
      assert(status(readResult) == Status.OK)
      assert(contentAsJson(readResult).as[JsValue] == Json.toJson(dataModel))
    }
  }

  "ApplicationController .readAny with a bad request" should {
    "return an error" in {
      beforeEach()
      val request: FakeRequest[JsValue] =
        buildPost(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      assert(createdResult.header.status == Status.CREATED)

      val readResult: Future[Result] =
        TestApplicationController.readAny("name", baddy)(FakeRequest())
      assert(status(readResult) == Status.BAD_REQUEST)
      assert(contentAsJson(readResult).as[JsValue] == Json.toJson("Unable to read book"))
    }
  }

  "ApplicationController .readAny with a bad field request" should {
    "return an error" in {
      beforeEach()
      val request: FakeRequest[JsValue] =
        buildPost(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      assert(createdResult.header.status == Status.CREATED)

      val readResult: Future[Result] =
        TestApplicationController.readAny(baddy, "sjndfnjosfn")(FakeRequest())
      assert(status(readResult) == Status.BAD_REQUEST)
      assert(contentAsJson(readResult).as[JsValue] == Json.toJson("Unable to read book"))
    }
  }

  "ApplicationController .update()" should {
    "update a book in the database by id" in {
      beforeEach()
      // First we need to create a book
      val request: FakeRequest[JsValue] =
        buildPost(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      assert(createdResult.header.status == Status.CREATED)

      // To then be updated by the following code
      val updateRequest: FakeRequest[JsValue] =
        buildPost(s"/update/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val updateResult: Result =
        await(TestApplicationController.update(id = "abcd")(updateRequest))

      // Check if request was accepted
      assert(updateResult.header.status == Status.ACCEPTED)
    }
  }

  "ApplicationController .partialUpdate()" should {
    "partially updates a field of a book in the database by id" in {
      beforeEach()
      // First we need to create a book
      val request: FakeRequest[JsValue] =
        buildPost(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      assert(createdResult.header.status == Status.CREATED)

      // To then be updated by the following code
      val updateRequest: FakeRequest[JsValue] =
        buildPost(s"/update/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val updateResult: Result =
        await(
          TestApplicationController.partialUpdate(id = "abcd", "name", "replaced")(updateRequest)
        )
      // Check if request was accepted
      assert(updateResult.header.status == Status.ACCEPTED)
    }
  }

  "ApplicationController .update() with a bad request" should {
    "return an error" in {
      beforeEach()
      // First we need to create a book
      val request: FakeRequest[JsValue] =
        buildPost(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      assert(createdResult.header.status == Status.CREATED)

      // To then be updated by the following code
      val updateRequest: FakeRequest[JsValue] =
        buildPost(s"/update/${dataModel._id}").withBody[JsValue](Json.toJson(baddy))
      val updateResult: Result =
        await(TestApplicationController.update(baddy)(updateRequest))

      // Check if request was accepted
      assert(updateResult.header.status == Status.BAD_REQUEST)
    }
  }

  "ApplicationController .delete()" should {
    "delete a book in the database by id" in {
      beforeEach()
      // First we create a book to be deleted
      val request: FakeRequest[JsValue] =
        buildPost(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      // and we check that it actually does get made
      assert(createdResult.header.status == Status.CREATED)

      // and then we check if it gets deleted
      val deleteResult: Result =
        await(TestApplicationController.delete(dataModel._id)(FakeRequest()))
      assert(deleteResult.header.status == Status.ACCEPTED)

    }
  }

  "ApplicationController .delete() with a bad request" should {
    "return an error" in {
      beforeEach()
      // First we create a book to be deleted
      val request: FakeRequest[JsValue] =
        buildGet(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))
      // and we check that it actually does get made
      assert(createdResult.header.status == Status.CREATED)

      // and the code will still return an accepted if it found nothing to delete
      val deleteResult: Result =
        await(TestApplicationController.delete(baddy)(FakeRequest()))
      assert(deleteResult.header.status == Status.BAD_REQUEST)
    }
  }
}
