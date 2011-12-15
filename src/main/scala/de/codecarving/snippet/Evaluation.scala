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
      "Vorname;Nachname;Straße;Ort;PLZ;E-Mail;Hochschule;Fakultät;Status;Matrnr.;Semester;Kommentar\n"
    
    val csvBody = 
      for(v <- in) yield strip4CSV(v.firstname.get) + ";" + strip4CSV(v.lastname.get) + ";" + strip4CSV(v.street.get) + ";" +
        strip4CSV(v.city.get) + ";" + strip4CSV(v.zipcode.get) + ";" + strip4CSV(v.email.get) + ";" +
        strip4CSV(v.university.get) + ";" + strip4CSV(v.studycourse.get) + "; " + strip4CSV(v.status) + ";" +
        strip4CSV(v.matrnr.get) + ";" + strip4CSV(v.semester.get) + ";" + strip4CSV(v.comment.get) + "\n"
                 
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
          {v.status + ": "}{v.lastname + ", "}{v.firstname}<br />
          {v.email}
        </td>
        <td style="border:0">
          {SHtml.link("/download", () => HM.CurrentDownload(
                                            HM.return4Download(buildCSV(List(v)), 
                                                v.firstname.get + "_" + v.lastname.get, 
                                                "text/csv", "csv")), 
                      Text("CSV Export"))}<br />
          
          {v.status.get match {
            case "Student" => SHtml.link("/download", () => HM.CurrentDownload(
                                                               HM.return4Download(v.matrcert.get, 
                                                                                  v.filename.get, 
                                                                                  v.filetype.get)), 
                                                                     Text("Studienbescheinigung"))
            case _ => 
          }}

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
    val studentCount = 
      Participant.findAll().filter(x => x.status.get.toLowerCase == "student").size
    val dozentCount = 
      Participant.findAll().filter(x => x.status.get.toLowerCase == "dozent").size
    
    <table>
      <tr>
        <td style="border:0">
          Studenten: {studentCount}
          <br />
          Dozenten: {dozentCount}
        </td>
      </tr>
    </table>
  }
  
}
