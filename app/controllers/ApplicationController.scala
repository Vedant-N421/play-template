package controllers

import org.mongodb.scala._
import models.DataModel
import org.mongodb.scala.result.DeleteResult
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repositories.DataRepository

import javax.inject.Inject
import scala.concurrent.Future

class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val dataRepository: DataRepository) extends BaseController{

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        dataRepository.create(dataModel).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def update(id: String): Action[JsValue] = Action.async(parse.json){ implicit request =>
    request.body.validate[DataModel] match{
      case JsSuccess(dataModel: DataModel, _) =>
        dataRepository.update(id=id, book=dataModel).map(_ => Accepted)
      case JsError(_) => Future(BadRequest)
    }
  }

  //  def index(): Action[AnyContent] = Action {
  //    Ok("It ran!")
  //  }

  def index(): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.index().map {
      case Right(item: Seq[DataModel]) => Ok {
        Json.toJson(item)
      }
      case Left(error) => Status(error)(Json.toJson("Unable to find any books"))
    }
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.read(id: String).map {
      case item: DataModel => Ok {
        Json.toJson(item)
      }
      case _ => NotFound{Status(_)(Json.toJson("Unable to read book"))}
    }
  }

  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.delete(id: String).map {
      case res: DeleteResult => Accepted
      case _ => NotFound{Status(_)(Json.toJson("Unable to delete book"))}
    }
  }
}