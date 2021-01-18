package com.azhara.perintisadminapp.utils

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

    fun currencyFormat(currency: Long): String{
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(currency)
    }
}