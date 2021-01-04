package com.azhara.perintisadminapp.ui.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class AuthViewModel : ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _msg = MutableLiveData<String?>()
    val msg = _msg

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading = _isLoading

    fun login(email: String?, password: String?){
        _isLoading.value = true
        val adminLogin = email?.let { password?.let { it1 ->
            auth.signInWithEmailAndPassword(it,
                it1
            )
        } }

        adminLogin?.addOnCompleteListener { login ->
            if (login.isSuccessful){
                val adminId = login.result?.user?.uid
                checkIsAdmin(adminId)
            }else{
                _isLoading.value = false
                _msg.value = login.exception?.message
                login.exception?.message?.let { Log.e("AuthViewModel", it) }
            }
        }
    }

    private fun checkIsAdmin(adminId: String?){
        val adminDb = adminId?.let { db.collection("admin").document(it) }
        adminDb?.get()?.addOnCompleteListener { task ->
            _isLoading.value = false
            if (task.isSuccessful){
                val admin = task.result
                if (admin != null && admin.exists()){
                    _msg.value = "Login Sukses"
                }else{
                    _msg.value = "Email Don't Have Access"
                }
            }else{
                _msg.value = task.exception?.message
            }
        }
    }

}