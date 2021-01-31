package com.azhara.perintisadminapp.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

data class BookingData(
        var id: String? = null,
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
        var statusBooking: String? = null, //null Belum terkonfirmasi, 0 on Progress dan 1 Selesai
        var totalPrice: Long? = null,
        var userId: String? = null,
        var userImgUrl: String? = null,
        var userName: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? String,
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(bookedIdPartner)
        parcel.writeString(bookedListIdUser)
        parcel.writeString(carId)
        parcel.writeString(carName)
        parcel.writeString(driver)
        parcel.writeValue(duration)
        parcel.writeParcelable(endDate, flags)
        parcel.writeString(partnerId)
        parcel.writeString(pickUpArea)
        parcel.writeParcelable(startDate, flags)
        parcel.writeString(statusBooking)
        parcel.writeValue(totalPrice)
        parcel.writeString(userId)
        parcel.writeString(userImgUrl)
        parcel.writeString(userName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookingData> {
        override fun createFromParcel(parcel: Parcel): BookingData {
            return BookingData(parcel)
        }

        override fun newArray(size: Int): Array<BookingData?> {
            return arrayOfNulls(size)
        }
    }
}