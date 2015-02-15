package models

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by lucas on 15/02/15.
 */
case class File(id:Option[Long], path:String, name:String, contentType:String)

object File {
  
  def save(f:File):File = {
    if(f.id.isEmpty) {
      insert(f)
    } else {
      update(f)
    }
  }
  
  def find(id:Int):Option[File] = {
    DB.withConnection { implicit c =>
      SQL("SELECT * FROM files WHERE id = {id}")
        .on('id -> id)
        .apply().headOption match {

        case Some(row) =>
          Some(File(
            Some(row[Long]("id")),
              row[String]("path"),
              row[String]("name"),
              row[String]("contentType")
          ))

        case _ => None

      }
    }
  }





  protected def update(f:File):File = {
    DB.withConnection { implicit c =>
      SQL("UPDATE files SET path = {path} WHERE id = {id}")
        .on('id -> f.id.get, 'path -> f.path)
        .executeUpdate()

      f
    }
  }


  protected def insert(f:File):File = {
    DB.withConnection { implicit c =>
      val id = SQL("INSERT INTO files(path) VALUES ({path})")
        .on('path -> f.path)
        .executeInsert()

      f.copy(id)
    }
  }
}
