package com.azhara.perintisadminapp.ui.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentLoginBinding
import com.azhara.perintisadminapp.ui.auth.AuthViewModel
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    private val authViewModel: AuthViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        val authUser = FirebaseAuth.getInstance().currentUser
        if (authUser != null){
            startActivity(Intent(activity, HomeActivity::class.java))
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnLogin?.setOnClickListener(this)
        binding?.btnResetPassword?.setOnClickListener(this)

        textWatcher()
        isLoading()
        checkLogin()
    }

    private fun textWatcher(){
        val watcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0){
                    binding?.btnLogin?.isEnabled = false
                }else{
                    binding?.btnLogin?.isEnabled = binding?.edtEmailLogin?.text.toString().trim() != "" && binding?.edtPasswordLogin?.text.toString().trim() != ""
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        binding?.edtEmailLogin?.addTextChangedListener(watcher)
        binding?.edtPasswordLogin?.addTextChangedListener(watcher)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnResetPassword -> view?.findNavController()?.navigate(R.id.action_nav_login_fragment_to_nav_reset_password_fragment)
            R.id.btnLogin -> {
                authViewModel.login(binding?.edtEmailLogin?.text.toString().trim(), binding?.edtPasswordLogin?.text.toString().trim())
            }
        }
    }

    private fun checkLogin(){
        authViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg == "Login Sukses"){
                startActivity(Intent(activity, HomeActivity::class.java))
                activity?.finish()
            }else if(msg == "The password is invalid or the user does not have a password."){
                binding?.containerLogin?.let {
                    Snackbar.make(it, "Email or password wrong!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Try Again"){
                                binding?.edtEmailLogin?.requestFocus()
                                binding?.edtEmailLogin?.text = null
                                binding?.edtPasswordLogin?.text = null
                            }
                            .show()
                }
            }else{
                binding?.containerLogin?.let {
                    msg?.let { it1 ->
                        Snackbar.make(it, it1, Snackbar.LENGTH_INDEFINITE)
                                .setAction("Try Again"){
                                    binding?.edtEmailLogin?.requestFocus()
                                    binding?.edtEmailLogin?.text = null
                                    binding?.edtPasswordLogin?.text = null
                                }
                                .show()
                    }
                }
            }

        })
    }

    private fun isLoading(){
        authViewModel.isLoading.observe(viewLifecycleOwner, {loading ->
            if (loading == true){
                binding?.loadingLogin?.visibility = View.VISIBLE
                binding?.edtEmailLogin?.isEnabled = false
                binding?.edtPasswordLogin?.isEnabled = false
                binding?.btnLogin?.isEnabled = false
                binding?.btnResetPassword?.isEnabled = false
            }else{
                binding?.loadingLogin?.visibility = View.INVISIBLE
                binding?.edtEmailLogin?.isEnabled = true
                binding?.edtPasswordLogin?.isEnabled = true
                binding?.btnLogin?.isEnabled = true
                binding?.btnResetPassword?.isEnabled = true
            }
        })
    }

}