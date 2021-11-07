package com.example.threenitasproject_mvvm.extensions


fun String.isUserIDValid() : Boolean =
    this.matches(("(^([A-Z]{2})\\d{4}){1}").toRegex())

fun String.isPasswordValid() : Boolean =
    this.matches(
        ("^(?=.*[A-Z].*[A-Z])(?=.*[!@#\$&*])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8}\$")
        .toRegex()
    )