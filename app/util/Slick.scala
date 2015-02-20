package util

/**
 * Created by lucas on 16/02/15.
 */

import play.api.db.DB
import play.api.Play.current
import scala.slick.jdbc.JdbcBackend

object SlickDB {
  def withSession[A](f:(JdbcBackend.Session => A)):A = {
    val jdbcBackend = JdbcBackend.Database.forDataSource(DB.getDataSource()).createSession()
    val returnValue = f(jdbcBackend)
    jdbcBackend.close()
    returnValue
  }
}
  
