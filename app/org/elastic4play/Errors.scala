package org.elastic4play

import play.api.libs.json.{ JsObject, JsValue }

import org.elastic4play.controllers.InputValue

case class BadRequestError(message: String) extends Exception(message)
case class CreateError(status: Option[String], message: String, attributes: JsObject) extends Exception(message)
case class ConflictError(message: String, attributes: JsObject) extends Exception(message)
case class NotFoundError(message: String) extends Exception(message)
case class GetError(message: String) extends Exception(message)
case class UpdateError(status: Option[String], message: String, attributes: JsObject) extends Exception(message)
case class InternalError(message: String) extends Exception(message)
case class SearchError(message: String, cause: Throwable) extends Exception(message, cause)
case class AuthenticationError(message: String) extends Exception(message)
case class AuthorizationError(message: String) extends Exception(message)
case class MultiError(message: String, exceptions: Seq[Exception]) extends Exception(message + exceptions.map(_.getMessage).mkString(" :\n\t- ", "\n\t- ", ""))

case class AttributeCheckingError(
  tableName: String,
  errors: Seq[AttributeError] = Nil)
    extends Exception(errors.mkString("[", "][", "]")) {
  override def toString = errors.mkString("[", "][", "]")
}

sealed trait AttributeError extends Throwable {
  def withName(name: String): AttributeError
  val name: String
}

case class InvalidFormatAttributeError(name: String, format: String, value: InputValue) extends AttributeError {
  override def toString = s"Invalid format for $name: $value, expected $format"
  override def withName(newName: String) = copy(name = newName)
}
case class UnknownAttributeError(name: String, value: JsValue) extends AttributeError {
  override def toString = s"Unknown attribute $name: $value"
  override def withName(newName: String) = copy(name = newName)
}
case class UpdateReadOnlyAttributeError(name: String) extends AttributeError {
  override def toString = s"Attribute $name is read-only"
  override def withName(newName: String) = copy(name = newName)
}
case class MissingAttributeError(name: String) extends AttributeError {
  override def toString = s"Attribute $name is missing"
  override def withName(newName: String) = copy(name = newName)
}