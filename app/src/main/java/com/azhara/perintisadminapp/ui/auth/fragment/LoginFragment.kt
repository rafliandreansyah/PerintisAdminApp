package com.azhara.perintisadminapp.ui.auth.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.azhara.perintisadminapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

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
            
        }
    }

}