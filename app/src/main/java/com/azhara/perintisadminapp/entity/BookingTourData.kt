package com.azhara.perintisadminapp.entity

import com.google.firebase.Timestamp

data class BookingTourData(
        var dateTour: Timestamp? = null,
        var duration: String? = null,
        var idDetailBookingTourUser: String? = null,
        var idListBookingTourUser: String? = null,
        var pickupArea: String? = null,
        var statusPayment: Boolean? = null,
        var totalPrice: Long? = null,
        var tourId: String? = null,
        var tourName: String? = null,
        var userBookingName: String? = null,
        var userId: String? = null
)