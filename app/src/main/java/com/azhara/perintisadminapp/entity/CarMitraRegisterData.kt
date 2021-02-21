package com.azhara.perintisadminapp.entity

import android.os.Parcel
import android.os.Parcelable

data class CarMitraRegisterData(
        var carImg: String? = null,
        var carNumber: String? = null,
        var carTransmission: String? = null,
        var carType: String? = null,
        var partnerId: String? = null,
        var carYear: Int? = null
): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readValue(Int::class.java.classLoader) as? Int
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(carImg)
                parcel.writeString(carNumber)
                parcel.writeString(carTransmission)
                parcel.writeString(carType)
                parcel.writeString(partnerId)
                parcel.writeValue(carYear)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<CarMitraRegisterData> {
                override fun createFromParcel(parcel: Parcel): CarMitraRegisterData {
                        return CarMitraRegisterData(parcel)
                }

                override fun newArray(size: Int): Array<CarMitraRegisterData?> {
                        return arrayOfNulls(size)
                }
        }
}