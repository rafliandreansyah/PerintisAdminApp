package com.azhara.perintisadminapp.ui.home.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentUserBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.user.adapter.UserAdapter
import com.google.android.material.snackbar.Snackbar

class UserFragment : Fragment() {

    private lateinit var userAdapter: UserAdapter

    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    private var backPressedTime: Long? = 0
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.getUser()

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
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAdapter = UserAdapter()

        setDataUser()
        isLoading()
        msg()
    }

    private fun setDataUser(){
        userViewModel.dataUser.observe(viewLifecycleOwner, { dataUser ->
            userAdapter.submitList(dataUser)
            with(binding){
                this?.rvUsers?.layoutManager = LinearLayoutManager(context)
                this?.rvUsers?.setHasFixedSize(true)
                this?.rvUsers?.adapter = userAdapter
            }
        })
    }

    private fun isLoading(){
        userViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun msg(){
        userViewModel.msg.observe(viewLifecycleOwner, { msg ->
            binding?.containerUser?.let {
                Snackbar.make(it, msg, Snackbar.LENGTH_LONG).setAction("Hide"){

                }.show()
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}