
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
/*2.2*/import helper._

object addnewbook extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template3[Form[DataModel],RequestHeader,Messages,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*4.2*/(dataForm: Form[DataModel])(implicit request: RequestHeader, messages: Messages):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*5.1*/("""
"""),_display_(/*6.2*/main("Add Book")/*6.18*/{_display_(Seq[Any](format.raw/*6.19*/("""
  """),format.raw/*7.3*/("""<div>
  """),_display_(/*8.4*/helper/*8.10*/.form(action = routes.ApplicationController.addNewBookForm())/*8.71*/ {_display_(Seq[Any](format.raw/*8.73*/("""
    """),_display_(/*9.6*/helper/*9.12*/.CSRF.formField),format.raw/*9.27*/("""
    """),_display_(/*10.6*/helper/*10.12*/.inputText(dataForm("_id"))),format.raw/*10.39*/("""
    """),_display_(/*11.6*/helper/*11.12*/.inputText(dataForm("name"))),format.raw/*11.40*/("""
    """),_display_(/*12.6*/helper/*12.12*/.inputText(dataForm("description"))),format.raw/*12.47*/("""
    """),_display_(/*13.6*/helper/*13.12*/.inputText(dataForm("numSales"))),format.raw/*13.44*/("""
    """),_display_(/*14.6*/helper/*14.12*/.inputText(dataForm("isbn"))),format.raw/*14.40*/("""
    """),format.raw/*15.5*/("""<input class="submitButton" type="submit" value="Submit">
    """)))}),format.raw/*16.6*/("""
  """),format.raw/*17.3*/("""</div>
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
                  HASH: fa5db3d752cd879fa8ac9a7b02dc54301fe108ee
                  MATRIX: 432->1|463->26|820->44|994->125|1021->127|1045->143|1083->144|1112->147|1146->156|1160->162|1229->223|1268->225|1299->231|1313->237|1348->252|1380->258|1395->264|1443->291|1475->297|1490->303|1539->331|1571->337|1586->343|1642->378|1674->384|1689->390|1742->422|1774->428|1789->434|1838->462|1870->467|1963->530|1993->533
                  LINES: 17->1|18->2|23->4|28->5|29->6|29->6|29->6|30->7|31->8|31->8|31->8|31->8|32->9|32->9|32->9|33->10|33->10|33->10|34->11|34->11|34->11|35->12|35->12|35->12|36->13|36->13|36->13|37->14|37->14|37->14|38->15|39->16|40->17
                  -- GENERATED --
              */
          