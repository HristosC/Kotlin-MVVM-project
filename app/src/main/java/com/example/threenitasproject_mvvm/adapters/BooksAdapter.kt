package com.example.threenitasproject_mvvm.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import androidx.annotation.IdRes
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import com.downloader.OnDownloadListener
import java.util.*

import com.downloader.Progress
import com.downloader.request.DownloadRequest
import com.example.threenitasproject_mvvm.R
import com.example.threenitasproject_mvvm.extensions.createFolder
import com.example.threenitasproject_mvvm.extensions.doesFolderExist
import com.example.threenitasproject_mvvm.extensions.doesMagazineExist
import com.example.threenitasproject_mvvm.extensions.openMagazinePdf
import com.example.threenitasproject_mvvm.models.BooksResponse
import com.example.threenitasproject_mvvm.models.Magazine
import com.example.threenitasproject_mvvm.models.RecyclerViewContainer
import com.example.threenitasproject_mvvm.network.PrDownloader.downloadBuild
import com.example.threenitasproject_mvvm.network.PrDownloader.initializePrDownloader
import java.io.File



class BooksAdapter(private val books: MutableList<RecyclerViewContainer>, val context : Context) :
    RecyclerView.Adapter<BooksAdapter.ViewHolder>() {

    private val magazinesDirectory = File(
        context.getExternalFilesDir(null),
        "Magazines"
    ).toString()
    private lateinit var magazine: Magazine

    enum class RowType {
        HEADER,
        ROW
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewMap: MutableMap<Int, View> = HashMap()
        init {
            findViewItems(itemView)
        }

        private fun findViewItems(itemView: View) {
            addToMap(itemView)
            if (itemView is ViewGroup) {
                val childCount = itemView.childCount
                (0 until childCount)
                    .map { itemView.getChildAt(it) }
                    .forEach { findViewItems(it) }
            }
        }
        private fun addToMap(itemView: View) {
            viewMap[itemView.id] = itemView
        }
        fun setHeader(@IdRes id: Int, text: String) {

            val view = (viewMap[id]
                ?: throw IllegalArgumentException("View for $id not found")) as? TextView
                ?: throw IllegalArgumentException("View for $id is not a TextView")
            view.text = text
        }
        fun setItems(item: BooksResponse, @IdRes textViewId: Int, @IdRes imageViewId: Int, context: Context) {
            val imageView = viewMap[imageViewId] as ImageView?
            Picasso.get().load(item.img_url).into(imageView)
            val view = (viewMap[textViewId]
                ?: throw IllegalArgumentException("View for $textViewId not found")) as? TextView
                ?: throw IllegalArgumentException("View for $textViewId is not a TextView")
            view.text = item.title
            setDownloadedMagazines(context,item, setMagazineModel())
        }
        fun setMagazineModel(): Magazine {
            return Magazine(
                viewMap[R.id.imageMagazine] as ImageView,
                viewMap[R.id.downloadMagazine] as ImageView,
                viewMap[R.id.triangleImage] as ImageView,
                viewMap[R.id.check_icon] as ImageView,
                viewMap[R.id.progressBar] as ProgressBar
            )
        }
        private fun setDownloadedMagazines(context:Context, book: BooksResponse, magazine: Magazine) {
            if(doesMagazineExist(context,book.title)){
                magazine.downloadIcon.visibility = View.INVISIBLE
                magazine.checkIcon.visibility = View.VISIBLE
                magazine.triangleIcon.visibility = View.VISIBLE
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView: View = when (viewType) {
            RowType.ROW.ordinal -> layoutInflater.inflate(R.layout.booksrecycler, parent, false)
            else -> layoutInflater.inflate(R.layout.recycler_header, parent, false)
        }
        return ViewHolder(inflatedView)
    }
    override fun getItemCount() = books.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(!doesFolderExist(context,"Magazines")){
            createFolder(context,"Magazines")
        }
        setHeaderOrRow(holder, position)
        lastRowBottomMargin(holder, position)
        magazineClick(position, holder)
    }
    private fun setHeaderOrRow(holder: ViewHolder, position: Int) {
        val book = books[position]
        if (book.isHeader) {
            book.headerTitle?.let { holder.setHeader(R.id.header_text_view, book.headerTitle!!) }
        } else {
            holder.setItems(book.BooksResponse!!, R.id.title, R.id.imageMagazine,context)
        }
    }
    private fun lastRowBottomMargin(holder: ViewHolder, position: Int) {
        if (position == books.lastIndex) {
            val params = holder.itemView.layoutParams as GridLayoutManager.LayoutParams
            params.bottomMargin = 100
            holder.itemView.layoutParams = params
        } else {
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 0
            holder.itemView.layoutParams = params
        }
    }
    private fun magazineClick(position: Int, holder: ViewHolder) {
        val book = books[position].BooksResponse
        val pdfURL = book?.pdf_url
        val savedFileName = book?.title?.trim() + ".pdf"
        val imageMagazine = holder.viewMap[R.id.imageMagazine]
        imageMagazine?.setOnClickListener {
            magazine = holder.setMagazineModel()
            if (magazine.downloadIcon.visibility == View.INVISIBLE) {
                if (magazine.progressBar.visibility == View.INVISIBLE) {
                    openMagazinePdf(context,savedFileName)
                }
            } else {
                initializePrDownloader(context)
                setProgressBar(downloadBuild(pdfURL, savedFileName, magazinesDirectory),magazine)
            }
        }
    }
    private fun setProgressBar(downloadBuild: DownloadRequest, currentMagazine: Magazine) {
        downloadBuild.setOnProgressListener {
                progressBarProgress(it,currentMagazine)
            }
                ?.start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        progressBarCompleted(currentMagazine)
                    }
                    override fun onError(error: com.downloader.Error?) {
                        Toast.makeText(
                            context,
                            "Failed to download the ${downloadBuild.url}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })
    }
    private fun progressBarProgress(progress: Progress,currentMagazine: Magazine) {

        currentMagazine.progressBar.max = progress.totalBytes.toInt()
        currentMagazine.progressBar.progress = progress.currentBytes.toInt()
        currentMagazine.progressBar.visibility = View.VISIBLE
        currentMagazine.downloadIcon.visibility = View.INVISIBLE
    }
    private fun progressBarCompleted(currentMagazine: Magazine) {
        currentMagazine.progressBar.visibility = View.INVISIBLE
        currentMagazine.checkIcon.visibility = View.VISIBLE
        currentMagazine.triangleIcon.visibility = View.VISIBLE
    }
    override fun getItemViewType(position: Int): Int {
        return if (books[position].isHeader) {
            0
        } else {
            1
        }
    }
}