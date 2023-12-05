
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
  <b>"""),_display_(/*4.7*/dataModel/*4.16*/._id),format.raw/*4.20*/(""", """),_display_(/*4.23*/dataModel/*4.32*/.name),format.raw/*4.37*/(""", """),_display_(/*4.40*/dataModel/*4.49*/.description),format.raw/*4.61*/(""", """),_display_(/*4.64*/dataModel/*4.73*/.isbn),format.raw/*4.78*/("""</b>
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
                  HASH: fb356bddac13bd15904d84f05a37fd13b0696aa8
                  MATRIX: 734->1|850->25|875->42|913->43|944->48|1008->87|1025->96|1049->100|1078->103|1095->112|1120->117|1149->120|1166->129|1198->141|1227->144|1244->153|1269->158
                  LINES: 21->1|26->2|26->2|26->2|27->3|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4|28->4
                  -- GENERATED --
              */
          