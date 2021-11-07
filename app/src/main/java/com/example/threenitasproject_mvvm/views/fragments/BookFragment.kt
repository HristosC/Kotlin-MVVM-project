package com.example.threenitasproject_mvvm.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.example.threenitasproject_mvvm.databinding.FragmentBookBinding
import com.example.threenitasproject_mvvm.models.RecyclerViewContainer
import com.example.threenitasproject_mvvm.network.BooksNetwork
import com.example.threenitasproject_mvvm.adapters.BooksAdapter
import com.example.threenitasproject_mvvm.viewmodels.fragments.BookFragmentViewModel


class BookFragment : Fragment() {
    private lateinit var binding: FragmentBookBinding
    private lateinit var viewModel: BookFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initializeLateVar()
        setObservers()
        viewModel.booksNetwork()
        return binding.root
    }

    private fun showData(books: MutableList<RecyclerViewContainer>) {
        val adapter = context?.let { BooksAdapter(books, it) }
        val gridLayout = GridLayoutManager(context, 2)
        gridLayoutSpanSize(gridLayout,adapter)
        binding.recyclerView.layoutManager = gridLayout
        books.size.let{ binding.recyclerView.setItemViewCacheSize(it) }
        binding.recyclerView.adapter = adapter
    }


    private fun gridLayoutSpanSize(gridLayout: GridLayoutManager, adapter: BooksAdapter?){
        gridLayout.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter?.getItemViewType(position)) {
                    BooksAdapter.RowType.ROW.ordinal -> 1
                    else -> 2
                }
            }
        }
    }

    private fun setObservers(){
        viewModel.sortedBooks.observe(viewLifecycleOwner, Observer {
            showData(it)
        })
    }

    private fun initializeLateVar(){
        binding = FragmentBookBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(BookFragmentViewModel::class.java)
    }

}
