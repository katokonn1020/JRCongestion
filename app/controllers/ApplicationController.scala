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
          def dirList = Process(Seq("sh", "-c", "curl -x 160.16.119.186:8118 -L \"https://rp.cloudrail.jp/tw02/jreast_app/fb/feedback/feedback/?at=1654&accessTime=1230&accessDayCd=7&direction=0&_=1499399710095\"")).lines.toList.toString()
          val response: WSResponse = Await.result(WS.url(s"http://taruo.net/e/").get(), Duration(2000, MILLISECONDS))
          //val trainCongestions = (Json.parse(response.body) \ "trainFeedbackList").as[List[JsValue]]
          Ok(views.html.index(dirList))
          //Ok(views.html.index(trainCongestions, StationForm.form,  stations,  time, week, direction))
        }
      }


      /*
      def stationSelect(time: String,week: Int,direction:Int) = Action.async { implicit request =>
          StationForm.form.bindFromRequest.fold(
            // if any error in submitted data
            errorForm:temp => Future.successful(Ok(views.html.error())),
            data => {
              val response: WSResponse = Await.result(WS.url(s"https://rp.cloudrail.jp/tw02/jreast_app/fb/feedback/feedback/?accessTime=$time&accessDayCd=$week&direction=$direction").get(), Duration(2000, MILLISECONDS))
              val trainCongestions = (Json.parse(response.body) \ "trainFeedbackList").as[List[JsValue]]
              Redirect(routes.ApplicationController.index("0600",3,0))
            })
        }
*/
    }

