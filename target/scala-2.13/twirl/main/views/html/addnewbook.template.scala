
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
/*1.2*/import models.DataModel

object addnewbook extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template3[Form[DataModel],RequestHeader,Messages,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*3.2*/(dataForm: Form[DataModel])(implicit request: RequestHeader, messages: Messages):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*4.1*/("""
"""),_display_(/*5.2*/main("Add Book")/*5.18*/{_display_(Seq[Any](format.raw/*5.19*/("""
  """),format.raw/*6.3*/("""<div>
  """),_display_(/*7.4*/helper/*7.10*/.form(action = routes.ApplicationController.addNewBookForm())/*7.71*/ {_display_(Seq[Any](format.raw/*7.73*/("""
    """),_display_(/*8.6*/helper/*8.12*/.CSRF.formField),format.raw/*8.27*/("""
    """),_display_(/*9.6*/helper/*9.12*/.inputText(dataForm("_id"))),format.raw/*9.39*/("""
    """),_display_(/*10.6*/helper/*10.12*/.inputText(dataForm("name"))),format.raw/*10.40*/("""
    """),_display_(/*11.6*/helper/*11.12*/.inputText(dataForm("description"))),format.raw/*11.47*/("""
    """),_display_(/*12.6*/helper/*12.12*/.inputText(dataForm("numSales"))),format.raw/*12.44*/("""
    """),_display_(/*13.6*/helper/*13.12*/.inputText(dataForm("isbn"))),format.raw/*13.40*/("""
    """),format.raw/*14.5*/("""<input class="submitButton" type="submit" value="Submit">
    """)))}),format.raw/*15.6*/("""
  """),format.raw/*16.3*/("""</div>
""")))}))
      }
    }
  }

  def render(dataForm:Form[DataModel],request:RequestHeader,messages:Messages): play.twirl.api.HtmlFormat.Appendable = apply(dataForm)(request,messages)

  def f:((Form[DataModel]) => (RequestHeader,Messages) => play.twirl.api.HtmlFormat.Appendable) = (dataForm) => (request,messages) => apply(dataForm)(request,messages)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/addnewbook.scala.html
                  HASH: 99697f2af1ec00432a4411859f45fc931bd1fb51
                  MATRIX: 432->1|797->27|971->108|998->110|1022->126|1060->127|1089->130|1123->139|1137->145|1206->206|1245->208|1276->214|1290->220|1325->235|1356->241|1370->247|1417->274|1449->280|1464->286|1513->314|1545->320|1560->326|1616->361|1648->367|1663->373|1716->405|1748->411|1763->417|1812->445|1844->450|1937->513|1967->516
                  LINES: 17->1|22->3|27->4|28->5|28->5|28->5|29->6|30->7|30->7|30->7|30->7|31->8|31->8|31->8|32->9|32->9|32->9|33->10|33->10|33->10|34->11|34->11|34->11|35->12|35->12|35->12|36->13|36->13|36->13|37->14|38->15|39->16
                  -- GENERATED --
              */
          