package kz.stierlitz.skillcinema.data.remote.auth

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String? = null
)

data class AuthResult(
    val data: UserData?,
    val errorMessage: String? = null
)

