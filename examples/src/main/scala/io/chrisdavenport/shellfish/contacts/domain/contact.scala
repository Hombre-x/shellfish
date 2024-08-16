package io.chrisdavenport.shellfish.contacts.domain

import scala.util.control.NoStackTrace

object contact {

  case class Contact(
      username: Username,
      firstName: Name,
      lastName: Name,
      phoneNumber: PhoneNumber,
      email: Email
  ) {
    def show: String =
      s"""|------ $username ------
         |
         |First Name:   $firstName
         |Last Name:    $lastName
         |Phone Number: $phoneNumber
         |Email:        $email
          """.stripMargin
  }

  type Username    = String
  type Name        = String
  type PhoneNumber = String
  type Email       = String
  case class ContactFound(username: Username)
      extends NoStackTrace

}
