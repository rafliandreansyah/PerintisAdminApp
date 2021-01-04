package com.azhara.perintisadminapp.ui.auth.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentResetPasswordBinding
import com.azhara.perintisadminapp.ui.auth.AuthViewModel
import com.google.android.material.snackbar.Snackbar

class ResetPasswordFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnSendLink?.setOnClickListener(this)
        textWatcher()
        checkSendLink()
        isLoading()
    }

    private fun textWatcher(){
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0){
                    binding?.btnSendLink?.isEnabled = false
                }else{
                    binding?.btnSendLink?.isEnabled = binding?.edtEmailResetPassword?.text.toString().trim() != ""
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        binding?.edtEmailResetPassword?.addTextChangedListener(watcher)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnSendLink -> authViewModel.sendPasswordResetEmail(binding?.edtEmailResetPassword?.text.toString().trim())
        }
    }

    private fun checkSendLink(){
        authViewModel.msg.observe(viewLifecycleOwner, {msg ->
            if (msg == "Kirim Link Berhasil"){
                view?.findNavController()?.navigate(R.id.action_nav_reset_password_fragment_to_nav_reset_password_link_information_fragment)
            }else{
                binding?.containerSendLinkResetPassword?.let { msg?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_INDEFINITE) } }
                        ?.setAction("Try Again"){
                            binding?.edtEmailResetPassword?.requestFocus()
                            binding?.edtEmailResetPassword?.text = null
                        }
                        ?.show()
            }
        })
    }

    private fun isLoading(){
        authViewModel.isLoading.observe(viewLifecycleOwner, { loading ->
            if (loading == true){
                binding?.loadingSendLinkResetPassword?.visibility = View.VISIBLE
                binding?.edtEmailResetPassword?.isEnabled = false
                binding?.btnSendLink?.isEnabled = false
            }else{
                binding?.loadingSendLinkResetPassword?.visibility = View.INVISIBLE
                binding?.edtEmailResetPassword?.isEnabled = true
                binding?.btnSendLink?.isEnabled = true
            }
        })
    }

}