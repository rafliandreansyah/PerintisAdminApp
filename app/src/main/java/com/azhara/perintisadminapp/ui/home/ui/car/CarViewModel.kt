package com.azhara.perintisadminapp.ui.home.ui.car

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.CarsData
import com.azhara.perintisadminapp.entity.MitraData
import com.azhara.perintisadminapp.entity.UserData
import com.azhara.perintisadminapp.utils.FirebaseConstants
import java.lang.reflect.Array
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CarViewModel : ViewModel() {


    private val _carData = MutableLiveData<List<CarsData>>()
    val carData = _carData

    private val _mitraData = MutableLiveData<MitraData>()
    val mitraData = _mitraData

    private val _listMitraData = MutableLiveData<List<MitraData>>()
    val listMitraData = _listMitraData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    fun getDataCar(){
        _isLoading.value = true
        val db = FirebaseConstants.firebaseDb.collection("cars")
        db.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.postValue(error.message)
            }
            if (value != null){
                _carData.postValue(value.toObjects(CarsData::class.java))
            }
        }
    }

    fun getDataPartner(userId: String?){
        _isLoading.value = true
        val db = userId?.let { FirebaseConstants.firebaseDb.collection("partners").document(it) }
        db?.get()?.addOnCompleteListener { task ->
            _isLoading.value = false
            if (task.isSuccessful){
                _mitraData.postValue(task.result?.toObject(MitraData::class.java))
            }
            else{
                _msg.value = task.exception?.message
            }

        }
    }

    fun editStatusActive(carNoRegister: String?, isActive: Boolean?){
        _isLoading.value = true
        val carDB = FirebaseConstants.firebaseDb.collection("cars")
        val loadCar = carDB.whereEqualTo("carNumberPlate", carNoRegister)
        loadCar.get().addOnCompleteListener { loadCarTask ->

            if (loadCarTask.isSuccessful){
                //Car id
                val carId = loadCarTask.result?.documents?.get(0)?.id

                //Edit status active car
                carId?.let { carDB.document(it).update("statusReady", isActive) }?.addOnCompleteListener { editTask ->
                    _isLoading.value = false
                    if (editTask.isSuccessful){

                        if (isActive == false){
                            _msg.value = "Berhasil di non-aktifkan"
                        }
                        else{
                            _msg.value = "Berhasil di aktifkan"
                        }

                    }
                    else{
                        _msg.value = editTask.exception?.message
                    }

                }
            }
            else{
                _isLoading.value = false
                _msg.value = loadCarTask.exception?.message
            }

        }
    }

    fun delete(carNoRegister: String?){
        _isLoading.value = true
        val carDb = FirebaseConstants.firebaseDb.collection("cars")
        val loadCar = carDb.whereEqualTo("carNumberPlate", carNoRegister)
        loadCar.get().addOnCompleteListener { taskLoadCar ->

            if (taskLoadCar.isSuccessful){
                //carId
                val carId = taskLoadCar.result?.documents?.get(0)?.id

                //deleteCar
                carId?.let { carDb.document(it).delete() }?.addOnCompleteListener { deleteCar ->
                    _isLoading.value = false
                    if (deleteCar.isSuccessful){
                        _msg.value = "Delete berhasil"
                    }
                    else{
                        _msg.value = deleteCar.exception?.message
                    }
                }
            }
            else{
                _isLoading.value = false
                msg.value = taskLoadCar.exception?.message
            }

        }
    }

    fun getDataMitra(){
        _isLoading.value = true
        val mitraDb = FirebaseConstants.firebaseDb.collection("partners")
        mitraDb.get().addOnCompleteListener { task ->

            _isLoading.value = false
            if (task.isSuccessful){
                _listMitraData.postValue(task.result?.toObjects(MitraData::class.java))
            }
            else{
                _msg.value = task.exception?.message
            }

        }

    }

    fun addCar(imgCar: ByteArray, email: String?, ownerName: String?, typeCar: String?, transmission: Int?,
               carYear: Long?, noRegister: String?, capacityPerson: Int?, capacityLuggage: Int?, price: Long?){
        _isLoading.value = true

        val mitraDb = FirebaseConstants.firebaseDb.collection("partners").whereEqualTo("email", email)
        mitraDb.get().addOnCompleteListener { mitraTask ->

            if (mitraTask.isSuccessful){

                val ownerId = mitraTask.result?.documents?.get(0)?.id
                val imgCarStorage = ownerId?.let { typeCar?.let { it1 -> noRegister?.let { it2 -> FirebaseConstants.firebaseStorage.reference.child("car").child(it).child(it1).child(it2) } } }
                val uploadTask = imgCarStorage?.putBytes(imgCar)
                uploadTask?.addOnFailureListener {
                    _msg.value = "Gagal upload gambar"
                }?.addOnSuccessListener {
                    (uploadTask).continueWithTask { task ->

                        if (!task.isSuccessful){
                            _isLoading.value = false
                            _msg.value = task.exception?.message
                        }
                        imgCarStorage.downloadUrl
                    }.addOnCompleteListener {

                        if (it.isSuccessful){
                            //Image URL
                            val downloadUri = it.result


                            // Add car data firestore
                            val carData = hashMapOf(
                                    "booked" to emptyList<HashMap<String, Any>>(),
                                    "capacity" to capacityPerson,
                                    "carName" to typeCar,
                                    "carNumberPlate" to noRegister,
                                    "carOwnerName" to ownerName,
                                    "gear" to transmission,
                                    "imgUrl" to downloadUri.toString(),
                                    "luggage" to capacityLuggage,
                                    "partnerId" to ownerId,
                                    "price" to price,
                                    "statusReady" to true,
                                    "year" to carYear
                            )

                            val carDB = FirebaseConstants.firebaseDb.collection("cars")
                            carDB.add(carData).addOnSuccessListener {

                                _isLoading.value = false
                                _msg.value = "Berhasil tambah mobil"

                            }.addOnFailureListener {e ->

                                _isLoading.value = false
                                _msg.value = e.message

                            }

                        }
                        else{
                            _isLoading.value = false
                            _msg.value = it.exception?.message
                        }

                    }
                }

            }
            else{
                _isLoading.value = false
                _msg.value = mitraTask.exception?.message
            }

        }

    }

}