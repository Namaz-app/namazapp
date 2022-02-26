package ba.aadil.namaz.auth

sealed class User(val email: String, val password: String) {
    data class AuthedUser(val emailString: String, val passwordString: String) :
        User(emailString, passwordString)

    object UnAuthUser : User("", "")
}