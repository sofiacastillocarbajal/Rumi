package pe.edu.upc.rumi.model

import java.io.Serializable

data class RegisterResponse (
    val success: String,
    val name: String,
    val gender: String
) : Serializable {
    constructor() : this("", "", "")
}