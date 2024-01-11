package controllers

import models.{APIError, DataModel}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach}
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AnyContentAsFormUrlEncoded, Result}
import play.api.test.CSRFTokenHelper.CSRFRequest
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class ApplicationControllerSpec extends BaseSpecWithApplication with BeforeAndAfterEach {
  override def beforeEach(): Unit = await(repository.deleteAll())

  val TestApplicationController = new ApplicationController(
    component,
    service,
    repoService
  )

  private val dataModel: DataModel =
    DataModel(
      _id = "abcd",
      name = "test name",
      description = "test description",
      numSales = 100,
      isbn = "blah"
    )
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

  // make it more clear in your tests what you are doing! i.e. uploading the same book twice etc
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
      assert(contentAsJson(readResult).as[JsValue] == Json.toJson(APIError.ResourceNotFound().reason))
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
      assert(contentAsJson(readResult).as[JsValue] == Json.toJson(APIError.ResourceNotFound().reason))
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
      assert(contentAsJson(readResult).as[JsValue] == Json.toJson(APIError.ResourceNotFound().reason))
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
    "partially update a field of a book in the database by id" in {
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

      // Need to make sure that field actually did change!
      val readResult: Future[Result] = TestApplicationController.read("abcd")(FakeRequest())
      assert(
        contentAsJson(readResult).as[JsValue] == Json.toJson(
          DataModel("abcd", "replaced", "test description", 100, "blah")
        )
      )
    }
  }

  "ApplicationController .partialUpdate() with a request that does not find a book" should {
    "return a bad request and not change any books in the database" in {
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
          TestApplicationController.partialUpdate(id = "hiafnadfjn", "name", "replaced")(
            updateRequest
          )
        )
      // Check if request was identified as useless
      assert(updateResult.header.status == Status.BAD_REQUEST)

      val readResult: Future[Result] = TestApplicationController.read("abcd")(FakeRequest())
      assert(
        contentAsJson(readResult).as[JsValue] == Json.toJson(
          DataModel("abcd", "test name", "test description", 100, "blah")
        )
      )

    }
  }

  "ApplicationController .partialUpdate() with a good request but bad field change" should {
    "not make any changes to the book in the database" in {
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
          TestApplicationController.partialUpdate(id = "abcd", "non-existent field", "replaced")(
            updateRequest
          )
        )
      // Check if update status was correct
      assert(updateResult.header.status == Status.ACCEPTED)

      // Check if book was not edited and stays the same
      val readResult: Future[Result] = TestApplicationController.read("abcd")(FakeRequest())
      assert(
        contentAsJson(readResult).as[JsValue] == Json.toJson(
          DataModel("abcd", "test name", "test description", 100, "blah")
        )
      )
    }
  }

  // descriptive with the test scenario
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
      // error code/http code here? for the "bad" request? more descriptive?
      // the status can be "bad request" but the message could actually give away what's the problem
      assert(updateResult.header.status == Status.BAD_REQUEST)
    }
  }

  "ApplicationController .delete()" should {
    "delete a book in the database by id" in {
      beforeEach()

      val request: FakeRequest[JsValue] =
        buildPost(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))

      assert(createdResult.header.status == Status.CREATED)

      val deleteResult: Result =
        await(TestApplicationController.delete(dataModel._id)(FakeRequest()))
      assert(deleteResult.header.status == Status.ACCEPTED)

    }
  }

  "ApplicationController .delete() with a bad request" should {
    "return an error" in {
      beforeEach()

      val request: FakeRequest[JsValue] =
        buildGet(s"/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Result = await(TestApplicationController.create()(request))

      assert(createdResult.header.status == Status.CREATED)

      val deleteResult: Result =
        await(TestApplicationController.delete(baddy)(FakeRequest()))
      assert(deleteResult.header.status == Status.ACCEPTED)
    }
  }

  "ApplicationController .getGoogleBook" should {
    "find a book, store it in Mongo and produce the book data in a browser window" in {
      beforeEach()
      val fetchedResult: Result =
        await(TestApplicationController.getGoogleBook("flowers", "inauthor:keyes")(FakeRequest()))
      assert(fetchedResult.header.status == Status.CREATED)

//      Check whether a book with the name flowers for algernon exists
      val readResult: Future[Result] =
        TestApplicationController.readAny("name", "Flowers for Algernon")(FakeRequest())
      assert(status(readResult) == Status.OK)
    }
  }

  "Adding new book via browser form" should {
    "create a new book and add it to MongoDB" in {
      beforeEach()
//      Create a book via form and check it has actually created
      val request: FakeRequest[AnyContentAsFormUrlEncoded] =
        buildPost("/addbook/form")
          .withFormUrlEncodedBody(
            "_id" -> "blue",
            "name" -> "is a colour",
            "description" -> "something about colours",
            "numSales" -> "42",
            "isbn" -> "colore"
          )
      val createdResult: Future[Result] = (TestApplicationController.addNewBookForm()(request))
      assert(status(createdResult) == Status.CREATED)
      assert(
        contentAsJson(createdResult) == Json.toJson(
          DataModel("blue", "is a colour", "something about colours", 42, "colore")
        )
      )

//      Then verify it has been uploaded to the MongoDB
      val readResult: Future[Result] = TestApplicationController.read("blue")(FakeRequest())
      assert(status(readResult) == Status.OK)
      assert(
        contentAsJson(readResult).as[JsValue] == Json.toJson(
          DataModel("blue", "is a colour", "something about colours", 42, "colore")
        )
      )
    }
  }

  "Adding new book via browser form" should {
    "create a bad book and try to add it to MongoDB" in {
      beforeEach()
      //      Create a book via form and check it has actually created
      val request: FakeRequest[AnyContentAsFormUrlEncoded] =
        buildPost("/addbook/form")
          .withFormUrlEncodedBody(
            "_id" -> "blue",
            "name" -> "is a colour",
            "description" -> "something about colours",
            "numSales" -> "THROW AN ERROR: this is not meant to be a string!",
            "isbn" -> "colore"
          )
      val createdResult: Future[Result] = (TestApplicationController.addNewBookForm()(request))
      assert(status(createdResult) == Status.BAD_REQUEST)

      //      Then verify it has been uploaded to the MongoDB
      val readResult: Future[Result] = TestApplicationController.read("blue")(FakeRequest())
      assert(status(readResult) == Status.BAD_REQUEST)
    }
  }
}
