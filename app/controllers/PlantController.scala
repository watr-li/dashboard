package controllers


import db.Tables._
import play.api.libs.Files.TemporaryFile
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import util.SlickDB

import scala.slick.driver.MySQLDriver.simple._

case class PlantData(name:String, picture:Int) {

}


/**
 * Created by lucas on 15/02/15.
 */
object PlantController extends Controller {

  val plantForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "picture" -> number
    )(PlantData.apply)(PlantData.unapply)
  )

  def index = Action {
    Ok(views.html.plant.create(plantForm))
  }



  def plantPost = Action(parse.multipartFormData) { implicit request =>
    val requestWithFile = handleFileUpload(request)._2

    plantForm.bindFromRequest()(requestWithFile).fold(
      formWithErrors => request.body.dataParts.get("picture") match {
        case Some(Seq(x)) =>  BadRequest(views.html.plant.create(formWithErrors)).withSession("picture" -> x)
        case _ => BadRequest(views.html.plant.create(formWithErrors))
      },

      plantData => {
        storePlant(plantData)
        Redirect("/")
      }
    )
  }




  def storePlant(data:PlantData):db.Tables.PlantsRow = SlickDB.withSession { implicit session =>
    val insertedId = (Plants returning Plants.map(_.id)) +=
      PlantsRow(0, data.name, None, Some(data.picture))

    Plants.filter(_.id === insertedId).list.head
  }


  /**
   * Takes a request MultiPartFormData request, moves the file to a new location,
   * writes the entry to the database and returns the augmented request with the
   * picture variable added. Not very generic, room for improvement :D
   * @param request The request with which the file was sent
   * @return A tuple consisting of the inserted file if it exists as well as the request,
   *         possibly augmented with the file id
   */
  def handleFileUpload(request:Request[MultipartFormData[TemporaryFile]]):(Option[FilesRow], Request[MultipartFormData[TemporaryFile]])  = {

    request.body.file("picture").map { picture =>
      import java.io.File
      val filename = picture.filename
      val contentType = picture.contentType

      val targetFile = s"pictures/$filename"
      val targetPath = s"${global.Global.uploadDirectory}/$targetFile"

      picture.ref.moveTo(new File(targetPath))

      val file = SlickDB.withSession { implicit session =>
        val insertedId = (Files returning Files.map(_.id)) +=
          FilesRow(0, targetFile, filename, contentType.get)

        Files.filter(_.id === insertedId).list.head
      }

      val requestWithFile = request.map { body =>
        MultipartFormData.apply(
          body.dataParts ++ Map("picture" -> Seq(file.id.toString)),
          body.files,
          body.badParts,
          body.missingFileParts
        )
      }

      (Some(file), requestWithFile)

    }.getOrElse {
      (None, request)
    }
  }


}
