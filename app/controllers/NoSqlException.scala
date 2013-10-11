package controllers

import play.api.http.Status


/**
 * @author Karel Maesen, Geovise BVBA
 *         creation-date: 10/10/13
 */
package object Exceptions {

  abstract class NoSqlException(msg: String) extends RuntimeException(msg)

  case class DatabaseNotFoundException(msg: String = "") extends NoSqlException(msg)

  case class CollectionNotFoundException(msg: String = "") extends NoSqlException(msg)

  case class NoSpatialMetadataException(msg: String = "") extends NoSqlException(msg)

  case class DatabaseAlreadyExists(msg: String = "") extends NoSqlException(msg)

  case class CollectionAlreadyExists(msg: String ="") extends NoSqlException(msg)

  case class DatabaseCreationException(msg: String = "") extends NoSqlException(msg)

  case class DatabaseDeleteException(msg: String= "") extends NoSqlException(msg)

  case class InvalidQueryException(msg: String = "") extends NoSqlException(msg)

}
