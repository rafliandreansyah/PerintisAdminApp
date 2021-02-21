package com.azhara.perintisadminapp.ui.home.ui.tour

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.TourData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class TourViewModel : ViewModel() {

    private val _tourData = MutableLiveData<List<TourData>>()
    val tourData = _tourData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    private val id = FirebaseConstants.firebaseAuth.uid

    fun getDataTour(){
        _isLoading.value = true
        val dbTour = FirebaseConstants.firebaseDb.collection("tour")
        dbTour.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.postValue(error.message)
            }

            if (value != null){
                _tourData.postValue(value.toObjects(TourData::class.java))
            }
        }
    }

    fun addTour(
        imgTour: ByteArray, facility: ArrayList<String>?, locationTour: String?,
        visitedTour: ArrayList<String>?, tourName: String?, timeTour: String?,
        transport: String?, durationTour: String?, capacity: Int?, price: Long?, ){

        _isLoading.value = true

        val tourStorage = tourName?.let {
            FirebaseConstants.firebaseStorage.reference.child("tour").child(
                it
            )
        }
        val uploadTask = tourStorage?.putBytes(imgTour)
        uploadTask?.addOnFailureListener {
            _msg.value = "Gagal upload gambar"
        }?.addOnSuccessListener {
            (uploadTask).continueWithTask { task ->

                if (!task.isSuccessful){
                    _isLoading.value = false
                    _msg.value = task.exception?.message
                }
                tourStorage.downloadUrl
            }.addOnCompleteListener {

                if (it.isSuccessful){

                    //Image URL
                    val downloadUri = it.result

                    val dataTour = hashMapOf(
                        "capacity" to capacity,
                        "durationTour" to durationTour,
                        "facilities" to facility,
                        "imgUrl" to downloadUri.toString(),
                        "locationTour" to locationTour,
                        "partnerId" to id,
                        "price" to price,
                        "statusReady" to true,
                        "timeTour" to timeTour,
                        "vehicle" to transport,
                        "visitedTour" to visitedTour,
                        "tourName" to tourName
                    )

                    val tourDb = FirebaseConstants.firebaseDb.collection("tour")
                    tourDb.add(dataTour).addOnSuccessListener {

                        _isLoading.value = false
                        _msg.value = "Berhasil tambah tour"

                    }.addOnFailureListener { e ->

                        _isLoading.value = false
                        _msg.value = e.message


                    }

                }

            }
        }?.addOnFailureListener { e ->

            _isLoading.value = false
            _msg.value = e.message

        }

    }

    fun changeStatus(tourData: TourData, status: Boolean) {
        _isLoading.value = true
        val getTour = FirebaseConstants.firebaseDb.collection("tour")
            .whereEqualTo("price", tourData.price).whereEqualTo("tourName", tourData.tourName)
            .whereEqualTo("timeTour", tourData.timeTour).whereEqualTo("locationTour", tourData.locationTour)

        getTour.get().addOnCompleteListener {

            if (it.isSuccessful){

                //Pengambilan id tour
                val idTour = it.result?.documents?.get(0)?.id

                // Load tour database
                val tour = idTour?.let { it1 ->
                    FirebaseConstants.firebaseDb.collection("tour").document(
                        it1
                    )
                }

                //Update tour
                tour?.update("statusReady", status)?.addOnCompleteListener { update ->

                    _isLoading.value = false

                    if(update.isSuccessful){

                        if (status){
                            _msg.value = "Wisata berhasil di aktifkan"
                        }
                        else{
                            _msg.value = "Wisata berhasil di non-aktifkan"
                        }

                    }
                    else{
                        _msg.value = update.exception?.message
                    }

                }

            }
            else{
                _isLoading.value = false
                _msg.value = it.exception?.message
            }
        }

    }

    fun deleteTour(tourData: TourData){
        _isLoading.value = true
        val getTour = FirebaseConstants.firebaseDb.collection("tour")
            .whereEqualTo("price", tourData.price).whereEqualTo("tourName", tourData.tourName)
            .whereEqualTo("timeTour", tourData.timeTour).whereEqualTo("locationTour", tourData.locationTour)

        getTour.get().addOnCompleteListener {

            if (it.isSuccessful){

                //Pengambilan id tour
                val idTour = it.result?.documents?.get(0)?.id

                // Load tour database
                val tour = idTour?.let { it1 ->
                    FirebaseConstants.firebaseDb.collection("tour").document(
                        it1
                    )
                }

                tour?.delete()?.addOnCompleteListener { delete ->

                    _isLoading.value = false

                    if (delete.isSuccessful){
                        _msg.value = "Wisata berhasil dihapus"
                    }
                    else{
                        _msg.value = delete.exception?.message
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