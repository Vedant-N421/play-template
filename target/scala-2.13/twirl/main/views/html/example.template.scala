
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
    """),format.raw/*3.5*/("""<h1>Google Book API Project</h1>
  <b>Book ID: """),_display_(/*4.16*/dataModel/*4.25*/._id),format.raw/*4.29*/(""" """),format.raw/*4.30*/("""<br><br>Book name: """),_display_(/*4.50*/dataModel/*4.59*/.name),format.raw/*4.64*/(""" """),format.raw/*4.65*/("""<br><br>Book description: """),_display_(/*4.92*/dataModel/*4.101*/.description),format.raw/*4.113*/(""" """),format.raw/*4.114*/("""<br><br>Book ISBN: """),_display_(/*4.134*/dataModel/*4.143*/.isbn),format.raw/*4.148*/(""" """),format.raw/*4.149*/("""<br><br></b>
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
                  HASH: 4793b39a094c577ca43d71b3358ce5347a73c27a
                  MATRIX: 734->1|850->25|875->42|913->43|944->48|1018->96|1035->105|1059->109|1087->110|1133->130|1150->139|1175->144|1203->145|1256->172|1274->181|1307->193|1336->194|1383->214|1401->223|1427->228|1456->229
                  LINES: 21->1|26->2|26->2|26->2|27->3|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4
                  -- GENERATED --
              */
          