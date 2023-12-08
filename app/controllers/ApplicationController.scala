package controllers

import models.DataModel.dataForm
import models.{APIError, Book, DataModel}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import play.filters.csrf.CSRF
import services.{LibraryService, RepositoryService}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ApplicationController @Inject() (
    val controllerComponents: ControllerComponents,
    val service: LibraryService,
    val repositoryService: RepositoryService
)(implicit val ec: ExecutionContext)
    extends BaseController
    with play.api.i18n.I18nSupport {

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    repositoryService.create(request).map {
      case Right(book) => Created(Json.toJson(book))
      case Left(error) => BadRequest(Json.toJson(error))
    }
  }

  def example(id: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.read(id).map {
      case Right(book: DataModel) => Ok(views.html.example(book))
      case Left(err: String) =>
        BadRequest(views.html.example(DataModel("404", "404", "404", 404, "404")))
    }
  }

  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    repositoryService.update(id, request).map {
      case Right(book) => Accepted(Json.toJson(book))
      case Left(error) => BadRequest(Json.toJson(error))
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
      case Right(book: DataModel) =>
        Ok(Json.toJson(book))
      case Left(error) =>
        BadRequest(Json.toJson(error))
    }
  }

  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.delete(id: String).map {
      case Right(message) => Accepted(Json.toJson(message))
      case Left(error) => BadRequest(Json.toJson(error))
    }
  }

  def convertToDataModel(book: Book): DataModel = {
    DataModel(
      book.id,
      book.volumeInfo.title,
      book.volumeInfo.description.getOrElse("Empty description."),
      1,
      book.volumeInfo.industryIdentifiers.head.identifier
    )
  }

  def getGoogleBook(search: String, term: String): Action[AnyContent] = Action.async {
    implicit request =>
//      print("F1")
      service.getGoogleBook(search = search, term = term).value.flatMap {
        case Right(books: List[Book]) =>
          val bookList = books.map { book =>
            repositoryService.createGoogleBook(convertToDataModel(book))
          }
          bookList.head.map {
            case Right(book: DataModel) => Created(Json.toJson(book))
            case Left(err: String) => BadRequest(err)
          }
        case Left(error) =>
          Future(BadRequest(Json.toJson(error.reason)))
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
      .fold( // from the implicit request we want to bind this to the form in our companion object
        formWithErrors => {
          // here write what you want to do if the form has errors
          Future(BadRequest("ERROR: Data entered did not match requirements!"))
        },
        formData => {
          // here write how you would use this data to create a new book (DataModel)
          repositoryService.createGoogleBook(formData).map {
            case Right(book: DataModel) => Created(Json.toJson(book))
            case Left(err: String) => BadRequest(Json.toJson(err))
          }
        }
      )
  }

}
