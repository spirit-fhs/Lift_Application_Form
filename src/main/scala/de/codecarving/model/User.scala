package de.codecarving.model

import net.liftweb.common.Empty
import net.liftweb.mapper.{ MegaProtoUser, MetaMegaProtoUser }
import scala.xml.NodeSeq

object User extends User with MetaMegaProtoUser[User] {

  def currentUserName = User.currentUser map { _.shortName } openOr error("No current user!")

  override def signupFields = firstName :: email :: password :: Nil

  override def menus =
  List(loginMenuLoc, createUserMenuLoc, changePasswordMenuLoc, logoutMenuLoc).flatten
  //List(loginMenuLoc, logoutMenuLoc).flatten

  override def skipEmailValidation = true

  override def loginXhtml = surround(super.loginXhtml)

  override def signupXhtml(user: User) = surround(super.signupXhtml(user))

  override def changePasswordXhtml = surround(super.changePasswordXhtml)

  private def surround(xhtml: => NodeSeq) =
    <lift:surround with="default" at="content">{ xhtml }</lift:surround>
}

class User extends MegaProtoUser[User] {

  override def firstNameDisplayName = "Name"

  override def getSingleton = User
}