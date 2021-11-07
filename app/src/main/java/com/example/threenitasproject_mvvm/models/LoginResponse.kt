package com.example.threenitas_project.data_classes



data class LoginResponse(
    val expires_in: Int,
    val token_type: String,
    val refresh_token: String,
    val access_token: String
)
