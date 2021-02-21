package com.azhara.perintisadminapp.entity

import com.google.firebase.Timestamp

data class UserDetailBookingTour (
    var data: Timestamp? = null,
    var pickupArea: String? = null,
    var statusBooking: Int? = null,
    var totalPrice: Long? = null,
    var tourId: String? = null
        )