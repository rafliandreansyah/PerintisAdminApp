package com.azhara.perintisadminapp.ui.home.ui.bookingtour

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.DialogProofPaymentBinding
import com.azhara.perintisadminapp.databinding.FragmentDetailBookingTourBinding
import com.azhara.perintisadminapp.entity.BookingTourData
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.bookingtour.adapter.FacilityDetailBookingTourAdapter
import com.azhara.perintisadminapp.ui.home.ui.bookingtour.adapter.VisitedTourDetailBookingTourAdapter
import com.azhara.perintisadminapp.utils.Helper
import com.bumptech.glide.Glide

class DetailBookingTourFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentDetailBookingTourBinding? = null
    private val binding get() = _binding!!

    private var _dialogBinding: DialogProofPaymentBinding? = null
    private val dialogBinding get() = _dialogBinding

    private val bookingTourViewModel: BookingTourViewModel by viewModels()
    private lateinit var facilityDetailBookingTourAdapter: FacilityDetailBookingTourAdapter
    private lateinit var visitedTourDetailBookingTourAdapter: VisitedTourDetailBookingTourAdapter

    private var bookingTourData: BookingTourData? = null
    private var phoneNumber: String? = null
    private var imgUrlProofPayment: String? = null
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bookingTourData = DetailBookingTourFragmentArgs.fromBundle(arguments as Bundle).bookingTourData
        this.bookingTourData = bookingTourData

        //Get data user
        bookingTourViewModel.getDataUser(bookingTourData?.userId)

        //Get data user List booking
        bookingTourViewModel.getListBookingUser(bookingTourData?.userId, bookingTourData?.idListBookingTourUser)

        //Get data tour
        bookingTourViewModel.getTour(bookingTourData?.tourId)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBookingTourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = context?.let { Dialog(it) }
        _dialogBinding = DialogProofPaymentBinding.inflate(LayoutInflater.from(parentFragment?.context))
        dialogBinding?.root?.let { dialog?.setContentView(it) }

        facilityDetailBookingTourAdapter = FacilityDetailBookingTourAdapter()
        visitedTourDetailBookingTourAdapter = VisitedTourDetailBookingTourAdapter()

        with(binding){
            btnCallDetailBookingTour.setOnClickListener(this@DetailBookingTourFragment)
            btnConfirmationDetailBookingTour.setOnClickListener(this@DetailBookingTourFragment)
            btnDeleteDetailBookingTour.setOnClickListener(this@DetailBookingTourFragment)
            btnDetailProofPaymentDetailBookingTour.setOnClickListener(this@DetailBookingTourFragment)
        }

        isLoading()
        msg()
        setDataTour()
    }

    private fun setDataTour(){

        with(binding){

            tvTourNameDetailBookingTour.text = bookingTourData?.tourName
            tvLocationPickupAreaDetailBookingTour.text = bookingTourData?.pickupArea
            tvStartDateDetailBookingTour.text = "${Helper.convertTime(bookingTourData?.dateTour?.seconds, "dd MMMM yyyy")}"
            if (bookingTourData?.statusPayment == true){
                tvStatusConfirmationDetailBookingTour.text = "Terkonfirmasi"
                tvStatusConfirmationDetailBookingTour.setTextColor(context?.let {
                    ContextCompat.getColorStateList(
                        it, R.color.colorGreen)
                })
            }
            else{
                tvStatusConfirmationDetailBookingTour.text = "Menunggu Konfirmasi"
                tvStatusConfirmationDetailBookingTour.setTextColor(context?.let {
                    ContextCompat.getColorStateList(
                        it, R.color.colorRed)
                })
            }
            tvTotalPaymentDetailBookingTour.text = "Rp. ${bookingTourData?.totalPrice?.let { Helper.currencyFormat(it) }}"
            tvDurationDetailBookingTour.text = bookingTourData?.duration

            //User Data
            bookingTourViewModel.userData.observe(viewLifecycleOwner, { user ->
                if (user != null){
                    tvUserEmailDetailBookingTour.text = user.email
                    tvUserNameDetailBookingTour.text = user.name
                    tvUserPhoneDetailBookingTour.text = user.phone
                    phoneNumber = user.phone
                }
            })

            //Tour Data
            bookingTourViewModel.dataTour.observe(viewLifecycleOwner, { tourData ->
                if (tourData != null){

                    tvCapacityDetailBookingTour.text = "${tourData.capacity} Orang"
                    tvRengeDurationDetailBookingCar.text = tourData.timeTour
                    tvLocationDetailBookingTour.text = tourData.locationTour
                    context?.let { Glide.with(it).load(tourData.imgUrl).into(imgTourDetailBookingTour) }

                    facilityDetailBookingTourAdapter.submitList(tourData.facilities)
                    with(rvFacilityDetailBookingTour){

                        layoutManager = LinearLayoutManager(context)
                        adapter = facilityDetailBookingTourAdapter
                        setHasFixedSize(true)

                    }

                    visitedTourDetailBookingTourAdapter.submitList(tourData.visitedTour)
                    with(rvVisitedTourDetailBookingTour){

                        layoutManager = LinearLayoutManager(context)
                        adapter = visitedTourDetailBookingTourAdapter
                        setHasFixedSize(true)

                    }

                }
            })

            //List Booking User
            bookingTourViewModel.userListDataBooking.observe(viewLifecycleOwner, { userListData ->
                if (userListData != null){

                    with(binding){

                        if (userListData.uploadProofPayment == false){

                            tvEmptyProofPaymentDetailBookingTour.visibility = View.VISIBLE
                            btnDetailProofPaymentDetailBookingTour.visibility = View.INVISIBLE

                        }
                        else{
                            tvEmptyProofPaymentDetailBookingTour.visibility = View.INVISIBLE
                            btnDetailProofPaymentDetailBookingTour.visibility = View.VISIBLE
                            context?.let { Glide.with(it).load(userListData.imgUrlProofPayment).into(imgTourProofPaymentDetailBookingTour) }
                            imgUrlProofPayment = userListData.imgUrlProofPayment
                        }

                    }
                }
            })
        }

    }

    private fun dialog(){
        context?.let { dialogBinding?.imgProofPayment?.let { it1 -> Glide.with(it).load(imgUrlProofPayment).into(it1) } }
        dialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.btnCallDetailBookingTour -> {
                if (phoneNumber != null || phoneNumber != ""){
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:$phoneNumber")
                    startActivity(intent)
                }
            }

            R.id.btnConfirmationDetailBookingTour -> {

            }

            R.id.btnDeleteDetailBookingTour -> {

            }

            R.id.btnDetailProofPaymentDetailBookingTour -> {
                if (imgUrlProofPayment != null || imgUrlProofPayment != ""){
                    dialog()
                }
            }

        }
    }

    private fun isLoading(){
        bookingTourViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true) {
                (activity as HomeActivity).isLoading(true)
            } else {
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun msg(){
        bookingTourViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg != null) {
                Helper.snackbar(msg, binding.containerDetailBookingTour)
            }
        })
    }

}