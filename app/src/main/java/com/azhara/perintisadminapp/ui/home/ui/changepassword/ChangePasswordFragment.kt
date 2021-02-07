package com.azhara.perintisadminapp.ui.home.ui.changepassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentChangePasswordBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.utils.Helper


class ChangePasswordFragment : Fragment() {
    private val changePasswordViewModel: ChangePasswordViewModel by viewModels()
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding

    private var backPressedTime: Long? = 0
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (backPressedTime!! + 2000 > System.currentTimeMillis()) {
                toast.cancel()
                activity?.moveTaskToBack(true)
                activity?.finish()
            }
            else {
                toast = Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT)
                toast.show()
            }
            backPressedTime = System.currentTimeMillis()
        }
        // The callback can be enabled or disabled here or in the lambda

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkButtonActive()
        binding?.btnChangePassword?.setOnClickListener {
            binding?.btnChangePassword?.isEnabled = false
            checkChangePassword()
        }
        stateChangePassword()
        isLoading()
    }

    private fun checkButtonActive(){
        val watcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.layoutEdtOldPasswordChange?.error = null
                binding?.layoutEdtNewPasswordChange?.error = null
                binding?.layoutEdtConfirmNewPasswordChange?.error = null
                if (s?.length == 0){
                    binding?.btnChangePassword?.isEnabled = false
                }else{
                    binding?.btnChangePassword?.isEnabled =
                            binding?.edtOldPasswordChange?.text?.toString()?.trim()?.isNotBlank()!! &&
                                    binding?.edtNewPasswordChange?.text?.toString()?.trim()?.isNotBlank()!! &&
                            binding?.edtConfirmNewPasswordChange?.text?.toString()?.trim()?.isNotBlank()!!
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }
        binding?.edtOldPasswordChange?.addTextChangedListener(watcher)
        binding?.edtNewPasswordChange?.addTextChangedListener(watcher)
        binding?.edtConfirmNewPasswordChange?.addTextChangedListener(watcher)
    }

    private fun checkChangePassword(){
        val oldPassword = binding?.edtOldPasswordChange?.text?.toString()?.trim()
        val newPassword =  binding?.edtNewPasswordChange?.text?.toString()?.trim()
        val confirmNewPassword = binding?.edtConfirmNewPasswordChange?.text?.toString()?.trim()

        if (newPassword != confirmNewPassword){
            binding?.layoutEdtConfirmNewPasswordChange?.error = "Konfirmasi password tidak sama!"
        }else{
            changePasswordViewModel.changePassword(oldPassword, newPassword)
        }
    }

    private fun stateChangePassword(){
        changePasswordViewModel.msgInfo.observe(viewLifecycleOwner, { msgInfo ->
            if (msgInfo != null){
                binding?.btnChangePassword?.isEnabled = true
            }
            when (msgInfo) {
                "newPasswordIsSame" -> {
                    binding?.layoutEdtNewPasswordChange?.error = "Password baru tidak boleh sama!"
                }
                "success" -> {
                    context?.let { Helper.toast("Password berhasil diupdate", it) }
                    view?.findNavController()?.navigate(R.id.action_nav_change_password_to_nav_dashboard)
                }
                "The password is invalid or the user does not have a password." -> {
                    binding?.layoutEdtOldPasswordChange?.error = "Password lama salah!"
                }
                else -> {
                    binding?.containerChangePassword?.let { Helper.snackbar(msgInfo, it) }
                }
            }
        })
    }

    private fun isLoading(){
        changePasswordViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else if(isLoading == false){
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}