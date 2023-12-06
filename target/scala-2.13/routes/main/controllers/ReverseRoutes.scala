// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:4
package controllers {

  // @LINE:4
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:4
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
  }

  // @LINE:7
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:7
    def versioned(file:Asset): Call = {
      implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }

  // @LINE:10
  class ReverseApplicationController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:13
    def read(id:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "read/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
    // @LINE:12
    def create(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "create")
    }
  
    // @LINE:17
    def getGoogleBook(search:String, term:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "library/google/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("search", search)) + "/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("term", term)))
    }
  
    // @LINE:22
    def addNewBookForm(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "addbook/form")
    }
  
    // @LINE:21
    def addNewBook(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "addbook/form")
    }
  
    // @LINE:15
    def delete(id:String): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "delete/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
    // @LINE:19
    def example(id:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "example/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
    // @LINE:14
    def update(id:String): Call = {
      
      Call("PUT", _prefix + { _defaultPrefix } + "update/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
    // @LINE:10
    def index: Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api")
    }
  
  }


}
