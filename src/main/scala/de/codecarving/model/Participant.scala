package de.codecarving.model

import net.liftweb.mapper._

class Participant extends LongKeyedMapper[Participant] with IdPK {
  def getSingleton = Participant
  
  object firstname extends MappedString(this, 100)
  object lastname extends MappedString(this, 100)
  object city extends MappedString(this, 100) 
  object street extends MappedString(this, 100)
  object zipcode extends MappedString(this, 100)
  object email extends MappedEmail(this, 100)
  
  object university extends MappedString(this, 100)
  object studycourse extends MappedString(this, 100)
  object status extends MappedString(this, 100)
  
  object matrnr extends MappedString(this, 100)
  object semester extends MappedString(this, 100)
  
  object matrcert extends MappedBinary(this)
  object filename extends MappedString(this, 100)
  object filetype extends MappedString(this, 100)
  
  object comment extends MappedTextarea(this, 10000) {
    
    override def textareaRows = 12
    override def textareaCols = 80
  }
  
}

object Participant extends Participant with LongKeyedMetaMapper[Participant] {
  override def dbTableName = "participant"

}
