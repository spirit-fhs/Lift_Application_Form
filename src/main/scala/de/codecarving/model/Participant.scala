package de.codecarving.model

import net.liftweb.mapper._

class Participant extends LongKeyedMapper[Participant] with IdPK {
  def getSingleton = Participant
  
  object firstname extends MappedString(this, 100)
  object lastname extends MappedString(this, 100)
  object city extends MappedString(this, 100) 
  object street extends MappedString(this, 100)
  object zipcode extends MappedString(this, 100)
  object telefon extends MappedString(this, 100)
  object email extends MappedEmail(this, 100)
  
  object anz_erwachsene extends MappedInt(this)
  object anz_kinder extends MappedInt(this)
  
  object veranstaltung
  
  object viba extends MappedString(this, 100)
  object spaziergang extends MappedString(this, 100)
  object besuch extends MappedString(this, 100)
  object festempfang extends MappedString(this, 100)
  
  object comment extends MappedTextarea(this, 10000) {
    
    override def textareaRows = 12
    override def textareaCols = 80
  }
  
}

object Participant extends Participant with LongKeyedMetaMapper[Participant] {
  override def dbTableName = "participant"

} 