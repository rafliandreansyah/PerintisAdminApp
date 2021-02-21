package com.azhara.perintisadminapp.ui.home.ui.bookingcar

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.DialogProofPaymentBinding
import com.azhara.perintisadminapp.databinding.FragmentDetailBookingCarBinding
import com.azhara.perintisadminapp.entity.BookingData
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.utils.Helper
import com.bumptech.glide.Glide

class DetailBookingCarFragment : Fragment(), View.OnClickListener {

    private val bookingCarViewModel: BookingCarViewModel by activityViewModels()
    private var _binding: FragmentDetailBookingCarBinding? = null
    private val binding get() = _binding
    private var _dialogBinding: DialogProofPaymentBinding? = null
    private val dialogBinding get() = _dialogBinding

    private var bookingData: BookingData? = null
    private var phoneNumber: String? = null
    private var imgProofPayment: String? = null
    private var statusBooking: Int? = null
    private var userBookingCarId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bookingData = DetailBookingCarFragmentArgs.fromBundle(arguments as Bundle).bookingData
        this.bookingData = bookingData
        bookingData?.carId?.let { bookingCarViewModel.getDataCar(it) }
        bookingData?.userId?.let { bookingCarViewModel.getDataUser(it) }
        bookingData?.bookedListIdUser?.let { bookingCarViewModel.getDataListBookingUserId(it, bookingData.userId) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBookingCarBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            this?.btnCallDetailBooking?.setOnClickListener(this@DetailBookingCarFragment)
            this?.btnDetailCarBookingConfirmation?.setOnClickListener(this@DetailBookingCarFragment)
            this?.btnDetailCarBookingDetailProofPayment?.setOnClickListener(this@DetailBookingCarFragment)
            this?.btnDetailCarBookingDelete?.setOnClickListener(this@DetailBookingCarFragment)
        }

