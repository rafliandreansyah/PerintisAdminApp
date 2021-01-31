package com.azhara.perintisadminapp.ui.home.ui.bookingcar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.BookingData
import com.azhara.perintisadminapp.entity.CarsData
import com.azhara.perintisadminapp.entity.UserData
import com.azhara.perintisadminapp.entity.UserListBookingData
import com.azhara.perintisadminapp.utils.FirebaseConstants
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BookingCarViewModel : ViewModel(){

    private val _bookingCarData = MutableLiveData<List<BookingData>>()
    val bookingCar = _bookingCarData

    private val _userData = MutableLiveData<UserData>()
    val userData =_userData

    private val _carData = MutableLiveData<CarsData>()
    val carData = _carData

    private val _listBookingUserData = MutableLiveData<UserListBookingData>()
    val listBookingUserData = _listBookingUserData

    val _msg = MutableLiveData<String>()
    val msg = _msg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getDataBooking(){
        _isLoading.value = true
        val bookingDataDb = FirebaseConstants.firebaseDb.collection("booking_data")
        bookingDataDb.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.value = error.message
            }
            if (value != null){
                val listBookingCarData = ArrayList<BookingData>()
                for (doc in value){
                    val id =  doc.id
                    val bookedIdPartner = doc["bookedIdPartner"]
                    val bookedListIdUser = doc["bookedListIdUser"]
                    val carId = doc["carId"]
                    val carName = doc["carName"]
                    val driver = doc["driver"]
                    val duration = doc["duration"].toString()
                    val endDate = doc["endDate"]
                    val partnerId = doc["partnerId"]
                    val pickUpArea = doc["pickUpArea"]
                    val startDate = doc["startDate"]
                    val statusBooking = doc["statusBooking"].toString() //null Belum terkonfirmasi, 0 on Progress dan 1 Selesai
                    val totalPrice = doc["totalPrice"]
                    val userId = doc["userId"]
                    val userImgUrl = doc["userImgUrl"]
                    val userName = doc["userName"]

                    listBookingCarData.add(BookingData(id, bookedIdPartner as String?, bookedListIdUser as String?, carId as String?, carName as String?, driver as String?, duration.toInt(),
                            endDate as Timestamp?, partnerId as String?, pickUpArea as String?, startDate as Timestamp?, statusBooking, totalPrice as Long?,
                            userId as String?, userImgUrl as String?, userName as String?))
                }
                _bookingCarData.postValue(listBookingCarData)
            }
        }
    }

    fun getDataUser(userId: String){
        _isLoading.value = true
        val db = FirebaseConstants.firebaseDb.collection("users").document(userId)
        db.get().addOnCompleteListener {
            _isLoading.value = false
            if (it.isSuccessful){
                _userData.postValue(it.result?.toObject(UserData::class.java))
            }else{
                _msg.value = it.exception?.message
            }
        }
    }

    fun getDataCar(carId: String){
        _isLoading.value = true
        val db = FirebaseConstants.firebaseDb.collection("cars").document(carId)
        db.get().addOnCompleteListener {
            _isLoading.value = false
            if (it.isSuccessful){
                _carData.postValue(it.result?.toObject(CarsData::class.java))
            }else{
                _msg.value = it.exception?.message
            }
        }
    }

    fun getDataListBookingUserId(bookingIdListUser: String?, userId: String?){
        _isLoading.value = true
        val db = userId?.let {
            bookingIdListUser?.let { it1 ->
                FirebaseConstants.firebaseDb.collection("users").document(it)
                    .collection("listBooking").document(it1)
            }
        }

        db?.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.value = error.message
            }

            if (value?.exists() == true){
                listBookingUserData.postValue(value.toObject(UserListBookingData::class.java))
            }
        }
    }

    fun confirmBooking(bookingIdListUser: String?, userId: String?, bookingCarId: String?, partnerId: String?, partnerBookingId: String?){
        _isLoading.value = true

        val dbListUser = userId?.let { bookingIdListUser?.let { it1 -> FirebaseConstants.firebaseDb.collection("users").document(it).collection("listBooking").document(it1) } }
        val dbBookingCar = bookingCarId?.let { FirebaseConstants.firebaseDb.collection("booking_data").document(it) }
        val dbCarPartner = partnerId?.let { partnerBookingId?.let { it1 -> FirebaseConstants.firebaseDb.collection("partners").document(it).collection("bookingCar").document(it1) } }

        dbListUser?.update("statusPayment", true)?.addOnCompleteListener {
            if (it.isSuccessful){

                dbBookingCar?.update("statusBooking", "0")?.addOnCompleteListener { task ->

                    if (task.isSuccessful){

                        dbCarPartner?.update("statusBooking", 0)?.addOnCompleteListener { partnerTask ->

                            if (partnerTask.isSuccessful){
                                _isLoading.value = false
                                _msg.value = "Booking berhasil dikonfirmasi"
                            }
                            else{
                                _msg.value = partnerTask.exception?.message
                            }

                        }

                    }
                    else{
                        _isLoading.value = false
                        _msg.value = task.exception?.message
                    }

                }

            }
            else{
                _isLoading.value = false
                _msg.value = it.exception?.message
            }
        }
    }

    fun endBooking(userBookingCarId: String?, userId: String?, bookingCarId: String?, partnerId: String?, partnerBookingId: String?){

        _isLoading.value = true

        val dbUserBookingCar = userId?.let { userBookingCarId?.let { it1 -> FirebaseConstants.firebaseDb.collection("users").document(it).collection("bookingCar").document(it1) } }
        val dbBookingCar = bookingCarId?.let { FirebaseConstants.firebaseDb.collection("booking_data").document(it) }
        val dbCarPartner = partnerId?.let { partnerBookingId?.let { it1 -> FirebaseConstants.firebaseDb.collection("partners").document(it).collection("bookingCar").document(it1) } }

        dbUserBookingCar?.update("statusBooking", 1)?.addOnCompleteListener {
            if (it.isSuccessful){

                dbBookingCar?.update("statusBooking", "1")?.addOnCompleteListener { task ->

                    if (task.isSuccessful){

                        dbCarPartner?.update("statusBooking", 1)?.addOnCompleteListener { partnerTask ->

                            _isLoading.value = false

                            if (partnerTask.isSuccessful){
                                _msg.value = "Booking selesai"
                            }
                            else{
                                _msg.value = partnerTask.exception?.message
                            }

                        }

                    }
                    else{
                        _isLoading.value = false
                        _msg.value = task.exception?.message
                    }

                }

            }
            else{
                _isLoading.value = false
                _msg.value = it.exception?.message
            }
        }
    }

    fun deleteBookingCar(carId: String?, userId: String?, startDate: Timestamp?, endDate: Timestamp?, userBookingListId: String?, userBookingCarId: String?, partnerId: String?, partnerBookingCarId: String?, bookingDataId: String?){
        _isLoading.value = true

        val carDb = carId?.let { FirebaseConstants.firebaseDb.collection("cars").document(it) }
        val userListDb = userId?.let { userBookingListId?.let { it1 -> FirebaseConstants.firebaseDb.collection("users").document(it).collection("listBooking").document(it1) } }
        val userBookingCarIdDb = userId?.let { userBookingCarId?.let { it1 -> FirebaseConstants.firebaseDb.collection("users").document(it).collection("bookingCar").document(it1) } }
        val partnerBookingDb = partnerId?.let { partnerBookingCarId?.let { it1 -> FirebaseConstants.firebaseDb.collection("partners").document(it).collection("bookingCar").document(it1) } }
        val bookingCarData = bookingDataId?.let { FirebaseConstants.firebaseDb.collection("booking_data").document(it) }

        val bookingDataCar = hashMapOf(
                "startDate" to startDate,
                "endDate" to endDate,
                "userId" to userId
        )

        // Menghapus list booking pada document car
        carDb?.update("booked", FieldValue.arrayRemove(bookingDataCar))?.addOnCompleteListener { taskDeleteBookedCar ->

            if (taskDeleteBookedCar.isSuccessful){
                _isLoading.value = false
            }
            else{
                _isLoading.value = false
                _msg.value = taskDeleteBookedCar.exception?.message
            }

        }

        // Menghapus document detail booking car pada user
        userBookingCarIdDb?.delete()?.addOnCompleteListener { deleteBookingCarDb ->

            if (deleteBookingCarDb.isSuccessful){
                _isLoading.value = false
            }
            else{
                _isLoading.value = false
                _msg.value = deleteBookingCarDb.exception?.message
            }

        }

        // Menghapus list booking document pada user
        userListDb?.delete()?.addOnCompleteListener { deleteListUser ->

            if (deleteListUser.isSuccessful){
                _isLoading.value = false
            }
            else{
                _isLoading.value = false
                _msg.value = deleteListUser.exception?.message
            }

        }

        // Menghapus list booking car document pada partner
        partnerBookingDb?.delete()?.addOnCompleteListener { deletePartnerBookingDb ->

            if (deletePartnerBookingDb.isSuccessful){
                _isLoading.value = false
            }
            else{
                _isLoading.value = false
                _msg.value = deletePartnerBookingDb.exception?.message
            }

        }

        // Menghapus data booking car document
        bookingCarData?.delete()?.addOnCompleteListener { deleteBookingDataCar ->

            if (deleteBookingDataCar.isSuccessful){
                _msg.value = "booking data deleted"
                _isLoading.value = false
            }
            else{
                _isLoading.value = false
                _msg.value = deleteBookingDataCar.exception?.message
            }

        }

    }

}