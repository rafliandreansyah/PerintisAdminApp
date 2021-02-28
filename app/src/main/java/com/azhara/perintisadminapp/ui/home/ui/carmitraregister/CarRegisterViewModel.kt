package com.azhara.perintisadminapp.ui.home.ui.carmitraregister

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.CarMitraRegisterData
import com.azhara.perintisadminapp.entity.MitraData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class CarRegisterViewModel : ViewModel(){

    private val _dataCarRegister = MutableLiveData<List<CarMitraRegisterData>>()
    val dataCarRegister = _dataCarRegister

    private val _dataMitra = MutableLiveData<MitraData>()
    val dataMitra = _dataMitra

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    fun getDataCarRegister(){
        _isLoading.value = true
        val db = FirebaseConstants.firebaseDb.collection("car_register")
        db.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.value = error.message
            }
            if (value != null){
                _dataCarRegister.postValue(value.toObjects(CarMitraRegisterData::class.java))
            }
        }
    }

    fun loadDataMitra(id: String?){
        _isLoading.value = true
        val db = id?.let { FirebaseConstants.firebaseDb.collection("partners").document(it) }
        db?.get()?.addOnCompleteListener {
            _isLoading.value = false
            if (it.isSuccessful){
                _dataMitra.postValue(it.result?.toObject(MitraData::class.java))
            }
            else{
                _msg.value = it.exception.toString()
            }
        }
    }

    fun statusConfirm(data: CarMitraRegisterData ,confirmType: Int?){
        _isLoading.value = true
        val db = FirebaseConstants.firebaseDb.collection("car_register")
            .whereEqualTo("carNumber", data.carNumber).whereEqualTo("carType", data.carType)
            .whereEqualTo("carYear", data.carYear).whereEqualTo("partnerId", data.partnerId)

        db.get().addOnCompleteListener { dataCarRegister ->

            if (dataCarRegister.isSuccessful){

                val id = dataCarRegister.result?.documents?.get(0)?.id
                val carRegisterDB =
                    id?.let { FirebaseConstants.firebaseDb.collection("car_register").document(it) }
                carRegisterDB?.update("statusConfirm", confirmType)?.addOnCompleteListener {
                    _isLoading.value = false
                    if (it.isSuccessful){
                        if (confirmType == 1){
                            _msg.value = "Success confirm"
                        }
                        else{
                            _msg.value = "Reject"
                        }
                    }
                    else{
                        _msg.value = it.exception.toString()
                    }
                }

            }
            else{
                _isLoading.value = false
                _msg.value = dataCarRegister.exception.toString()
            }

        }
    }

}