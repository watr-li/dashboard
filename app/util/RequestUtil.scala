package util

import play.api.mvc.MultipartFormData
import play.api.mvc.Request

/**
 * Created by lucas on 21/02/15.
 */

object RequestUtil {
  
  def addDataParts[T](request:Request[MultipartFormData[T]], dataParts:Map[String, Seq[String]]):Request[MultipartFormData[T]] = {

    request.map { body =>
      body.copy(
        body.dataParts ++ dataParts,
        body.files,
        body.badParts,
        body.missingFileParts
      )
    }
  }
  
}