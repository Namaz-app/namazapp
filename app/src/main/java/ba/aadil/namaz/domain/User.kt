package ba.aadil.namaz.domain

sealed class User(val name: String, val email: String, val password: String) {
    data class AuthedUser(
        val nameString: String,
        val emailString: String,
        val passwordString: String,
    ) :
        User(nameString, emailString, passwordString)

    object UnAuthUser : User(
        "",
        "",
        "")
}