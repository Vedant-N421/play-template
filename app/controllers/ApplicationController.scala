package controllers

import models.{APIError, DataModel}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.{LibraryService, RepositoryService}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ApplicationController @Inject() (
    val controllerComponents: ControllerComponents,
    val service: LibraryService,
    val repositoryService: RepositoryService
)(implicit val ec: ExecutionContext)
    extends BaseController {

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    repositoryService.create(request).map {
      case Right(book) => Created(Json.toJson(book))
      case Left(error) => BadRequest(Json.toJson(error))
    }
  }

  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    repositoryService.update(id, request) match {
      case Right(book) => Future(Accepted(Json.toJson(book)))
      case Left(error) => Future(BadRequest(Json.toJson(error)))
    }
  }

  def partialUpdate[T](id: String, field: String, value: T): Action[JsValue] =
    Action.async(parse.json) { implicit request =>
      repositoryService.partialUpdate(id, field, value, request).map {
        case Right(book) => (Accepted(Json.toJson(book)))
        case Left(error) => (BadRequest(Json.toJson(error)))
      }
    }

  def index(): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.index().flatMap {
      case Right(item: Seq[DataModel]) => Future(Ok(Json.toJson(item)))
      case Left(error: APIError.BadAPIResponse) => Future(BadRequest(Json.toJson(error)))
    }
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.read(id).map {
      case Right(book: DataModel) => Ok(Json.toJson(book))
      case Left(error) => BadRequest(Json.toJson(error))
    }
  }

  def readAny[T](field: String, value: T): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.readAny(field, value).map {
      case Right(book: DataModel) => Ok(Json.toJson(book))
      case Left(error) => BadRequest(Json.toJson(error))
    }
  }

  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.delete(id: String).map {
      case Right(message) => Accepted(Json.toJson(message))
      case Left(error) => BadRequest(Json.toJson(error))
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
