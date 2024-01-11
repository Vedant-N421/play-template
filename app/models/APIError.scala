package models

import play.api.http.Status
import play.api.libs.json.{Json, Writes}

sealed abstract class APIError(val httpResponseStatus: Int, val reason: String)

object APIError {
  implicit val writes: Writes[BadAPIResponse] = Json.writes[BadAPIResponse]
  implicit val writesConflictError: Writes[ConflictError] = Json.writes[ConflictError]
  implicit val writesResourceNotFound: Writes[ResourceNotFound] = Json.writes[ResourceNotFound]
  implicit val writesBadRequestError: Writes[BadRequestError] = Json.writes[BadRequestError]
  implicit val writesNoContentError: Writes[NoContentError] = Json.writes[NoContentError]

  final case class BadAPIResponse(upstreamStatus: Int, upstreamMessage: String)
      extends APIError(
        Status.INTERNAL_SERVER_ERROR,
        s"Bad response from upstream; got status: $upstreamStatus, and got reason $upstreamMessage"
      )

  final case class ConflictError(info: String = "N/A")
      extends APIError(Status.CONFLICT,
                       s"Conflict error due to current state of target resource. Additional info: $info")

  final case class ResourceNotFound(info: String = "N/A")
      extends APIError(Status.NOT_FOUND, s"Server cannot locate the requested resource. Additional info: $info")

  final case class BadRequestError(info: String = "N/A")
      extends APIError(Status.BAD_REQUEST, s"Data validation error. Additional info: $info")

  final case class NoContentError(info: String = "N/A")
      extends APIError(Status.NO_CONTENT,
                       s"Request processed successfully but no change was made. Additional info: $info")
}