        getDataCar()
        getDataCar()
        getDataListBookingUser()
        getDataUser()
        setDataBooking()
        isLoading()
        msg()
    }

    private fun getDataCar(){
        bookingCarViewModel.carData.observe(viewLifecycleOwner, { dataCar ->
            if (dataCar != null) {
                with(binding) {
                    if (dataCar.imgUrl != null) {
                        context?.let { this?.imgDetailCarBooking?.let { it1 -> Glide.with(it).load(dataCar.imgUrl).into(it1) } }
                    }
                    this?.tvDetailCarBookingCarName?.text = dataCar.carName
                    this?.tvDetailCarBookingCarYear?.text = "${dataCar.year}"
                }
            }
        })
    }

    private fun getDataUser(){
        bookingCarViewModel.userData.observe(viewLifecycleOwner, { dataUser ->
            if (dataUser != null) {
                with(binding) {
                    this?.tvDetailBookingUserName?.text = dataUser.name
                    this?.tvDetailBookingUserEmail?.text = dataUser.email
                    this?.tvDetailBookingUserPhone?.text = dataUser.phone
                    phoneNumber = dataUser.phone
                    if (dataUser.imgUrl != null && dataUser.imgUrl != "") {
                        context?.let { this?.imgDetailCarBookingUserImage?.let { it1 -> Glide.with(it).load(dataUser.imgUrl).into(it1) } }
                    }
                }
            }
        })
    }

    private fun setDataBooking(){
        with(binding){
            this?.tvDetailBookingDriver?.text = bookingData?.driver
            this?.tvDetailBookingDuration?.text = "${bookingData?.duration} Hari"
            this?.tvDetailBookingLocationPickup?.text = bookingData?.pickUpArea
            when (bookingData?.statusBooking) {
                "null" -> {
                    this?.tvDetailCarBookingStatus?.text = "Menunggu Konfirmasi"
                    this?.tvDetailCarBookingStatus?.setTextColor(context?.let { ContextCompat.getColorStateList(it, R.color.colorRed) })
                    this?.btnDetailCarBookingConfirmation?.text = "Konfirmasi"
                    statusBooking = null
                }
                "1" -> {
                    this?.tvDetailCarBookingStatus?.text = "Selesai"
                    this?.tvDetailCarBookingStatus?.setTextColor(context?.let { ContextCompat.getColorStateList(it, R.color.colorGreen) })
                    this?.btnDetailCarBookingConfirmation?.visibility = View.INVISIBLE
                    this?.btnDetailCarBookingDelete?.visibility = View.INVISIBLE
                    statusBooking = 1
                }
                else -> {
                    this?.tvDetailCarBookingStatus?.text = "On Progress"
                    this?.tvDetailCarBookingStatus?.setTextColor(context?.let { ContextCompat.getColorStateList(it, R.color.colorAccent) })
                    this?.btnDetailCarBookingConfirmation?.text = "Selesai"
                    statusBooking = 0
                }
            }
            this?.tvDetailBookingCarTotalPayment?.text = "Rp. ${bookingData?.totalPrice?.let { Helper.currencyFormat(it) }}"
            this?.tvDetailBookingStartTime?.text = Helper.convertTime(bookingData?.startDate?.seconds, "dd MMMM yyyy, HH.mm")
            this?.tvDetailBookingEndTime?.text = Helper.convertTime(bookingData?.endDate?.seconds, "dd MMMM yyyy, HH.mm")
        }
    }

    private fun getDataListBookingUser(){
        bookingCarViewModel.listBookingUserData.observe(viewLifecycleOwner, { dataListBookingUser ->
            if (dataListBookingUser != null) {
                userBookingCarId = dataListBookingUser.bookingId
                with(binding) {
                    if (dataListBookingUser.uploadProofPayment == false) {
                        this?.tvDetailBookingCarEmptyProofPayment?.visibility = View.VISIBLE
                        this?.imgDetailBookingCarProofPayment?.visibility = View.INVISIBLE
                        this?.btnDetailCarBookingDetailProofPayment?.visibility = View.INVISIBLE
                    } else {
                        this?.tvDetailBookingCarEmptyProofPayment?.visibility = View.INVISIBLE
                        this?.imgDetailBookingCarProofPayment?.visibility = View.VISIBLE
                        this?.btnDetailCarBookingDetailProofPayment?.visibility = View.VISIBLE
                        context?.let { binding?.imgDetailBookingCarProofPayment?.let { it1 -> Glide.with(it).load(dataListBookingUser.imgUrlProofPayment).into(it1) } }
                        imgProofPayment = dataListBookingUser.imgUrlProofPayment
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnDetailCarBookingConfirmation -> {
                if (statusBooking == null){
                    bookingCarViewModel.confirmBooking(bookingData?.bookedListIdUser, bookingData?.userId, bookingData?.id, bookingData?.partnerId, bookingData?.bookedIdPartner)
                }else if (statusBooking == 0){
                    bookingCarViewModel.endBooking(userBookingCarId, bookingData?.userId, bookingData?.id, bookingData?.partnerId, bookingData?.bookedIdPartner)
                }
            }
            R.id.btnDetailCarBookingDetailProofPayment -> {
                dialog()
            }
            R.id.btnCallDetailBooking -> {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
                startActivity(intent)
            }
            R.id.btnDetailCarBookingDelete -> {
                bookingCarViewModel.deleteBookingCar(bookingData?.carId, bookingData?.userId, bookingData?.startDate, bookingData?.endDate,
                        bookingData?.bookedListIdUser, userBookingCarId, bookingData?.partnerId, bookingData?.bookedIdPartner, bookingData?.id)
            }
        }
    }

    private fun dialog(){
        val dialog = context?.let { Dialog(it) }
        _dialogBinding = DialogProofPaymentBinding.inflate(LayoutInflater.from(parentFragment?.context))
        dialogBinding?.root?.let { dialog?.setContentView(it) }
        context?.let { dialogBinding?.imgProofPayment?.let { it1 -> Glide.with(it).load(imgProofPayment).into(it1) } }
        dialog?.show()
    }

    private fun isLoading(){
        bookingCarViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true) {
                (activity as HomeActivity).isLoading(true)
            } else {
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun msg(){
        bookingCarViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg != null) {
                binding?.containerDetailBooking?.let { Helper.snackbar(msg, it) }
                when (msg) {
                    "Booking berhasil dikonfirmasi" -> {
                        binding?.tvDetailCarBookingStatus?.text = "On Progress"
                        binding?.tvDetailCarBookingStatus?.setTextColor(context?.let { ContextCompat.getColorStateList(it, R.color.colorAccent) })
                        binding?.btnDetailCarBookingConfirmation?.text = "Selesai"
                        statusBooking = 0
                    }
                    "Booking selesai" -> {
                        binding?.tvDetailCarBookingStatus?.text = "Selesai"
                        binding?.tvDetailCarBookingStatus?.setTextColor(context?.let { ContextCompat.getColorStateList(it, R.color.colorGreen) })
                        binding?.btnDetailCarBookingConfirmation?.visibility = View.INVISIBLE
                        statusBooking = 1
                    }
                    "booking data deleted" -> {
                        view?.findNavController()?.navigate(R.id.action_detailBookingCarFragment_to_nav_booking_car)
                        bookingCarViewModel._msg.value = null
                    }
                }
            }
        })
    }

}