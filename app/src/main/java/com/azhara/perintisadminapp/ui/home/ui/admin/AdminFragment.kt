package com.azhara.perintisadminapp.ui.home.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentAdminBinding
import com.azhara.perintisadminapp.databinding.FragmentMitraBinding
import com.azhara.perintisadminapp.entity.AdminData
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.admin.adapter.AdminAdapter
import com.azhara.perintisadminapp.ui.home.ui.admin.adapter.OnItemClickedListener
import com.azhara.perintisadminapp.ui.home.ui.mitra.MitraViewModel
import com.azhara.perintisadminapp.ui.home.ui.mitra.adapter.MitraAdapter
import com.azhara.perintisadminapp.utils.Helper
import com.google.android.material.snackbar.Snackbar


class AdminFragment : Fragment(), View.OnClickListener {

    private lateinit var adminAdapter: AdminAdapter
    private val adminViewModel: AdminViewModel by viewModels()

    private var _binding: FragmentAdminBinding? = null
    val binding get() = _binding

    private var backPressedTime: Long? = 0
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adminViewModel.getAdmin()

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.fabAddAdmin?.setOnClickListener(this)

        adminAdapter = AdminAdapter()

        setDataAdmin()
        isLoading()
        msg()
        setOnItemClicked()

    }

    private fun setDataAdmin(){
        adminViewModel.dataAdmin.observe(viewLifecycleOwner, { dataAdmin ->
            adminAdapter.submitList(dataAdmin)
            with(binding){
                this?.rvAdmin?.layoutManager = LinearLayoutManager(context)
                this?.rvAdmin?.setHasFixedSize(true)
                this?.rvAdmin?.adapter = adminAdapter
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
            binding?.containerAdmin?.let {
                Helper.snackbar(msg, it)
            }
        })

    }

    private fun setOnItemClicked(){
        adminAdapter.setOnItemClicked(object : OnItemClickedListener{
            override fun onItemClicked(adminData: AdminData) {
                adminViewModel.deleteAdmin(adminData.email)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fabAddAdmin -> {
                view?.findNavController()?.navigate(R.id.action_nav_admin_to_addAdminFragment)
            }
        }
    }
}