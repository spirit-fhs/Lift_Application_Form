package de.codecarving
package snippet

import model.Participant
import net.liftweb.util.BindHelpers._
import net.liftweb.http.S
import net.liftweb.common.Full
import net.liftweb.mapper.MapperRules
import net.liftweb.http.{ SHtml, FileParamHolder, SessionVar }

class Form {
  
  private val mimeTypes = List("image/jpeg", "image/pjpeg", "image/png", "image/gif", "application/pdf")
  
  object p extends SessionVar[Participant](Participant.create)
  
  def validateForm = {

    val EmailParser = """([\w\d\-\_\.]+)(\+\d+)?@([\w\d\-\.]+)""".r
    
    def validate = p.status.get match {
      case "Dozent" => !(List(p.firstname, p.lastname, p.zipcode, p.city, p.street, 
           p.email, p.university, p.studycourse) contains "")
      case "Student" => !(List(p.firstname, p.lastname, p.zipcode, p.city, p.street, 
           p.email, p.university, p.studycourse, p.matrnr, p.semester, p.filename) contains "")
    }
           
    def validateEmail = p.email.get match {
      case EmailParser(_,_,_) => true
      case _ => false
    }
    
    (validate, validateEmail)
   
  }
  
  def process() = {
     validateForm match {
        case (false, _) =>
          S.error("Bitte alle Felder ausfüllen und als Student eine Studienbescheinigung beifügen!")
          S.redirectTo("/")
        case (_, false) =>
          S.error("Bitte eine valide E-Mail-Adresse angeben!")
          S.redirectTo("/")
        case _ =>
     }
     
     p.saveMe()
     p.remove()
     
     S.notice("Anmeldung erfolgreich!")
     S.redirectTo("/")
     
  }
  
  private def saveFile(fp: FileParamHolder): Unit = {
    
    fp match {                
      case x if x.file.size > 3000000 =>
        println(x.file.size)
        S.error("Ihre Studienbescheinigung ist zu Groß! Max. 3 Megabyte!")
        S.redirectTo("/")
      case x if !(mimeTypes contains x.mimeType.toLowerCase()) =>
        S.error("Ihre Studienbescheinigung muss vom typ GIF, JPG, PNG oder PDF sein.")
        S.redirectTo("/")
      case null =>
        S.error("Bitte eine valide Studienbescheinigung anfügen!")
        S.redirectTo("/")
      case x if x.fileName == "" =>
        S.error("Bitte eine valide Studienbescheinigung anfügen!")
        S.redirectTo("/")
      case x =>
        p.matrcert(x.file)
        p.filename(fp.fileName)
        p.filetype(fp.mimeType)
        
        
    }
  }
  
  def status() = {
    
    val options = Seq("Student" -> "Student", "Dozent" -> "Dozent")
    
    SHtml.select(options, Full(("Student")), p.status(_))
  }
  
  def render = {
    
    "name=firstname" #> p.firstname.toForm &
    "name=lastname" #> p.lastname.toForm &
    "name=zipcode" #> p.zipcode.toForm &
    "name=city" #> p.city.toForm &
    "name=street" #> p.street.toForm &
    "name=email" #> p.email.toForm &
    "name=university" #> p.university.toForm &
    "name=studycourse" #> p.studycourse.toForm &
    "name=status" #> status &
    "name=matrnr" #> p.matrnr.toForm &
    "name=semester" #> p.semester.toForm &
    "name=matrcert" #> SHtml.fileUpload(saveFile _) &
    "name=comment" #> (p.comment.toForm ++ SHtml.hidden(() => process()))
  }
}
