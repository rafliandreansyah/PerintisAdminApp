package com.azhara.perintisadminapp.entity

data class UserListBookingData(
        var bookingId: String? = null,
        var bookingListId: String? = null,
        var bookingName: String? = null,
        var bookingType: Int? = null,
        var imgUrlProofPayment: String? = null,
        var statusPayment: Boolean? = null,
        var uploadProofPayment: Boolean? = null
)