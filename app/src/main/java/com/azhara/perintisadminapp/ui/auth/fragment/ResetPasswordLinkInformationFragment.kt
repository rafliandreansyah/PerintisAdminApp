package com.azhara.perintisadminapp.ui.auth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentResetPasswordBinding
import com.azhara.perintisadminapp.databinding.FragmentResetPasswordLinkInformationBinding

class ResetPasswordLinkInformationFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentResetPasswordLinkInformationBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordLinkInformationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnBackToLogin?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnBackToLogin -> view?.findNavController()?.navigate(R.id.action_nav_reset_password_link_information_fragment_to_nav_login_fragment)
        }
    }
}