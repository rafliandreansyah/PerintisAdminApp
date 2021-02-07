package com.azhara.perintisadminapp.ui.home.ui.car

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.CarsData
import com.azhara.perintisadminapp.entity.UserData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class CarViewModel : ViewModel() {


    private val _carData = MutableLiveData<List<CarsData>>()
    val carData = _carData

    private val _userData = MutableLiveData<UserData>()
    val userData = _userData

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

    fun getDataUser(userId: String?){
        _isLoading.value = true
        val db = userId?.let { FirebaseConstants.firebaseDb.collection("users").document(it) }
        db?.get()?.addOnCompleteListener { task ->
            _isLoading.value = false
            if (task.isSuccessful){
                _userData.postValue(task.result?.toObject(UserData::class.java))
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
}