package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

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
//    Request(request, request.bo);
    // Handle file upload
    request.body.file("picture").map { picture =>
      import java.io.File
      val filename = picture.filename
      val contentType = picture.contentType
      val targetFilename = s"/tmp/picture/$filename"
      picture.ref.moveTo(new File(targetFilename))
      
      val file = models.File.save(models.File(None, targetFilename))
      
      Ok("File uploaded " + file.id.get.toString)
    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Missing file")
    }
    //request.bo
    //val foo = plantForm.bindFromRequest.bind(Map("picture" -> "foolol"))
    
    
    
//    val r = request.map { body =>
//      MultipartFormData.apply(
//        body.dataParts ++ Map("picture" -> Seq("foolol123")),
//        body.files,
//        body.badParts,
//        body.missingFileParts
//      )
//    }
//
//
//    // Handle rest of form
//    plantForm.bindFromRequest()(r).fold(
//      formWithErrors => {
//        BadRequest(views.html.plant.create(formWithErrors))
//      },
//
//      plantData => {
//        Ok(plantData.picture)
//      }
//
//    )
    
  }

}
