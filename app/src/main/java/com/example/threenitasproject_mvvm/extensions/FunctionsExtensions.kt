package com.example.threenitasproject_mvvm.extensions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.threenitasproject_mvvm.BuildConfig
import java.io.File
import java.util.*

fun createFolder(context: Context, nameOfFolder : String){
    val file = File(context.getExternalFilesDir(null),nameOfFolder)
    if(!file.exists()){
        file.mkdirs()
    }
}
fun doesFolderExist(context: Context, nameOfFolder: String) : Boolean{
    val file = File(context.getExternalFilesDir(null),nameOfFolder)
    return file.exists()
}

fun deleteFolder(context: Context?, nameOfFolder: String){
    val file = File(context?.getExternalFilesDir(null),nameOfFolder)
    file.deleteRecursively()
}

fun doesMagazineExist(context: Context, nameOfMagazine: String):Boolean{
    val magazinesDirectory = File(
        context.getExternalFilesDir(null),
        "Magazines").toString()
    val file = File(magazinesDirectory, nameOfMagazine.trim() + ".pdf")
    return file.exists()
}
fun openMagazinePdf(context : Context, pdfName: String){
    val folderPath = File(
        context.getExternalFilesDir(null),
        "Magazines"
    ).toString()
    val filePath = File(folderPath, pdfName)
    val intent = Intent(Intent.ACTION_VIEW)
    val uri =
        FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", filePath)
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    intent.setDataAndType(uri, "application/pdf")
    context.startActivity(Intent.createChooser(intent, "Open With"))
}

fun setLocale(Lang: String,context:Context) {

    val locale = Locale(Lang)
    Locale.setDefault(locale)
    val config = Configuration()
    config.locale = locale
    context.resources.updateConfiguration(
        config
        ,context.resources.displayMetrics
    )
    StartApp.sharedPreferencesProvider.putString(
        "locale"
        ,Lang)
}
fun checkForInternet(context: Context): Boolean {

    // register activity with the connectivity manager service
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // if the android version is equal to M
    // or greater we need to use the
    // NetworkCapabilities to check what type of
    // network has the internet connection
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        // Returns a Network object corresponding to
        // the currently active default data network.
        val network = connectivityManager.activeNetwork ?: return false

        // Representation of the capabilities of an active network.
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Wi-Fi transport,
            // or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            // Indicates this network uses a Cellular transport. or
            // Cellular has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // else return false
            else -> false
        }
    } else {
        // if the android version is below M
        @Suppress("DEPRECATION") val networkInfo =
            connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}
