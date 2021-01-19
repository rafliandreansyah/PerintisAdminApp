package com.azhara.perintisadminapp.ui.home.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _dataCar = MutableLiveData<List<CarsData>>()
    val dataCar = _dataCar

    private val _dataTour = MutableLiveData<List<TourData>>()
    val dataTour = _dataTour

    private val _dataUsers = MutableLiveData<List<UserData>>()
    val dataUsers = _dataUsers

    private val _dataMitra = MutableLiveData<List<MitraData>>()
    val dataMitra = _dataMitra

    private val _dataBookingCar = MutableLiveData<List<BookingData>>()
    val dataBookingCar = _dataBookingCar

    private val _dataMitraRegister = MutableLiveData<List<MitraRegisterData>>()
    val dataMitraRegister = _dataMitraRegister

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg = _errorMsg

    fun getCar(){
        _isLoading.value = true
        val carDB = db.collection("cars")
        carDB.get().addOnSuccessListener { document ->
            _isLoading.value = false
            if (document != null){
                val cars = document.toObjects(CarsData::class.java)
                _dataCar.postValue(cars)
            }else{

            }
        }.addOnFailureListener{ e ->
            _isLoading.value = false
            errorMsg.value = e.message.toString()
        }
    }

    fun getTour(){
        _isLoading.value = true
        val tourDb = db.collection("tour")
        tourDb.get().addOnSuccessListener { document ->
            _isLoading.value = false
            if (document != null){
                val tour = document.toObjects(TourData::class.java)
                _dataTour.postValue(tour)
            }else{

            }
        }.addOnFailureListener { e ->
            _isLoading.value = false
            errorMsg.value = e.message.toString()
        }
    }

    fun getUser(){
        _isLoading.value = true
        val userDb = db.collection("users")
        userDb.get().addOnSuccessListener { document ->
            _isLoading.value = false
            val user = document.toObjects(UserData::class.java)
            _dataUsers.postValue(user)
        }.addOnFailureListener { e ->
            _isLoading.value = false
            errorMsg.value = e.message.toString()
        }
    }

    fun getMitra(){
        _isLoading.value = true
        val mitraDb = db.collection("partners")
        mitraDb.get().addOnSuccessListener { document ->
            _isLoading.value = false
            val mitra = document.toObjects(MitraData::class.java)
            _dataMitra.postValue(mitra)
        }.addOnFailureListener { e ->
            _isLoading.value = false
            errorMsg.value = e.message.toString()
        }
    }

    fun getBookingCar(){
        _isLoading.value = true
        val bookingCarDB = db.collection("booking_data")
        bookingCarDB.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                errorMsg.value = error.message.toString()
            }else{
                if (value != null){
                    val bookingData = value.toObjects(BookingData::class.java)
                    _dataBookingCar.postValue(bookingData)
                }else{
                    _dataBookingCar.postValue(null)
                }

            }
        }
    }

    fun getPengajuanMitraConfirmation(){
        _isLoading.value = true
        val mitraRegisterDB = db.collection("partner_register")
        mitraRegisterDB.get().addOnSuccessListener {
            _isLoading.value = false
            val mitraRegisterData = it.toObjects(MitraRegisterData::class.java)
            _dataMitraRegister.postValue(mitraRegisterData)
        }.addOnFailureListener {
            _isLoading.value = false
            errorMsg.value = it.message.toString()
        }
    }

}