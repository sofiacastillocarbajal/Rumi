package pe.edu.upc.rumi.model

import java.io.Serializable

data class LoginResponse (
    val profile: Profile,
    val role: String,
    val token: String
) : Serializable {
    constructor() : this(Profile(), "", "")
}