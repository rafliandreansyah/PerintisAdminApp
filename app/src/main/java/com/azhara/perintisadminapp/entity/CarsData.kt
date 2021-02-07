package com.azhara.perintisadminapp.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

data class CarsData(
        var carId: String? = null,
        var capacity: Int? = null,
        var carName: String? = null,
        var gear: Int? = null,
        var imgUrl: String? = null,
        var luggage: Int? = null,
        var partnerId: String? = null,
        var price: Long? = null,
        var statusReady: Boolean? = false,
        var year: Int? = null,
        var booked: List<BookedDate>? = null,
        var carNumberPlate: String? = null,
        var carOwnerName: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.createTypedArrayList(BookedDate),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(carId)
        parcel.writeValue(capacity)
        parcel.writeString(carName)
        parcel.writeValue(gear)
        parcel.writeString(imgUrl)
        parcel.writeValue(luggage)
        parcel.writeString(partnerId)
        parcel.writeValue(price)
        parcel.writeValue(statusReady)
        parcel.writeValue(year)
        parcel.writeTypedList(booked)
        parcel.writeString(carNumberPlate)
        parcel.writeString(carOwnerName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CarsData> {
        override fun createFromParcel(parcel: Parcel): CarsData {
            return CarsData(parcel)
        }

        override fun newArray(size: Int): Array<CarsData?> {
            return arrayOfNulls(size)
        }
    }
}

data class BookedDate(
        var userId: String? = null,
        var startDate: Timestamp? = null,
        var endDate: Timestamp? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(Timestamp::class.java.classLoader),
            parcel.readParcelable(Timestamp::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeParcelable(startDate, flags)
        parcel.writeParcelable(endDate, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookedDate> {
        override fun createFromParcel(parcel: Parcel): BookedDate {
            return BookedDate(parcel)
        }

        override fun newArray(size: Int): Array<BookedDate?> {
            return arrayOfNulls(size)
        }
    }
}