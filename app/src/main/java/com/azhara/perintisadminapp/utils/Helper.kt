package com.azhara.perintisadminapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object Helper{

    fun convertToLocalDate(date: Long): String {
        // Convert timestamp to local time
        val calendar = Calendar.getInstance()
        val tz = calendar.timeZone
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        sdf.timeZone = tz
        val startSecondDate = Date(date * 1000)
        return sdf.format(startSecondDate)
    }

    fun convertTime(time: Long?, format: String?): String? {
        // Convert timestamp to local time
        val calendar = Calendar.getInstance()
        val tz = calendar.timeZone
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = tz
        val startSecondDate = time?.times(1000)?.let { Date(it) }
        return sdf.format(startSecondDate)
    }

    fun currencyFormat(currency: Long): String{
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(currency)
    }

    fun snackbar(msg: String?, view: View){
        msg?.let {
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).setAction("Hide"){

            }.show()
        }
    }

    fun toast(msg: String?, context: Context){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    // resize filebitmap with specific size
    private fun resizeBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width //get width image
        var height = image.height //get heigh image

        val bitMapRatio = width.toFloat() / height.toFloat()
        if (bitMapRatio > 1) {
            width = maxSize
            height = (width / bitMapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitMapRatio).toInt()
        }

        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    //Change format bitmap to byte Array
    fun imageByteArray(bitmap: Bitmap?, maxSize: Int?): ByteArray {
        val bitmapCompress = bitmap?.let { maxSize?.let { it1 -> resizeBitmap(it, it1) } } //resize bitmap file
        val baos = ByteArrayOutputStream()
        bitmapCompress?.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            baos
        ) //compress bitmap extension to JPEG

        return baos.toByteArray()
    }

}