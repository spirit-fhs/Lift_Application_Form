package de.codecarving
package snippet

import model.Participant
import net.liftweb.http.S
import net.liftweb.http.SHtml
import scala.xml.Text
import lib.{ HelperMethods => HM }
import scala.collection.mutable.ArrayBuffer


class Evaluation {
  
   /**
   * Building the CSV String together for Exporting and Downloading user Informations as CSV Files.
   */
  def buildCSV(in: List[Participant]) = {
    
    def strip4CSV(in: String) = {
      in.replaceAll(";", "_").replaceAll(",", "_").replaceAll("\r\n", "|")
    }
    
    val csvBodyHead = 
      "Vorname;Nachname;Strasse;Ort;PLZ;Telefon;E-Mail;Teilnehmerzahl;Kinder;Viba;Wanderung;Besuch_Informatik;Festempfang;Kommentar\n"
    
    val csvBody = 
      for(v <- in) yield strip4CSV(v.firstname.get) + ";" + strip4CSV(v.lastname.get) + ";" + strip4CSV(v.street.get) + ";" +
        strip4CSV(v.city.get) + ";" + strip4CSV(v.zipcode.get) + ";" + strip4CSV(v.telefon.get) + ";" + strip4CSV(v.email.get) + ";" +
        strip4CSV(v.anz_erwachsene.toString()) + ";" + strip4CSV(v.anz_kinder.toString()) + "; " + strip4CSV(v.viba) + ";" +
        strip4CSV(v.spaziergang) + ";" + strip4CSV(v.besuch) + ";" + strip4CSV(v.festempfang) + ";" + strip4CSV(v.comment.get) + "\n"
                 
    (csvBodyHead + csvBody.mkString).getBytes("UTF-8")
  }
  
  /**
  * Listing all Participants on /eval.
  */
  def list() = {
    val allParticipants = Participant.findAll()
    
    <table>
    { allParticipants.flatMap(v =>
      <tr>
        <td style="border:0">
          {v.lastname + ", "}{v.firstname}<br />
          {v.email}
        </td>
        <td style="border:0">
          {SHtml.link("/download", () => HM.CurrentDownload(
                                            HM.return4Download(buildCSV(List(v)), 
                                                v.firstname.get + "_" + v.lastname.get, 
                                                "text/csv", "csv")), 
                      Text("CSV Export"))}<br />

        </td>
      </tr>) }
    </table>
  }
  
  /**
  * Giving extra exportOptions on the /eval page.
  */
  def exportOptions() = {
    val allParticipants = Participant.findAll()
    
    <table>
      <tr>
        <td style="border:0">
          {SHtml.link("/download", () => HM.CurrentDownload(
                                            HM.return4Download(buildCSV(allParticipants), 
                                                               "Teilnehmer", "text/csv", "csv")), 
                                                               Text("CSV Export für Alle"))}
        </td>
      </tr>
    </table>
  }
  
  /**
  * Some small stats about the Participants on /eval.
  */
  def stats() = {
	var erwachseneCount = 0
    var kinderCount = 0
    //var viba_erwachseneCount = 0
    //var viba_kinderCount = 0
    //var spaziergang_erwachseneCount = 0
    //var spaziergang_kinderCount = 0
    //var besuch_erwachseneCount = 0
    //var besuch_kinderCount = 0
    //var festempfang_erwachseneCount = 0
    //var festempfang_kinderCount = 0
    var iter_erwachsene = Participant.findAll().iterator
    var iter_kinder = Participant.findAll().iterator
    
    //var iter_viba_erwachsene = Participant.findAll().filter(x => x.viba.get.toLowerCase == "ja").iterator
    //var iter_viba_kinder = Participant.findAll().filter(x => x.viba.get.toLowerCase == "ja").iterator
    //var iter_spaziergang_erwachsene = Participant.findAll().filter(x => x.spaziergang.get.toLowerCase == "ja").iterator
    //var iter_spaziergang_kinder = Participant.findAll().filter(x => x.spaziergang.get.toLowerCase == "ja").iterator
    //var iter_besuch_erwachsene = Participant.findAll().filter(x => x.besuch.get.toLowerCase == "ja").iterator
    //var iter_besuch_kinder = Participant.findAll().filter(x => x.besuch.get.toLowerCase == "ja").iterator
    //var iter_festempfang_erwachsene = Participant.findAll().filter(x => x.festempfang.get.toLowerCase == "ja").iterator
    //var iter_festempfang_kinder = Participant.findAll().filter(x => x.festempfang.get.toLowerCase == "ja").iterator
    
    // Gesamtanzahl zaehlen:
    while(iter_erwachsene.hasNext){
      erwachseneCount += iter_erwachsene.next().anz_erwachsene
    }
	while(iter_kinder.hasNext){
      kinderCount += iter_kinder.next().anz_kinder
    }
	// Viba-Teilnehmerzahlen
	//while(iter_viba_erwachsene.hasNext){
    //  viba_erwachseneCount += iter_viba_erwachsene.next().anz_erwachsene
    //}
	//while(iter_viba_kinder.hasNext){
	//  viba_kinderCount += iter_viba_kinder.next().anz_kinder
	//}
    // Spaziergang-Teilnehmerzahlen
	//while(iter_spaziergang_erwachsene.hasNext){
    //  spaziergang_erwachseneCount += iter_spaziergang_erwachsene.next().anz_erwachsene
    //}
	//while(iter_spaziergang_kinder.hasNext){
	//  spaziergang_kinderCount += iter_spaziergang_kinder.next().anz_kinder
	//}
    // Besuch-Teilnehmerzahlen
	//while(iter_besuch_erwachsene.hasNext){
    //  besuch_erwachseneCount += iter_besuch_erwachsene.next().anz_erwachsene
    //}
	//while(iter_besuch_kinder.hasNext){
	//  besuch_kinderCount += iter_besuch_kinder.next().anz_kinder
	//}	
    // Festempfang-Teilnehmerzahlen
	//while(iter_festempfang_erwachsene.hasNext){
    //  festempfang_erwachseneCount += iter_festempfang_erwachsene.next().anz_erwachsene
    //}
	//while(iter_festempfang_kinder.hasNext){
	//  festempfang_kinderCount += iter_festempfang_kinder.next().anz_kinder
	//}	
	
	
    <table>
      <tr>
		<b>Angemeldete Besucher:</b>
        <td style="border:0">
          Erwachsene: {erwachseneCount}
          <br />
          Kinder: {kinderCount}
        </td>
      </tr>
    </table>
          
    /*      
      <table>
        <tr>
            <b>Teilnehmer an Viba Erlebnisfuehrung:</b>
          <td style="border:0">
            Erwachsene: {viba_erwachseneCount}
            <br />
            Kinder: {viba_kinderCount}
          </td>
        </tr>
      </table>
      <table>
        <tr>
            <b>Teilnehmer am historischen Spaziergang:</b>
          <td style="border:0">
            Erwachsene: {spaziergang_erwachseneCount}
            <br />
            Kinder: {spaziergang_kinderCount}
          </td>
        </tr>
      </table>
      <table>
        <tr>
            <b>Teilnehmer am Besuch des Informatik Domizils:</b>
          <td style="border:0">
            Erwachsene: {besuch_erwachseneCount}
            <br />
            Kinder: {besuch_kinderCount}
          </td>
        </tr>
      </table>
      <table>
        <tr>
            <b>Teilnehmer am Festempfang:</b>
          <td style="border:0">
            Erwachsene: {festempfang_erwachseneCount}
            <br />
            Kinder: {festempfang_kinderCount}
          </td>
        </tr>
      </table>
    */        
  }
}
