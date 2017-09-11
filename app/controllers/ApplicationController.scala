package controllers
    import javax.inject.Inject
    import model.{Station, StationForm}
    import play.api.libs.json.{JsValue, Json}
    import play.api.libs.ws.{WSClient, WSResponse}
    import play.api.mvc._
    import scala.concurrent.{Await, Future}
    import service.StationService
    import scala.concurrent.ExecutionContext.Implicits.global
    import play.api.libs.ws.WSResponse
    import scala.concurrent._
    import ExecutionContext.Implicits.global
    import scala.concurrent.duration.{Duration, MILLISECONDS}
    import play.api.libs.json.Json
    import scala.sys.process._

    class ApplicationController @Inject()(WS: WSClient) extends Controller {

      def index(time: String ,week: Int,direction:Int) = Action.async { implicit request =>
        StationService.listAllStations map { stations =>
          val processString = s"curl -x 195.222.68.23:3128 -L https://rp.cloudrail.jp/tw02/jreast_app/fb/feedback/feedback/?accessTime=$time&accessDayCd=$week&direction=$direction"
          def dirList = Process(Seq("sh", "-c", processString)).lines.mkString
          Thread.sleep(3000)
          val trainCongestions = (Json.parse(dirList) \ "trainFeedbackList").as[List[JsValue]]
          Ok(views.html.index(trainCongestions, StationForm.form,  stations,  time, week, direction))
          //Ok(views.html.index("te"))
        }
      }
      /*
      def index(time: String ,week: Int,direction:Int) = Action.async { implicit request =>
        StationService.listAllStations map { stations =>
          val response: WSResponse = Await.result(WS.url(s"https://rp.cloudrail.jp/tw02/jreast_app/fb/feedback/feedback/?accessTime=$time&accessDayCd=$week&direction=$direction").get(), Duration(2000, MILLISECONDS))
          val trainCongestions = (Json.parse(response.body) \ "trainFeedbackList").as[List[JsValue]]
          Ok(views.html.index(trainCongestions, StationForm.form,  stations,  time, week, direction))
        }
      }
      */
    }

