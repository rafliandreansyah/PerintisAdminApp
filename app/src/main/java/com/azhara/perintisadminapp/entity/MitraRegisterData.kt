package com.azhara.perintisadminapp.entity

data class MitraRegisterData(
        var address: String? = null,
        var carImg: String? = null,
        var carNumberRegister: String? = null,
        var carTransmision: Int? = null,
        var carType: String? = null,
        var carYear: Int? = null,
        var email: String? = null,
        var name: String? = null,
        var phoneNumber: Long? = null
)