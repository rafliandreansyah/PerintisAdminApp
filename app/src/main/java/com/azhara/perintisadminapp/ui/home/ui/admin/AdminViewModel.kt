package com.azhara.perintisadminapp.ui.home.ui.admin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.AdminData
import com.azhara.perintisadminapp.entity.MitraData
import com.azhara.perintisadminapp.utils.FirebaseConstants
import com.azhara.perintisadminapp.utils.SingleLiveEvent

class AdminViewModel: ViewModel() {

    private val _dataAdmin = MutableLiveData<List<AdminData>>()
    val dataAdmin = _dataAdmin

    private val _dataAdminOnce = MutableLiveData<List<AdminData>>()
    val dataAdminOnce = _dataAdminOnce

    private val _msg = SingleLiveEvent<String>()
    val msg = _msg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getAdmin(){
        _isLoading.value = true
        val adminDb = FirebaseConstants.firebaseDb.collection("admin")
        adminDb.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.value = error.message
            }
            if (value != null){
                _dataAdmin.postValue(value.toObjects(AdminData::class.java))
            }
        }
    }

    fun getAdminOnce(){
        _isLoading.value = true
        val adminDb = FirebaseConstants.firebaseDb.collection("admin")
        adminDb.get().addOnCompleteListener {
            _isLoading.value = false
            if (it.isSuccessful){
                _dataAdminOnce.postValue(it.result?.toObjects(AdminData::class.java))
            }
            else{
                it.exception?.message?.let { it1 -> Log.e("AdminViewModel", it1) }
                _msg.value = it.exception?.message
            }
        }
    }

    fun addAdmin(email: String?, name: String?, phone: Long?, password: String?){

        _isLoading.value = true
        val auth = FirebaseConstants.firebaseAuth

        val dataUser = hashMapOf(
            "email" to email,
            "imgUrl" to null,
            "name" to name,
            "phone" to phone
        )

        //Membuat akun firebase auth
        email?.let { password?.let { it1 ->
            auth.createUserWithEmailAndPassword(it,
                it1
            )
        } }?.addOnCompleteListener { register ->

            if (register.isSuccessful){

                val userId = register.result?.user?.uid
                val adminDb = userId?.let {
                    FirebaseConstants.firebaseDb.collection("admin").document(
                        it
                    )
                }

                // Membuat document data admin pada firestore
                adminDb?.set(dataUser)?.addOnCompleteListener { addAdmin ->
                    _isLoading.value = false
                    if (addAdmin.isSuccessful){
                        _msg.value = "success"
                    }
                    else{
                        _msg.value = register.exception?.message
                    }

                }

            }
            else{
                _isLoading.value = false
                _msg.value = register.exception?.message
            }

        }

    }

    fun deleteAdmin(email: String?){
        _isLoading.value = true
        val adminDb = FirebaseConstants.firebaseDb.collection("admin").whereEqualTo("email", email)
        adminDb.get().addOnCompleteListener {

            if (it.isSuccessful){
                val adminId = it.result?.documents?.get(0)?.id

                adminId?.let { it1 ->

                    FirebaseConstants.firebaseDb.collection("admin").document(it1).delete().addOnCompleteListener { task ->

                        _isLoading.value = false

                        if (task.isSuccessful){
                            _msg.value = "Admin berhasil dihapus!"
                        }
                        else{
                            _msg.value = task.exception?.message
                        }

                    }

                }

            }
            else{
                _msg.value = it.exception?.message
            }

        }
    }

}