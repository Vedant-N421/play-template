package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import play.api.test._

import javax.inject.Inject

class ApplicationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController{
  def index(): Action[AnyContent] = Action {
    Ok("It ran!")
  }

  def create(id: String) = TODO
  def read(id: String) = TODO
  def update(id: String) = TODO
  def delete(id: String) = TODO

}
