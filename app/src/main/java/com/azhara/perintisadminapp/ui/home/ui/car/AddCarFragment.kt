package com.azhara.perintisadminapp.ui.home.ui.car

import android.app.Activity.RESULT_OK
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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentAddCarBinding
import com.azhara.perintisadminapp.entity.MitraData
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.utils.Helper
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import java.lang.Exception

class AddCarFragment : Fragment(), View.OnClickListener {

    private var imgUri: Uri? = null
    private var bitmapImage: Bitmap? = null
    private var requestImage = 1
    private var dataMitra: List<MitraData>? = null

    private var _binding: FragmentAddCarBinding? = null
    private val binding get() = _binding

    private val carViewModel: CarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carViewModel.getDataMitra()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddCarBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnAddCar?.setOnClickListener(this)
        binding?.fabAddImageAddCar?.setOnClickListener(this)

        dataMitra()
        isLoading()
        msg()
        checkButtonActived()
    }

    private fun dataMitra(){
        carViewModel.listMitraData.observe(viewLifecycleOwner, { dataMitra ->

            if (dataMitra != null){
                this.dataMitra = dataMitra
                setDataAutoComplete(dataMitra)
            }

        })
    }

    private fun setDataAutoComplete(dataMitra: List<MitraData>) {

        val listDataMitra = ArrayList<String>()
        dataMitra.forEach {
            it.owner?.let { it1 -> listDataMitra.add(it1) }
        }
        val listTransmission = listOf("Automatic", "Manual")
        val autoCompleteTransmissionAdapter = ArrayAdapter(requireContext(), R.layout.item_auto_complete, listTransmission)
        val autoCompleteOwnerAdapter = ArrayAdapter(requireContext(), R.layout.item_auto_complete, listDataMitra)

        with(binding){
            this?.edtOwnerAddCar?.setAdapter(autoCompleteOwnerAdapter)
            this?.edtTransmissionAddCar?.setAdapter(autoCompleteTransmissionAdapter)
        }

    }

    private fun isLoading(){
        carViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun msg(){
        carViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg == "Berhasil tambah mobil"){
                view?.findNavController()?.navigate(R.id.action_addCarFragment_to_nav_car)
            }
            else{
                binding?.containerAddCar?.let { Helper.snackbar(msg, it) }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fabAddImageAddCar -> {
                openGallery()
            }
            R.id.btnAddCar -> {
                addCar()
            }
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestImage)
    }

    private fun addCar(){
        with(binding){
            var transmissionCode = 0
            val edtOwnerName = this?.edtOwnerAddCar?.text.toString().trim()
            val edtTypeCar = this?.edtCarTypeAddCar?.text.toString().trim()
            val edtTransmission = this?.edtTransmissionAddCar?.text.toString().trim()
            val edtCarYear = this?.edtCarYearAddCar?.text.toString().trim()
            val edtNoRegister = this?.edtNoRegisterAddCar?.text.toString().trim()
            val edtCapacityPerson = this?.edtCapacityPersonAddCar?.text.toString().trim()
            val edtCapacityLuggage = this?.edtCapacityLuggageAddCar?.text.toString().trim()
            val edtPrice = this?.edtPriceAddCar?.text.toString().trim()

            val dataMitraSelected = dataMitra?.filter {
                it.owner == edtOwnerName
            }

            if (edtTransmission == "Automatic"){
                transmissionCode = 1
            }

            if (imgUri == null){
                this?.containerAddCar?.let { Helper.snackbar("Gambar tidak boleh kosong!", it) }
                return
            }

            carViewModel.addCar(Helper.imageByteArray(bitmapImage, 500), dataMitraSelected?.get(0)?.email, dataMitraSelected?.get(0)?.owner, edtTypeCar ,
                    transmissionCode, edtCarYear.toLong(), edtNoRegister, edtCapacityPerson.toInt(), edtCapacityLuggage.toInt(), edtPrice.toLong())

        }

    }

    private fun checkButtonActived(){
        val watcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0){
                    binding?.btnAddCar?.isEnabled = false
                }
                else{
                    binding?.btnAddCar?.isEnabled =  binding?.edtOwnerAddCar?.text.toString().trim() != "" &&  binding?.edtCarTypeAddCar?.text.toString().trim() != "" &&  binding?.edtTransmissionAddCar?.text.toString().trim() != "" &&
                             binding?.edtCarYearAddCar?.text.toString().trim() != "" &&  binding?.edtNoRegisterAddCar?.text.toString().trim() != "" &&  binding?.edtCapacityPersonAddCar?.text.toString().trim() != "" &&  binding?.edtPriceAddCar?.text.toString().trim() != "" &&
                             binding?.edtCapacityLuggageAddCar?.text.toString().trim() != ""
                }

            }

            override fun afterTextChanged(s: Editable?) {}

        }

        binding?.edtOwnerAddCar?.addTextChangedListener(watcher)
        binding?.edtCarTypeAddCar?.addTextChangedListener(watcher)
        binding?.edtTransmissionAddCar?.addTextChangedListener(watcher)
        binding?.edtCarYearAddCar?.addTextChangedListener(watcher)
        binding?.edtNoRegisterAddCar?.addTextChangedListener(watcher)
        binding?.edtCapacityPersonAddCar?.addTextChangedListener(watcher)
        binding?.edtCapacityLuggageAddCar?.addTextChangedListener(watcher)
        binding?.edtPriceAddCar?.addTextChangedListener(watcher)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == requestImage && data != null){

            imgUri = data.data
            context?.let { binding?.imgAddCar?.let { it1 -> Glide.with(it).load(imgUri).into(it1) } }

            if (imgUri != null){

                try {

                    if (Build.VERSION.SDK_INT < 28){
                        val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imgUri)
                        bitmapImage = bitmap
                    }
                    else{
                        val source = activity?.contentResolver?.let { it -> ImageDecoder.createSource(it, imgUri!!) }
                        val bitmap = source?.let { ImageDecoder.decodeBitmap(source) }
                        bitmapImage = bitmap
                    }

                }catch (e: Exception){
                    Log.e("AddCarFragment", e.message.toString())
                }

            }

        }

    }

}