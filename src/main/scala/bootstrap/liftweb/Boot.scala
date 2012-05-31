package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import sitemap._
import Loc._
import mapper._

import de.codecarving.model.Participant
import de.codecarving.model.User
import de.codecarving.lib.HelperMethods.CurrentDownload

//import de.codecarving.fhsldap.fhsldap
//import de.codecarving.fhsldap.model.User

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    
    //SiteMap
    val ifLoggedIn =
      If(() => User.loggedIn_?, () => RedirectResponse(User.loginPageURL))
    val homeMenu = Menu("Home") / "index"
    val auswertungMenu = Menu("Auswertung") / "eval" >> ifLoggedIn
    val menus = homeMenu :: auswertungMenu :: User.menus
    LiftRules.setSiteMap(SiteMap(menus: _*))
    
    // where to search snippet
    LiftRules.addToPackages("de.codecarving")

    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
      	  							//Props.get("db.url") openOr "jdbc:h2:lift_SAPAlloc.db;AUTO_SERVER=TRUE",
    		  							Props.get("db.url") openOr "jdbc:h2:lift_TEST.db;AUTO_SERVER=TRUE",
    		  							Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)

      Schemifier.schemify(true, Schemifier.infoF _, Participant)
      Schemifier.schemify(true, Schemifier.infoF _, User)
    }

    // Creating a LocParam 
    // val loggedInAdmin = If(() => User.isAdmin, () => RedirectResponse("/index"))
    
    
    // Build SiteMap
    // def sitemap(): SiteMap = SiteMap(
    //  Menu.i("Home") / "index",
    //  Menu.i("Anmeldung") / "login",
    //  Menu.i("Auswertung") / "eval" // >> loggedInAdmin
    // )

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    // LiftRules.setSiteMapFunc(() => sitemap())

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))
      
    // Set max file upload to 200mb
    LiftRules.maxMimeSize = 200 * 1024 * 1024
    LiftRules.maxMimeFileSize = 200 * 1024 * 1024   
    
    // Dispatching /download for file Downloads.
    LiftRules.dispatch.append {
      case Req("download" :: _, _, GetRequest) =>
        () => CurrentDownload
    }
    
    // Initalizing FhS-LDAP-Module for fhs-id login.
    // fhsldap.init
      
  }
} 