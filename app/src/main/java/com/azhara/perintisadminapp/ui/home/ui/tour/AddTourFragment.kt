package com.azhara.perintisadminapp.ui.home.ui.tour

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.DialogSingleEditTextBinding
import com.azhara.perintisadminapp.databinding.FragmentAddTourBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.tour.adapter.FacilityAdapter
import com.azhara.perintisadminapp.ui.home.ui.tour.adapter.TourVisitedAdapter
import com.azhara.perintisadminapp.utils.Helper
import com.bumptech.glide.Glide


class AddTourFragment : Fragment(), View.OnClickListener {

    private val TAG = "AddTourFragment"

    private var imgUri: Uri? = null
    private var bitmapImage: Bitmap? = null
    private var requestImage = 1
    private var dialog: Dialog? = null

    private var listFacility: ArrayList<String>? = ArrayList()
    private var listVisitedTour: ArrayList<String>? = ArrayList()
    private lateinit var tourVisitedAdapter: TourVisitedAdapter
    private lateinit var facilityAdapter: FacilityAdapter

    private val tourViewModel: TourViewModel by viewModels()

    private var _binding: FragmentAddTourBinding? = null
    private val binding get() = _binding

    private var _dialogBinding: DialogSingleEditTextBinding? = null
    private val dialogBinding get() = _dialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTourBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.fabAddImageTour?.setOnClickListener(this)
        binding?.btnAddTour?.setOnClickListener(this)
        binding?.btnAddTourFacilityAddTour?.setOnClickListener(this)
        binding?.btnAddTourVisitedAddTour?.setOnClickListener(this)

        dialog = context?.let { Dialog(it) }

        _dialogBinding = DialogSingleEditTextBinding.inflate(LayoutInflater.from(parentFragment?.context))
        dialogBinding?.root?.let { dialog?.setContentView(it) }

        //Inisialisasi adapter
        facilityAdapter = FacilityAdapter()
        tourVisitedAdapter = TourVisitedAdapter()

