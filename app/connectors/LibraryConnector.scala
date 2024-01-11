package connectors

import cats.data.EitherT
import models._
import play.api.libs.json.{JsArray, JsValue, OFormat}
import play.api.libs.ws.{WSClient, WSResponse}

import javax.inject.Inject
import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}

class LibraryConnector @Inject()(ws: WSClient) {

  def get[Response](
      url: String
  )(implicit
    rds: OFormat[Response],
    ec: ExecutionContext): EitherT[Future, APIError, List[Book]] = {

//    Had to do it to 'em, an unnecessary piece of code below :)
    @tailrec
    def read_all_books(res: JsValue, accum: List[Book], count: Int): List[Book] = {
      count match {
        case count if count == res.as[JsArray].value.size => accum
        case _ =>
          read_all_books(
            res,
            accum :+ res(count).as[Book],
            count + 1
          )
      }
    }

    val request = ws.url(url)
    val response = request.get()

    EitherT {
      response
        .map { result =>
//          Right(read_all_books(result.json("items"), List(), 0))
          Right(result.json("items").as[List[Book]])
        }
        .recover {
          case _: WSResponse =>
            (Left(APIError.BadAPIResponse(500, "Could not connect")))
        }
    }

  }
}
