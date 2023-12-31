// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:4
  HomeController_0: controllers.HomeController,
  // @LINE:7
  Assets_1: controllers.Assets,
  // @LINE:10
  ApplicationController_2: controllers.ApplicationController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:4
    HomeController_0: controllers.HomeController,
    // @LINE:7
    Assets_1: controllers.Assets,
    // @LINE:10
    ApplicationController_2: controllers.ApplicationController
  ) = this(errorHandler, HomeController_0, Assets_1, ApplicationController_2, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HomeController_0, Assets_1, ApplicationController_2, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.HomeController.index()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api""", """controllers.ApplicationController.index"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """create""", """controllers.ApplicationController.create()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """read/""" + "$" + """id<[^/]+>""", """controllers.ApplicationController.read(id:String)"""),
    ("""PUT""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """update/""" + "$" + """id<[^/]+>""", """controllers.ApplicationController.update(id:String)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """delete/""" + "$" + """id<[^/]+>""", """controllers.ApplicationController.delete(id:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """library/google/""" + "$" + """search<[^/]+>/""" + "$" + """term<[^/]+>""", """controllers.ApplicationController.getGoogleBook(search:String, term:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """example/""" + "$" + """id<[^/]+>""", """controllers.ApplicationController.example(id:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """addbook/form""", """controllers.ApplicationController.addNewBook()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """addbook/form""", """controllers.ApplicationController.addNewBookForm()"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:4
  private[this] lazy val controllers_HomeController_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_HomeController_index0_invoker = createInvoker(
    HomeController_0.index(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "index",
      Nil,
      "GET",
      this.prefix + """""",
      """ An example controller showing a sample home page""",
      Seq()
    )
  )

  // @LINE:7
  private[this] lazy val controllers_Assets_versioned1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned1_invoker = createInvoker(
    Assets_1.versioned(fakeValue[String], fakeValue[Asset]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """ Map static resources from the /public folder to the /assets URL path""",
      Seq()
    )
  )

  // @LINE:10
  private[this] lazy val controllers_ApplicationController_index2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api")))
  )
  private[this] lazy val controllers_ApplicationController_index2_invoker = createInvoker(
    ApplicationController_2.index,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "index",
      Nil,
      "GET",
      this.prefix + """api""",
      """ we need to add an app route that references the new controller and method""",
      Seq()
    )
  )

  // @LINE:12
  private[this] lazy val controllers_ApplicationController_create3_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("create")))
  )
  private[this] lazy val controllers_ApplicationController_create3_invoker = createInvoker(
    ApplicationController_2.create(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "create",
      Nil,
      "POST",
      this.prefix + """create""",
      """""",
      Seq()
    )
  )

  // @LINE:13
  private[this] lazy val controllers_ApplicationController_read4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("read/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ApplicationController_read4_invoker = createInvoker(
    ApplicationController_2.read(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "read",
      Seq(classOf[String]),
      "GET",
      this.prefix + """read/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:14
  private[this] lazy val controllers_ApplicationController_update5_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("update/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ApplicationController_update5_invoker = createInvoker(
    ApplicationController_2.update(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "update",
      Seq(classOf[String]),
      "PUT",
      this.prefix + """update/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:15
  private[this] lazy val controllers_ApplicationController_delete6_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("delete/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ApplicationController_delete6_invoker = createInvoker(
    ApplicationController_2.delete(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "delete",
      Seq(classOf[String]),
      "DELETE",
      this.prefix + """delete/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:17
  private[this] lazy val controllers_ApplicationController_getGoogleBook7_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("library/google/"), DynamicPart("search", """[^/]+""",true), StaticPart("/"), DynamicPart("term", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ApplicationController_getGoogleBook7_invoker = createInvoker(
    ApplicationController_2.getGoogleBook(fakeValue[String], fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "getGoogleBook",
      Seq(classOf[String], classOf[String]),
      "GET",
      this.prefix + """library/google/""" + "$" + """search<[^/]+>/""" + "$" + """term<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:19
  private[this] lazy val controllers_ApplicationController_example8_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("example/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ApplicationController_example8_invoker = createInvoker(
    ApplicationController_2.example(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "example",
      Seq(classOf[String]),
      "GET",
      this.prefix + """example/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:21
  private[this] lazy val controllers_ApplicationController_addNewBook9_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("addbook/form")))
  )
  private[this] lazy val controllers_ApplicationController_addNewBook9_invoker = createInvoker(
    ApplicationController_2.addNewBook(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "addNewBook",
      Nil,
      "GET",
      this.prefix + """addbook/form""",
      """""",
      Seq()
    )
  )

  // @LINE:22
  private[this] lazy val controllers_ApplicationController_addNewBookForm10_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("addbook/form")))
  )
  private[this] lazy val controllers_ApplicationController_addNewBookForm10_invoker = createInvoker(
    ApplicationController_2.addNewBookForm(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "addNewBookForm",
      Nil,
      "POST",
      this.prefix + """addbook/form""",
      """""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:4
    case controllers_HomeController_index0_route(params@_) =>
      call { 
        controllers_HomeController_index0_invoker.call(HomeController_0.index())
      }
  
    // @LINE:7
    case controllers_Assets_versioned1_route(params@_) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned1_invoker.call(Assets_1.versioned(path, file))
      }
  
    // @LINE:10
    case controllers_ApplicationController_index2_route(params@_) =>
      call { 
        controllers_ApplicationController_index2_invoker.call(ApplicationController_2.index)
      }
  
    // @LINE:12
    case controllers_ApplicationController_create3_route(params@_) =>
      call { 
        controllers_ApplicationController_create3_invoker.call(ApplicationController_2.create())
      }
  
    // @LINE:13
    case controllers_ApplicationController_read4_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        controllers_ApplicationController_read4_invoker.call(ApplicationController_2.read(id))
      }
  
    // @LINE:14
    case controllers_ApplicationController_update5_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        controllers_ApplicationController_update5_invoker.call(ApplicationController_2.update(id))
      }
  
    // @LINE:15
    case controllers_ApplicationController_delete6_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        controllers_ApplicationController_delete6_invoker.call(ApplicationController_2.delete(id))
      }
  
    // @LINE:17
    case controllers_ApplicationController_getGoogleBook7_route(params@_) =>
      call(params.fromPath[String]("search", None), params.fromPath[String]("term", None)) { (search, term) =>
        controllers_ApplicationController_getGoogleBook7_invoker.call(ApplicationController_2.getGoogleBook(search, term))
      }
  
    // @LINE:19
    case controllers_ApplicationController_example8_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        controllers_ApplicationController_example8_invoker.call(ApplicationController_2.example(id))
      }
  
    // @LINE:21
    case controllers_ApplicationController_addNewBook9_route(params@_) =>
      call { 
        controllers_ApplicationController_addNewBook9_invoker.call(ApplicationController_2.addNewBook())
      }
  
    // @LINE:22
    case controllers_ApplicationController_addNewBookForm10_route(params@_) =>
      call { 
        controllers_ApplicationController_addNewBookForm10_invoker.call(ApplicationController_2.addNewBookForm())
      }
  }
}
