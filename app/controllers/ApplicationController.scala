package controllers

import models.DataModel.dataForm
import models.{APIError, DataModel}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import play.filters.csrf.CSRF
import services.{LibraryService, RepositoryService}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ApplicationController @Inject()(
    val controllerComponents: ControllerComponents,
    val service: LibraryService,
    val repositoryService: RepositoryService
)(implicit val ec: ExecutionContext)
    extends BaseController
    with play.api.i18n.I18nSupport {

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    repositoryService.create(request).map {
      case Right(book) => Created(Json.toJson(book))
      case Left(error) => BadRequest(Json.toJson(error.reason))
    }
  }

  def example(id: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.read(id).map {
      case Right(book) => Ok(views.html.example(book))
      case Left(error) => BadRequest(Json.toJson(error.reason))
    }
  }

  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    repositoryService.update(id, request).map {
      case Right(book) => Accepted(Json.toJson(book))
      case Left(error) => BadRequest(Json.toJson(error.reason))
    }
  }

  def partialUpdate[T](id: String, field: String, value: T): Action[JsValue] =
    Action.async(parse.json) { implicit request =>
      repositoryService.partialUpdate(id, field, value).map {
        case Right(book) => Accepted(Json.toJson(book))
        case Left(error) => BadRequest(Json.toJson(error.reason))
      }
    }

  def index(): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.index().flatMap {
      case Right(item: Seq[DataModel]) => Future(Ok(Json.toJson(item)))
      case Left(error: APIError.BadAPIResponse) => Future(BadRequest(Json.toJson(error.reason)))
    }
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.read(id).map {
      case Right(book: DataModel) => Ok(Json.toJson(book))
      case Left(error) => BadRequest(Json.toJson(error.reason))
    }
  }

  def readAny[T](field: String, value: T): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.readAny(field, value).map {
      case Right(book: DataModel) => Ok(Json.toJson(book))
      case Left(error) => BadRequest(Json.toJson(error.reason))
    }
  }

  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.delete(id: String).map {
      case Right(message) => Accepted(Json.toJson(message))
      case Left(error) => BadRequest(Json.toJson(error.reason))
    }
  }

  def getGoogleBook(search: String, term: String): Action[AnyContent] = Action.async { implicit request =>
    service.getGoogleBook(search, term).value.flatMap {
      // Search for the books, add them to Mongo and get the first one to return "created"
      case Right(books) =>
        books.map(book => repositoryService.createGoogleBook(book)).head.map {
          case Right(x: DataModel) => Created(Json.toJson(x))
        }
      case Left(error) => Future(BadRequest(Json.toJson(error.reason)))
    }
  }

  private def accessToken(implicit request: Request[_]): Option[CSRF.Token] = {
    CSRF.getToken
  }

  def addNewBook(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.addnewbook(dataForm = DataModel.dataForm))
  }

  def addNewBookForm(): Action[AnyContent] = Action.async { implicit request =>
    accessToken // call the accessToken method
    dataForm
      .bindFromRequest()
      .fold(
        formWithErrors => {
          Future(BadRequest("Data validation error."))
        },
        formData => {
          repositoryService.createGoogleBook(formData).map {
            case Right(book) => Created(Json.toJson(book))
            case Left(error) => BadRequest(Json.toJson(error.reason))
          }
        }
      )
  }

}
