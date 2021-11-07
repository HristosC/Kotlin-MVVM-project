package com.example.threenitasproject_mvvm.models

data class BooksResponse (
    var id: Int,
    var title: String,
    var img_url: String,
    var date_released: String,
    var pdf_url: String
    )

class RecyclerViewContainer (var BooksResponse: BooksResponse?, var isHeader: Boolean, var headerTitle: String?)

