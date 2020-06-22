package org.moda.model

sealed class User(val userid: String, val username: String, val userpassword: String) {
}

object User{
//  def apply(username: String, userpassword: String): User = new User(, username, userpassword)
}
