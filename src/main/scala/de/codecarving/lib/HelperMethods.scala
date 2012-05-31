package de.codecarving
package lib

import net.liftweb.common.{ Full, Box, Empty }
import net.liftweb.http.StreamingResponse
import net.liftweb.http.LiftResponse
import net.liftweb.http.RequestVar

object HelperMethods {
  
  /**
   * A RequestVar for Downloads.
   */
  object CurrentDownload extends RequestVar[Box[LiftResponse]](Empty)
  
  /**
   * Generating the headers for the return4Download method.
   */
  private def headers(in: Array[Byte], filename: String, contenttype: String, ext: String) = {
      ("Content-encoding" -> "UTF-8") ::
      ("Content-type" -> contenttype) ::
      ("Content-length" -> in.length.toString) ::
      ("Content-disposition" -> ("attachment; filename=" + filename.replaceAll(" ","_") + (if(ext == "") "" else "." + ext))) :: Nil
  }

  /**
   * For the creation of download links of generated files this method sets
   * a Box[LiftResponse] which will return a Array[Byte] to the User which
   * requests a file.
   * @param in The file as an Array[Byte].
   * @param filename The filename which will be viewed to the User.
   * @return The LiftResponse which will be returned to the User.
   */
  def return4Download(in: Array[Byte], filename: String, contenttype: String, ext: String = ""): Box[LiftResponse] = {
    Full(StreamingResponse(
      new java.io.ByteArrayInputStream(in),
      () => {},
      in.length,
      headers(in, filename, contenttype, ext), Nil, 200)
    )
  }
  
}
