package controllers

import org.mongodb.scala._
import models.DataModel
import org.mongodb.scala.result.DeleteResult
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repositories.DataRepository

import javax.inject.Inject
import scala.concurrent.Future

class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val dataRepository: DataRepository) extends BaseController{

  def create(id: String) = TODO

  def update(id: String) = TODO

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

  def read(id: String): Action[AnyContent] = dataRepository.read(id: String).map{
    case item: DataModel => Ok{
      Json.toJson(item)
    }
    case _ => Status(_)(Json.toJson("Unable to read book"))
  }

  def delete(id: String): Future[Object] =
    dataRepository.delete(id: String).map{
      case result: DeleteResult => Accepted
      case _ => Status(_)(Json.toJson("Unable to delete book"))
    }






}
