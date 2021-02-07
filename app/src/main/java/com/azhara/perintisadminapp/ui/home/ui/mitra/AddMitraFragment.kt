package com.azhara.perintisadminapp.ui.home.ui.mitra

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
import com.azhara.perintisadminapp.databinding.FragmentAddMitraBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.utils.Helper

class AddMitraFragment : Fragment(), View.OnClickListener {

    private val mitraViewModel: MitraViewModel by viewModels()

    private var _binding: FragmentAddMitraBinding? = null
    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddMitraBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnAddMitra?.setOnClickListener(this)

        checkButtonEnable()
        isLoading()
        msg()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddMitra -> {
                addMitra()
            }
        }
    }

    private fun checkButtonEnable(){

        val watcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0){
                    binding?.btnAddMitra?.isEnabled = false
                }
                else{
                    binding?.btnAddMitra?.isEnabled =
                            binding?.edtNameAddMitra?.text.toString().trim() != "" && binding?.edtEmailAddMitra?.text.toString().trim() != "" &&
                                    binding?.edtPhoneAddMitra?.text.toString().trim() != "" && binding?.edtAddressAddMitra?.text.toString() != "" &&
                                    binding?.edtTravelNameAddMitra?.text.toString().trim() != "" && binding?.edtPasswordAddMitra?.text.toString().trim() != "" &&
                                    binding?.edtConfirmPasswordAddMitra?.text.toString().trim() != ""

                }

            }

            override fun afterTextChanged(s: Editable?) {}

        }

        binding?.edtNameAddMitra?.addTextChangedListener(watcher)
        binding?.edtEmailAddMitra?.addTextChangedListener(watcher)
        binding?.edtPhoneAddMitra?.addTextChangedListener(watcher)
        binding?.edtAddressAddMitra?.addTextChangedListener(watcher)
        binding?.edtTravelNameAddMitra?.addTextChangedListener(watcher)
        binding?.edtPasswordAddMitra?.addTextChangedListener(watcher)
        binding?.edtConfirmPasswordAddMitra?.addTextChangedListener(watcher)

    }

    private fun isLoading(){
        mitraViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun msg(){
        mitraViewModel.msg.observe(viewLifecycleOwner, { msg ->

            if (msg == "Add Mitra Success"){
                view?.findNavController()?.navigate(R.id.action_addMitraFragment_to_nav_mitra)
            }
            else{
                binding?.containerAddMitra?.let {
                    Helper.snackbar(msg, it)
                }
            }

        })

    }

    private fun addMitra(){

        val name = binding?.edtNameAddMitra?.text.toString().trim()
        val email = binding?.edtEmailAddMitra?.text.toString().trim()
        val phone = binding?.edtPhoneAddMitra?.text.toString().trim()
        val address = binding?.edtAddressAddMitra?.text.toString().trim()
        val travelName = binding?.edtTravelNameAddMitra?.text.toString().trim()
        val password = binding?.edtPasswordAddMitra?.text.toString().trim()
        val confirmPassword = binding?.edtConfirmPasswordAddMitra?.text.toString().trim()

        if (password != confirmPassword){
            binding?.layoutEdtConfirmPasswordAddMitra?.error = "Password tidak sama"
            return
        }

        mitraViewModel.addMitra(name, email, phone.toLong(), address, travelName, password)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}