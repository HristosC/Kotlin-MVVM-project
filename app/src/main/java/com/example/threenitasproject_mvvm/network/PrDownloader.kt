package com.example.threenitasproject_mvvm.network

import android.content.Context
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.downloader.request.DownloadRequest

object PrDownloader {
    fun initializePrDownloader(context : Context){
    //Initialize PRDownloader with read and connection timeout
        val config = PRDownloaderConfig.newBuilder()
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()
        PRDownloader.initialize(context, config)
    }
    fun downloadBuild(url: String?, fileName: String?, directoryLoc: String): DownloadRequest {

        return PRDownloader.download(
            url,
            directoryLoc,
            fileName
        ).build()

    }

}