package de.codecarving
package snippet

import model.Participant
import net.liftweb.util.BindHelpers._
import net.liftweb.http.S
import net.liftweb.common.Full
import net.liftweb.mapper.MapperRules
import net.liftweb.http.{ SHtml, FileParamHolder, SessionVar }

class Form {
  
  
  object p extends SessionVar[Participant](Participant.create)
  
  def validateForm = {

    val EmailParser = """([\w\d\-\_\.]+)(\+\d+)?@([\w\d\-\.]+)""".r
    
           
    def validateEmail = p.email.get match {
      case EmailParser(_,_,_) => true
      case _ => false
    }
    
    (validateEmail)
   
  }
  
  def process() = {
     validateForm match {
        case (false) =>
          S.error("Bitte eine valide E-Mail-Adresse angeben!")
          S.redirectTo("/")
        case _ =>
     }
     
     p.saveMe()
     p.remove()
     
     S.notice("Anmeldung erfolgreich!")
     S.redirectTo("/")
     
  }
  
 val options = Seq("Ja" -> "Ja", "Nein" -> "Nein")
  
 
 def viba() = {
    SHtml.select(options, Full(("Ja")), p.viba(_))
  }
  
 def spaziergang() = {
    SHtml.select(options, Full(("Ja")), p.spaziergang(_))
  }
 
 def besuch() = {
    SHtml.select(options, Full(("Ja")), p.besuch(_))
  }
 
 def festempfang() = {
    SHtml.select(options, Full(("Ja")), p.festempfang(_))
  }
  
  def render = {
    
    
    "name=firstname" #> p.firstname.toForm &
    "name=lastname" #> p.lastname.toForm &
    "name=zipcode" #> p.zipcode.toForm &
    "name=city" #> p.city.toForm &
    "name=street" #> p.street.toForm &
    "name=telefon" #> p.telefon.toForm &
    "name=email" #> p.email.toForm &
    "name=anz_erwachsene" #> p.anz_erwachsene.toForm &
    "name=anz_kinder" #> p.anz_kinder.toForm &
    "name=viba" #> viba &
    "name=spaziergang" #> spaziergang &
    "name=besuch" #> besuch &
    "name=festempfang" #> festempfang &
    "name=comment" #> (p.comment.toForm ++ SHtml.hidden(() => process()))
  }
}