        //Pemanggilan fungsi
        isLoading()
        msg()
        checkButtonIsActive()
        cancelFacility()
        cancelVisitedTour()
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestImage)
    }

    private fun isLoading(){
        tourViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun msg(){
        tourViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg == "Berhasil tambah tour"){
                view?.findNavController()?.navigate(R.id.action_addTourFragment_to_nav_tour)
                binding?.containerAddTour?.let { Helper.snackbar(msg, it) }
            }
            else{
                binding?.containerAddTour?.let { Helper.snackbar(msg, it) }
            }
        })
    }

    private fun checkButtonIsActive(){

        val watcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s?.length == 0){
                    binding?.btnAddTour?.isEnabled = false
                }
                else{
                    binding?.btnAddTour?.isEnabled =
                                binding?.edtTourNameAddTour?.text?.trim() != "" &&
                                binding?.edtTourLocationAddTour?.text?.trim() != "" &&
                                binding?.edtTourDurationAddTour?.text?.trim() != "" &&
                                binding?.edtTourRangeDurationAddTour?.text?.trim() != "" &&
                                binding?.edtTourCapacityAddTour?.text?.trim() != "" &&
                                binding?.edtTourTransportAddTour?.text?.trim() != "" &&
                                binding?.edtIndividualPriceAddTour?.text?.trim() != ""
                }

            }

            override fun afterTextChanged(s: Editable?) {}

        }

        binding?.edtTourNameAddTour?.addTextChangedListener(watcher)
        binding?.edtTourLocationAddTour?.addTextChangedListener(watcher)
        binding?.edtTourDurationAddTour?.addTextChangedListener(watcher)
        binding?.edtTourRangeDurationAddTour?.addTextChangedListener(watcher)
        binding?.edtTourCapacityAddTour?.addTextChangedListener(watcher)
        binding?.edtTourTransportAddTour?.addTextChangedListener(watcher)
        binding?.edtIndividualPriceAddTour?.addTextChangedListener(watcher)

    }

    private fun addTour(){

        with(binding){

            val tourName = this?.edtTourNameAddTour?.text.toString().trim()
            val locationTour = this?.edtTourLocationAddTour?.text.toString().trim()
            val duration = this?.edtTourDurationAddTour?.text.toString().trim()
            val rangeDuration = this?.edtTourRangeDurationAddTour?.text.toString().trim()
            val capacity = this?.edtTourCapacityAddTour?.text.toString().trim()
            val transport = this?.edtTourTransportAddTour?.text.toString().trim()
            val individualPrice = this?.edtIndividualPriceAddTour?.text.toString().trim()

            if (imgUri == null){
                this?.containerAddTour?.let { Helper.snackbar("Gambar tidak boleh kosong!", it) }
                return
            }

            if (listVisitedTour == null || listVisitedTour!!.size == 0){
                this?.containerAddTour?.let { Helper.snackbar("Daftar wisata yang dikunjungi boleh kosong!", it) }
                return
            }

            if (listFacility == null || listFacility!!.size == 0){
                this?.containerAddTour?.let { Helper.snackbar("Fasilitas wisata tidak boleh kosong!", it) }
                return
            }

            tourViewModel.addTour(Helper.imageByteArray(bitmapImage, 500), listFacility,
                locationTour, listVisitedTour, tourName, rangeDuration, transport, duration, capacity.toInt(),
                individualPrice.toLong())

        }

    }

    private fun cancelVisitedTour(){
        tourVisitedAdapter.setOnItemCancelVisitedTour(object : TourVisitedAdapter.OnItemCancelListener{
            override fun onItemCancel(dataVisitedTour: String) {

                val delete = listVisitedTour?.filter {
                    it != dataVisitedTour
                }
                listVisitedTour?.clear()
                delete?.let { listVisitedTour?.addAll(it) }

                setDataVisitedTour(listVisitedTour)

                Log.d(TAG, delete.toString())
            }

        })
    }

    private fun cancelFacility(){
        facilityAdapter.setOnItemCancelFacility(object : FacilityAdapter.OnItemCancelListener{
            override fun onItemCancel(dataFacility: String) {

                val delete = listFacility?.filter {
                    it != dataFacility
                }
                listFacility?.clear()
                delete?.let { listFacility?.addAll(it) }

                setDataFacility(listFacility)

                Log.d(TAG, delete.toString())
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == requestImage && data != null){

            imgUri = data.data
            context?.let { binding?.imgAddTour?.let { it1 -> Glide.with(it).load(imgUri).into(it1) } }

            if (imgUri != null){

                try {

                    if (Build.VERSION.SDK_INT < 28){
                        val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imgUri)
                        bitmapImage = bitmap
                    }
                    else{
                        val source = activity?.contentResolver?.let { ImageDecoder.createSource(it, imgUri!!) }
                        val bitmap = source?.let { ImageDecoder.decodeBitmap(source) }
                        bitmapImage = bitmap
                    }

                }
                catch (e: Exception){
                    e.message?.let { Log.e(TAG, it) }
                }

            }

        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fabAddImageTour -> {
                openGallery()
            }
            R.id.btnAddTour -> {
                addTour()
            }
            R.id.btnAddTourFacilityAddTour -> {
                dialog(0)
            }
            R.id.btnAddTourVisitedAddTour -> {
                dialog(1)
            }
        }
    }

    private fun setDataFacility(listFacility: ArrayList<String>?) {
        facilityAdapter.submitList(listFacility)
        binding?.rvTourFacility?.layoutManager = LinearLayoutManager(context)
        binding?.rvTourFacility?.adapter = facilityAdapter
    }

    private fun setDataVisitedTour(listVisitedTour: ArrayList<String>?) {
        tourVisitedAdapter.submitList(listVisitedTour)
        binding?.rvTourVisitedAddTour?.layoutManager = LinearLayoutManager(context)
        binding?.rvTourVisitedAddTour?.adapter = tourVisitedAdapter
    }

    private fun dialog(dialogType: Int){

        if(dialogType == 0){
            dialogBinding?.tvTitleSingleDialog?.text = "Add Tour Facility"
        }
        else if (dialogType == 1){
            dialogBinding?.tvTitleSingleDialog?.text = "Add Visited Tour"
        }

        val watcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0){
                    dialogBinding?.btnAddDataSingleDialog?.isEnabled = false
                }
                else{
                    dialogBinding?.btnAddDataSingleDialog?.isEnabled = dialogBinding?.edtAddDataSingleDialog?.text?.toString()?.trim() != ""
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        }

        dialogBinding?.edtAddDataSingleDialog?.addTextChangedListener(watcher)

        dialogBinding?.btnAddDataSingleDialog?.setOnClickListener {
            if (dialogType == 0){
                listFacility?.add(dialogBinding?.edtAddDataSingleDialog?.text.toString().trim())
                setDataFacility(listFacility)
                dialogBinding?.edtAddDataSingleDialog?.text = null
                Log.d(TAG, listFacility.toString())
            }
            else if (dialogType == 1){
                listVisitedTour?.add(dialogBinding?.edtAddDataSingleDialog?.text.toString().trim())
                setDataVisitedTour(listVisitedTour)
                dialogBinding?.edtAddDataSingleDialog?.text = null
                Log.d(TAG, listVisitedTour.toString())
            }

            //Menghilangkan dialog setelah isi data
            dialog?.dismiss()
        }

        dialog?.show()

    }

}