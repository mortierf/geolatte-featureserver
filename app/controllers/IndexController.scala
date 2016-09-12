package controllers

import javax.inject.Inject

import config.AppExecutionContexts
import controllers.Formats._
import featureserver.Repository
import play.api.Logger
import play.api.libs.json.{ JsError, JsSuccess }
import play.api.mvc.{ BodyParsers, Result }

import scala.concurrent.Future

class IndexController @Inject() (val repository: Repository) extends AbstractFeatureServerSqlController {

  import AppExecutionContexts._

  def put(db: String, collection: String, indexName: String) = repositoryAction(BodyParsers.parse.tolerantJson) {
    implicit req =>
      {
        req.body.validate[IndexDef] match {
          case JsError(er) => {
            Logger.warn(er.mkString("\n"))
            Future.successful(BadRequest("Invalid index definition: " + er.mkString("\n")))
          }
          case JsSuccess(indexDef, _) => {
            repository.createIndex(db, collection, indexDef.copy(name = indexName))
              .map[Result](_ =>
                Created.withHeaders("Location" -> controllers.routes.ViewController.get(db, collection, indexName).url)).recover(commonExceptionHandler(db, collection))
          }
        }
      }
  }

  def list(db: String, collection: String) = repositoryAction {
    implicit req =>
      {
        repository.getIndices(db, collection).map[Result] { indexNames => IndexDefsResource(db, collection, indexNames) }
      }.recover(commonExceptionHandler(db, collection))
  }

  def get(db: String, collection: String, index: String) = repositoryAction {
    implicit req =>
      {
        repository.getIndex(db, collection, index).map[Result] { indexDef => IndexDefResource(db, collection, indexDef) }
      }.recover(commonExceptionHandler(db, collection))
  }

  def delete(db: String, collection: String, index: String) = repositoryAction {
    implicit req =>
      {
        repository.dropIndex(db, collection, index).map[Result] { _ => Ok }
      }.recover(commonExceptionHandler(db, collection))
  }

}