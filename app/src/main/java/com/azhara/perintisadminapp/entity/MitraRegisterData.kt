package com.azhara.perintisadminapp.entity

import android.os.Parcel
import android.os.Parcelable

data class MitraRegisterData(
        var address: String? = null,
        var carImg: String? = null,
        var carNumberRegister: String? = null,
        var carTransmision: Int? = null,
        var carType: String? = null,
        var carYear: Int? = null,
        var email: String? = null,
        var name: String? = null,
        var phoneNumber: Long? = null,
        var statusConfirm: Int? = null
): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readString(),
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readString(),
                parcel.readString(),
                parcel.readValue(Long::class.java.classLoader) as? Long,
                parcel.readValue(Int::class.java.classLoader) as? Int
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(address)
                parcel.writeString(carImg)
                parcel.writeString(carNumberRegister)
                parcel.writeValue(carTransmision)
                parcel.writeString(carType)
                parcel.writeValue(carYear)
                parcel.writeString(email)
                parcel.writeString(name)
                parcel.writeValue(phoneNumber)
                parcel.writeValue(statusConfirm)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<MitraRegisterData> {
                override fun createFromParcel(parcel: Parcel): MitraRegisterData {
                        return MitraRegisterData(parcel)
                }

                override fun newArray(size: Int): Array<MitraRegisterData?> {
                        return arrayOfNulls(size)
                }
        }
}