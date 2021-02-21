package com.azhara.perintisadminapp.ui.home.ui.bookingtour

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.azhara.perintisadminapp.entity.*
import com.azhara.perintisadminapp.utils.FirebaseConstants

class BookingTourViewModel : ViewModel(){
    private val _bookingTourData = MutableLiveData<List<BookingTourData>>()
    val bookingTourData = _bookingTourData

    private val _userData = MutableLiveData<UserData>()
    val userData = _userData

    private val _userListDataBooking = MutableLiveData<UserListBookingData>()
    val userListDataBooking = _userListDataBooking

    private val _userDetailBookingTour = MutableLiveData<UserDetailBookingTour>()
    val userDetailBookingTour = _userDetailBookingTour

    private val _dataTour = MutableLiveData<TourData>()
    val dataTour = _dataTour

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    fun getDataBookingTour(){
        _isLoading.value = true
        val dbTour = FirebaseConstants.firebaseDb.collection("booking_tour")
        dbTour.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.postValue(error.message)
            }

            if (value != null){
                _bookingTourData.postValue(value.toObjects(BookingTourData::class.java))
            }
        }
    }

    fun getDataUser(id: String?){

        _isLoading.value = true

        val userDb = id?.let { FirebaseConstants.firebaseDb.collection("users").document(it) }
        userDb?.get()?.addOnCompleteListener {

            _isLoading.value = false

            if (it.isSuccessful){
                _userData.postValue(it.result?.toObject(UserData::class.java))
            }
            else{
                _msg.value = it.exception?.message
            }

        }
    }

    fun getListBookingUser(id: String?, idList: String?){
        _isLoading.value = true

        val listBookingDb = id?.let { idList?.let { it1 ->
            FirebaseConstants.firebaseDb.collection("users").document(it).collection("listBooking").document(
                it1
            )
        } }
        listBookingDb?.get()?.addOnCompleteListener {
            _isLoading.value = false

            if (it.isSuccessful){
                _userListDataBooking.postValue(it.result?.toObject(UserListBookingData::class.java))
            }
            else{
                _msg.value = it.exception?.message
            }

        }

    }

    fun getDetailBookingTourUser(id: String?, idDetailBookingTour: String?){
        _isLoading.value = true
        val detailBookingUserDb =
            id?.let { idDetailBookingTour?.let { it1 ->
                FirebaseConstants.firebaseDb.collection("users").document(it).collection("bookingTour").document(
                    it1
                )
            } }

        detailBookingUserDb?.get()?.addOnCompleteListener {
            _isLoading.value = false
            if (it.isSuccessful){
                _userDetailBookingTour.postValue(it.result?.toObject(UserDetailBookingTour::class.java))
            }
            else{
                _msg.value = it.exception?.message
            }

        }

    }

    fun getTour(id: String?){
        _isLoading.value = true
        val tourDb = id?.let { FirebaseConstants.firebaseDb.collection("tour").document(it) }
        tourDb?.get()?.addOnCompleteListener {
            _isLoading.value = false
            if (it.isSuccessful){
                _dataTour.postValue(it.result?.toObject(TourData::class.java))
            }
            else{
                _msg.value = it.exception?.message
            }

        }
    }

    fun confirmBookingTour(bookingTourData: BookingTourData){
        _isLoading.value = true

        val db = FirebaseConstants.firebaseDb

        //Update booking tour
        val dataBookingTour = db.collection("booking_tour")
            .whereEqualTo("dateTour", bookingTourData.dateTour)
            .whereEqualTo("idDetailBookingTourUser", bookingTourData.idDetailBookingTourUser)
            .whereEqualTo("idListBookingTourUser", bookingTourData.idListBookingTourUser)
            .whereEqualTo("userId", bookingTourData.userId)

        dataBookingTour.get().addOnCompleteListener {

            if (it.isSuccessful){

                val id = it.result?.documents?.get(0)?.id
                id?.let { it1 -> db.collection("booking_tour").document(it1).update("statusPayment", true) }
                    ?.addOnCompleteListener { confirm ->

                        if (!confirm.isSuccessful){
                            _isLoading.value = false
                            _msg.value = it.exception?.message
                        }
                        else{
                            //Update UserListBooking
                            bookingTourData.userId?.let {
                                bookingTourData.idListBookingTourUser?.let { it1 ->
                                    db.collection("users").document(it)
                                        .collection("listBooking").document(it1)
                                        .update("statusPayment", true)
                                }
                            }?.addOnCompleteListener { task ->
                                _isLoading.value = false
                                if (!task.isSuccessful){
                                    _msg.value = task.exception?.message
                                }
                                else{
                                    _msg.value = "success confirm"
                                }
                            }
                        }
                    }

            }
            else{
                _isLoading.value = false
                _msg.value = it.exception?.message
            }

        }

    }

    fun updateStatusBooking(userId: String?, idDetailBookingTour: String?){
        _isLoading.value = true
        val db = userId?.let {
            idDetailBookingTour?.let { it1 ->
                FirebaseConstants.firebaseDb.collection("users").document(it)
                    .collection("bookingTour").document(it1)
            }
        }
        db?.update("statusBooking", 1)?.addOnCompleteListener {
            _isLoading.value = false
            if (it.isSuccessful){
                _msg.value = "success update status booking"
            }
            else{
                _msg.value = it.exception?.message
            }
        }

    }

    fun deleteBookingTour(bookingTourData: BookingTourData){
        _isLoading.value = true

        val db = FirebaseConstants.firebaseDb

        //Delete booking tour
        val dataBookingTour = db.collection("booking_tour")
            .whereEqualTo("dateTour", bookingTourData.dateTour)
            .whereEqualTo("idDetailBookingTourUser", bookingTourData.idDetailBookingTourUser)
            .whereEqualTo("idListBookingTourUser", bookingTourData.idListBookingTourUser)
            .whereEqualTo("userId", bookingTourData.userId)

        dataBookingTour.get().addOnCompleteListener {

            if (it.isSuccessful){

                val id = it.result?.documents?.get(0)?.id
                id?.let { it1 -> db.collection("booking_tour").document(it1).delete() }
                    ?.addOnCompleteListener { confirm ->

                        if (!confirm.isSuccessful){
                            _isLoading.value = false
                            _msg.value = "Error delete booking_tour"
                        }
                        else{

                            //Delete UserListBooking
                            bookingTourData.userId?.let {
                                bookingTourData.idListBookingTourUser?.let { it1 ->
                                    db.collection("users").document(it)
                                        .collection("listBooking").document(it1)
                                        .delete()
                                }
                            }?.addOnCompleteListener {task ->

                                if (!task.isSuccessful){
                                    _isLoading.value = false
                                    _msg.value = task.exception?.message
                                }
                                else{
                                    //Delete UserDetailBooking
                                    bookingTourData.userId?.let {
                                        bookingTourData.idDetailBookingTourUser?.let { it1 ->
                                            db.collection("users").document(it)
                                                .collection("bookingTour").document(it1)
                                        }
                                    }?.delete()?.addOnCompleteListener { task2 ->
                                        _isLoading.value = false

                                        if (!task2.isSuccessful){
                                            _msg.value = task2.exception?.message
                                        }
                                        else{
                                            _msg.value = "success delete"
                                        }
                                    }
                                }

                            }

                        }
                    }

            }
            else{
                _isLoading.value = false
                _msg.value = it.exception?.message
            }

        }

    }

}