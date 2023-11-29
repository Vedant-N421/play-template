package models

import play.api.http.Status
import play.api.libs.json.{Json, Writes}

sealed abstract class APIError(val httpResponseStatus: Int, val reason: String)

object APIError {
  implicit val writes: Writes[BadAPIResponse] = Json.writes[BadAPIResponse]

  final case class BadAPIResponse(upstreamStatus: Int, upstreamMessage: String)
      extends APIError(
        Status.INTERNAL_SERVER_ERROR,
        s"Bad response from upstream; got status: $upstreamStatus, and got reason $upstreamMessage"
      )
}
