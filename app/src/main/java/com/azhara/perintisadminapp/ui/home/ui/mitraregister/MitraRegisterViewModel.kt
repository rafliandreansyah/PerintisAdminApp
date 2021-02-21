package com.azhara.perintisadminapp.ui.home.ui.mitraregister

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.MitraRegisterData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class MitraRegisterViewModel : ViewModel(){

    private val _dataMitraRegister = MutableLiveData<List<MitraRegisterData>>()
    val dataMitraRegister = _dataMitraRegister

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getDataMitraRegister(){
        _isLoading.value = true
        val db = FirebaseConstants.firebaseDb.collection("partner_register")
        db.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.value = error.message
            }

            if (value != null){
                _dataMitraRegister.postValue(value.toObjects(MitraRegisterData::class.java))
            }
        }
    }

    fun statusConfirmMitraRegister(data: MitraRegisterData?, statusConfirm: String?){
        _isLoading.value = true
        val db = FirebaseConstants.firebaseDb.collection("partner_register")
            .whereEqualTo("email", data?.email)
            .whereEqualTo("name", data?.name)
            .whereEqualTo("phoneNumber", data?.phoneNumber)
            .whereEqualTo("carType", data?.carType)
            .whereEqualTo("carNumberRegister", data?.carNumberRegister)
        db.get().addOnCompleteListener {
            _isLoading.value = true

            if (it.isSuccessful){
                val id = it.result?.documents?.get(0)?.id

                var isConfirm = 1 //1 Menunjukan diterima
                if (statusConfirm == "reject"){
                    isConfirm = 0 //dan 0 Ditolak
                }

                val mitraData =
                    id?.let { it1 ->
                        FirebaseConstants.firebaseDb.collection("partner_register").document(
                            it1
                        )
                    }
                mitraData?.update("statusConfirm", isConfirm)?.addOnCompleteListener { confirm ->
                    _isLoading.value = false
                    if (confirm.isSuccessful){

                        if (isConfirm == 1){
                            _msg.value = "Success confirm"
                        }
                        else{
                            _msg.value = "Reject"
                        }

                    }
                    else{
                        _msg.value = confirm.exception?.message
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