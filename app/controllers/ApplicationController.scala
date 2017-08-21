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

    class ApplicationController @Inject()(WS: WSClient) extends Controller {
      def index(time: String ,week: Int,direction:Int) = Action.async { implicit request =>
        StationService.listAllStations map { stations =>
          val response: WSResponse = Await.result(WS.url(s"https://rp.cloudrail.jp/tw02/jreast_app/fb/feedback/feedback/?accessTime=$time&accessDayCd=$week&direction=$direction").get(), Duration(5000, MILLISECONDS))
          val trainCongestions = (Json.parse(response.body) \ "trainFeedbackList").as[List[JsValue]]
          //Ok(views.html.index(trainCongestions, StationForm.form,  stations,  time, week, direction))
          Ok(views.html.index("OK"))
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

