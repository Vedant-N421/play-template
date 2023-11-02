package scala

import controllers.ApplicationController
import play.api.test.FakeRequest
import play.api.test.Helpers
import play.api.test.Helpers._
import play.api.http.Status
import play.api.mvc.Result

import scala.concurrent.Future

class ApplicationControllerSpec extends BaseSpecWithApplication {
  val TestApplicationController = new ApplicationController(
    component
  )

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
