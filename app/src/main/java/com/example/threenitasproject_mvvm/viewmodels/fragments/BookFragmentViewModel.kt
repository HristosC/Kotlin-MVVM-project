package com.example.threenitasproject_mvvm.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threenitasproject_mvvm.models.BooksResponse
import com.example.threenitasproject_mvvm.models.RecyclerViewContainer
import com.example.threenitasproject_mvvm.network.BooksNetwork

class BookFragmentViewModel:ViewModel() {
    private val _sortedBooks = MutableLiveData<MutableList<RecyclerViewContainer>>()
    val sortedBooks: LiveData<MutableList<RecyclerViewContainer>>
        get()= _sortedBooks

    fun booksNetwork(){
        BooksNetwork.checkBooks()
        { response ->
            sortBooks(response)
        }
    }
    private fun sortBooks(books: List<BooksResponse>?){
        val sortedBooks = books?.sortedByDescending { it.date_released }
        passRecyclerViewContainer(sortedBooks)
    }
    private fun passRecyclerViewContainer(books: List<BooksResponse>?){
        val booksWithRecyclerViewContainer: MutableList<RecyclerViewContainer> =
            mutableListOf<RecyclerViewContainer>()
        if (books != null) {
            for (i in books.indices) {
                if (i == 0) {
                    booksWithRecyclerViewContainer.add(
                        RecyclerViewContainer(null,
                            true,
                            books[i].date_released.take(4))
                    )
                    booksWithRecyclerViewContainer.add(RecyclerViewContainer(books[i], false, null))
                }
                else {
                    if (books[i].date_released.take(4) == books[i - 1].date_released.take(4)) {
                        booksWithRecyclerViewContainer.add(RecyclerViewContainer(books[i], false, null))
                    }
                    else {
                        booksWithRecyclerViewContainer.add(
                            RecyclerViewContainer(null,
                                true,
                                books[i].date_released.take(4))
                        )
                        booksWithRecyclerViewContainer.add(RecyclerViewContainer(books[i], false, null))
                    }
                }
            }
        }
        _sortedBooks.value = booksWithRecyclerViewContainer
    }
}