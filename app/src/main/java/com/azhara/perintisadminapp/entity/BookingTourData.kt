package com.azhara.perintisadminapp.entity

import android.os.Parcel
import android.os.Parcelable
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
): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readParcelable(Timestamp::class.java.classLoader),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
                parcel.readValue(Long::class.java.classLoader) as? Long,
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(dateTour, flags)
                parcel.writeString(duration)
                parcel.writeString(idDetailBookingTourUser)
                parcel.writeString(idListBookingTourUser)
                parcel.writeString(pickupArea)
                parcel.writeValue(statusPayment)
                parcel.writeValue(totalPrice)
                parcel.writeString(tourId)
                parcel.writeString(tourName)
                parcel.writeString(userBookingName)
                parcel.writeString(userId)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<BookingTourData> {
                override fun createFromParcel(parcel: Parcel): BookingTourData {
                        return BookingTourData(parcel)
                }

                override fun newArray(size: Int): Array<BookingTourData?> {
                        return arrayOfNulls(size)
                }
        }
}