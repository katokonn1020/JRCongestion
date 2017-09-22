package controllers
    import javax.inject.Inject
    import model.{Station, StationForm}
    import play.api.libs.json.{JsValue, Json}
    import play.api.libs.ws.{WSClient, WSResponse}
    import play.api.mvc._
    import service.StationService
    import scala.concurrent._
    import ExecutionContext.Implicits.global
    import play.api.libs.json.Json
    import scala.sys.process._

    class ApplicationController @Inject()(WS: WSClient) extends Controller {
      def index(time: String ,week: String,direction:String) = Action.async { implicit request =>
        StationService.listAllStations map { stations =>
          val processString = s"""curl -x 115.36.15.26:3128 -L "https://rp.cloudrail.jp/tw02/jreast_app/fb/feedback/feedback/?accessTime=$time&accessDayCd=$week&direction=$direction""""
          def dirList = Process(Seq("sh", "-c", processString)).lines.mkString
          val trainCongestions = (Json.parse(dirList) \ "trainFeedbackList").as[List[JsValue]]
          Ok(views.html.index(trainCongestions, StationForm.form,  stations,  time, week, direction))
          //Ok(views.html.index("test"))
        }
      }
    }

