package controllers

import com.mongodb.client.result.DeleteResult
import models.DataModel
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repositories.DataRepository
import services.LibraryService

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ApplicationController @Inject() (
    val controllerComponents: ControllerComponents,
    val dataRepository: DataRepository,
    val service: LibraryService
)(implicit val ec: ExecutionContext)
    extends BaseController {

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

  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel: DataModel, _) =>
        dataRepository.update(id = id, book = dataModel).map(_ => Accepted)
      case JsError(_) => Future(BadRequest)
    }
  }

  def index(): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.index().map {
      case Right(item: Seq[DataModel]) => Ok(Json.toJson(item))
      case Left(_) => BadRequest(Json.toJson("Unable to find any books"))
    }
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    for {
      book <- dataRepository.read(id)
      res = book match {
        case Some(item: DataModel) => Ok(Json.toJson(item))
        case _ | None => BadRequest(Json.toJson("Unable to read book"))
      }
    } yield res
  }

  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.delete(id: String).map {
      case _: DeleteResult => Accepted
      case _ => BadRequest(Json.toJson("Unable to delete book"))
    }
  }

  def getGoogleBook(search: String, term: String): Action[AnyContent] = Action.async {
    implicit request =>
      service.getGoogleBook(search = search, term = term).value.map {
        case Right(book) => Ok(Json.toJson(book))
        case Left(error) => Status(error.httpResponseStatus)(error.reason)
      }
  }
}
