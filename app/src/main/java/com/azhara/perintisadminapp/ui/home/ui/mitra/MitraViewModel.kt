package com.azhara.perintisadminapp.ui.home.ui.mitra

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.MitraData
import com.azhara.perintisadminapp.entity.UserData
import com.azhara.perintisadminapp.utils.FirebaseConstants
import com.azhara.perintisadminapp.utils.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth

class MitraViewModel: ViewModel() {

    private val _dataMitra = MutableLiveData<List<MitraData>>()
    val dataMitra = _dataMitra

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    private val _isLoading = SingleLiveEvent<Boolean>()
    val isLoading = _isLoading

    fun getMitra(){
        _isLoading.value = true
        val mitraDb = FirebaseConstants.firebaseDb.collection("partners")
        mitraDb.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.value = error.message
            }
            if (value != null){
                _dataMitra.postValue(value.toObjects(MitraData::class.java))
            }
        }
    }

    fun addMitra(name: String?, email: String?, phone: Long?, address: String?, travelName: String?, password: String?){
        _isLoading.value = true
        val auth = email?.let { password?.let { it1 -> FirebaseAuth.getInstance().createUserWithEmailAndPassword(it, it1) } }
        auth?.addOnCompleteListener {

            if (it.isSuccessful){
                val partner = MitraData(
                        address,
                        email,
                        null,
                        name,
                        phone,
                        true,
                        travelName
                )
                val partnerId = it.result?.user?.uid
                val partnerDb = partnerId?.let { it1 -> FirebaseConstants.firebaseDb.collection("partners").document(it1) }
                partnerDb?.set(partner)?.addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful){
                        _msg.value = "Add Mitra Success"
                    }
                    else{
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

    fun editStatusActive(email: String?, statusActive: Boolean?){
        _isLoading.value = true
        val db = FirebaseConstants.firebaseDb.collection("partners").whereEqualTo("email", email)
        db.get().addOnCompleteListener { getMitra ->
            if (getMitra.isSuccessful){

                //Get mitra id
                val mitraId = getMitra.result?.documents?.get(0)?.id

                val mitraDb = mitraId?.let { FirebaseConstants.firebaseDb.collection("partners").document(it) }
                mitraDb?.update("statusActive", statusActive)?.addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful){

                        if (statusActive == true){
                            _msg.value = "Mitra sudah aktif"
                        }
                        else{
                            _msg.value = "Mitra di non-aktifkan"
                        }

                    }
                    else{
                        _msg.value = task.exception?.message
                    }
                }

            }
            else{
                _isLoading.value = false
                _msg.value = getMitra.exception?.message
            }
        }

    }

    fun delete(email: String?){
        _isLoading.value = true
        val db = FirebaseConstants.firebaseDb.collection("partners")

        //get mitra by email
        val filterByEmail = db.whereEqualTo("email", email)
        filterByEmail.get().addOnCompleteListener { getMitra ->

            if (getMitra.isSuccessful){

                //get mitra id
                val mitraId = getMitra.result?.documents?.get(0)?.id

                //delete mitra
                mitraId?.let {
                    db.document(it).delete().addOnCompleteListener { delete ->
                        _isLoading.value = false
                        if (delete.isSuccessful){
                            _msg.value = "Mitra dihapus."
                        }
                        else{
                            _msg.value = delete.exception?.message
                        }
                    }
                }

            }
            else{
                _isLoading.value = false
                _msg.value = getMitra.exception?.message
            }

        }
    }

}