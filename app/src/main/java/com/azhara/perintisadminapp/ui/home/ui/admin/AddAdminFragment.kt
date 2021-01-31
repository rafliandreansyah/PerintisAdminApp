package com.azhara.perintisadminapp.ui.home.ui.admin

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
import com.azhara.perintisadminapp.databinding.FragmentAddAdminBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.utils.Helper

class AddAdminFragment : Fragment(), View.OnClickListener {

    private val adminViewModel: AdminViewModel by viewModels()

    private var _binding: FragmentAddAdminBinding? = null
    val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adminViewModel.getAdminOnce()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddAdminBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnAddAdmin?.setOnClickListener(this)

        checkBtnEnable()
        isLoading()
        msg()
    }

    private fun checkBtnEnable(){
        val watcher =  object : TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                binding?.layoutEdtEmailAddAdmin?.error = null
                binding?.layoutEdtConfirmPasswordAddAdmin?.error = null

                if (s?.length == 0){
                    binding?.btnAddAdmin?.isEnabled = false
                }else{
                    binding?.btnAddAdmin?.isEnabled = binding?.edtNameAddAdmin?.text?.toString()?.trim() != "" &&
                            binding?.edtEmailAddAdmin?.text?.toString()?.trim() != "" && binding?.edtPhoneAddAdmin?.text?.toString()?.trim() != "" &&
                            binding?.edtPasswordAddAdmin?.text?.toString()?.trim() != "" && binding?.edtConfirmPasswordAddAdmin?.text?.toString()?.trim() != ""
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        binding?.edtNameAddAdmin?.addTextChangedListener(watcher)
        binding?.edtEmailAddAdmin?.addTextChangedListener(watcher)
        binding?.edtPhoneAddAdmin?.addTextChangedListener(watcher)
        binding?.edtPasswordAddAdmin?.addTextChangedListener(watcher)
        binding?.edtConfirmPasswordAddAdmin?.addTextChangedListener(watcher)
    }

    private fun addAdmin(){

        // pengambilan data admin
        adminViewModel.dataAdminOnce.observe(viewLifecycleOwner, { dataAdmin ->

            if (dataAdmin != null){

                with(binding){

                    val edtName = this?.edtNameAddAdmin?.text.toString().trim()
                    val edtEmail = this?.edtEmailAddAdmin?.text.toString().trim()
                    val edtPhone = this?.edtPhoneAddAdmin?.text.toString().trim()
                    val edtPassword = this?.edtPasswordAddAdmin?.text.toString().trim()
                    val edtConfirmPassword = this?.edtConfirmPasswordAddAdmin?.text.toString().trim()

                    val admin = dataAdmin.filter {
                        it.email == edtEmail
                    }

                    //Pengecekan admin sudah terdaftar atau belum
                    if (admin.isNotEmpty()){
                        this?.layoutEdtEmailAddAdmin?.error = "Email sudah terdaftar!"
                        return@with
                    }

                    if (edtPassword != edtConfirmPassword){
                        this?.layoutEdtConfirmPasswordAddAdmin?.error = "Password tidak sama!"
                        return@with
                    }

                    adminViewModel.addAdmin(edtEmail, edtName, edtPhone.toLong(), edtPassword)
                }

            }

        })

    }

    private fun isLoading(){
        adminViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun msg(){
        adminViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg == "success"){
                view?.findNavController()?.navigate(R.id.action_addAdminFragment_to_nav_admin)
            }else{
                binding?.containerAddAdmin?.let { Helper.snackbar(msg, it) }
                binding?.btnAddAdmin?.isEnabled = false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddAdmin -> {
                addAdmin()
                binding?.btnAddAdmin?.isEnabled = false
            }
        }
    }

}