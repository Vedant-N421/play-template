
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

object example extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template1[DataModel,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(dataModel: DataModel):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](_display_(/*2.2*/main("Find Book")/*2.19*/{_display_(Seq[Any](format.raw/*2.20*/("""



    """),format.raw/*6.5*/("""<h1>Google Book API Project</h1>
  <b>Book ID: """),_display_(/*7.16*/dataModel/*7.25*/._id),format.raw/*7.29*/(""" """),format.raw/*7.30*/("""<br><br>Book name: """),_display_(/*7.50*/dataModel/*7.59*/.name),format.raw/*7.64*/(""" """),format.raw/*7.65*/("""<br><br>Book description: """),_display_(/*7.92*/dataModel/*7.101*/.description),format.raw/*7.113*/(""" """),format.raw/*7.114*/("""<br><br>Book ISBN: """),_display_(/*7.134*/dataModel/*7.143*/.isbn),format.raw/*7.148*/(""" """),format.raw/*7.149*/("""<br><br></b>
""")))}))
      }
    }
  }

  def render(dataModel:DataModel): play.twirl.api.HtmlFormat.Appendable = apply(dataModel)

  def f:((DataModel) => play.twirl.api.HtmlFormat.Appendable) = (dataModel) => apply(dataModel)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/example.scala.html
                  HASH: 4caec9428629088b5f9cd9cb40013852eac907e4
                  MATRIX: 734->1|850->25|875->42|913->43|947->51|1021->99|1038->108|1062->112|1090->113|1136->133|1153->142|1178->147|1206->148|1259->175|1277->184|1310->196|1339->197|1386->217|1404->226|1430->231|1459->232
                  LINES: 21->1|26->2|26->2|26->2|30->6|31->7|31->7|31->7|31->7|31->7|31->7|31->7|31->7|31->7|31->7|31->7|31->7|31->7|31->7|31->7|31->7
                  -- GENERATED --
              */
          