package com.azhara.perintisadminapp.entity

import com.google.firebase.Timestamp

data class BookingData(
    var bookedIdPartner: String? = null,
    var bookedListIdUser: String? = null,
    var carId: String? = null,
    var carName: String? = null,
    var driver: String? = null,
    var duration: Int? = null,
    var endDate: Timestamp? = null,
    var partnerId: String? = null,
    var pickUpArea: String? = null,
    var startDate: Timestamp? = null,
    var statusBooking: Int? = null, //null Belum terkonfirmasi, 0 on Progress dan 1 Selesai
    var totalPrice: Long? = null,
    var userId: String? = null,
    var userImgUrl: String? = null,
    var userName: String? = null
)