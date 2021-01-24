package com.azhara.perintisadminapp.ui.home.ui.changepassword

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.utils.FirebaseConstants
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider

class ChangePasswordViewModel : ViewModel(){

    private val _msgInfo = MutableLiveData<String>()
    val msgInfo = _msgInfo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun changePassword(oldPassword: String?, newPassword: String?){
        _isLoading.value = true
        val emailUser = FirebaseConstants.firebaseAuth.currentUser?.email
        val reAuth = FirebaseConstants.firebaseAuth.currentUser
        val credential = emailUser?.let { oldPassword?.let { it1 -> EmailAuthProvider.getCredential(it, it1) } }
        credential?.let {
            reAuth?.reauthenticate(it)?.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    _isLoading.value = false
                    if (oldPassword ==  newPassword){
                        _isLoading.value = false
                        _msgInfo.value = "newPasswordIsSame"
                    }else{
                        updatePassword(newPassword)
                    }
                }else{
                    _isLoading.value = false
                    _msgInfo.value = task.exception?.message
                    task.exception?.message?.let { it1 -> Log.e("ChangePasswordReAuth", it1) }
                }
            }
        }
    }

    private fun updatePassword(newPassword: String?){
        val updatePassword = FirebaseConstants.firebaseAuth.currentUser
        newPassword?.let {
            updatePassword?.updatePassword(it)?.addOnCompleteListener {task ->
                if (task.isSuccessful){
                    _isLoading.value = false
                    _msgInfo.value = "success"
                }else{
                    _isLoading.value = false
                    _msgInfo.value = task.exception?.message
                    task.exception?.message?.let { it1 -> Log.e("ChangePasswordUpdate", it1) }
                }
            }
        }
    }

}