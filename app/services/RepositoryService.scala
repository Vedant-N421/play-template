package services

import models.DataModel
import play.api.libs.json.{JsError, JsSuccess, JsValue}
import play.api.mvc.{Action, ControllerComponents}
import play.api.mvc.Results.{BadRequest, Created}
import repositories.DataRepository

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RepositoryService @Inject(
  val dataRepository: DataRepository
)(){
  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        dataRepository.create(dataModel).map {
          case Some(_) => Created
          case None => BadRequest
        }
      case JsError(_) => Future(BadRequest)
    }
  }
}
