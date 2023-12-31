package com.capstone.foodresq.ui.main.order

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.foodresq.R
import com.capstone.foodresq.databinding.FragmentHistoryOrderPagerBinding
import com.capstone.foodresq.ui.detail.DetailViewModel
import com.capstone.foodresq.ui.detail_order.DetailOrderActivity
import com.capstone.foodresq.ui.main.order.adapter.HistoryAdapter
import com.capstone.foodresq.ui.main.order.adapter.OrderAdapter
import com.capstone.foodresq.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoryOrderPagerFragment : Fragment() {

    lateinit var binding:FragmentHistoryOrderPagerBinding
    lateinit var popupWindow:PopupWindow
    private val historyViewModel:HistoryOrderViewModel by viewModel()
    private val orderViewModel:OrderViewModel by viewModel()
    private val detailViewModel:DetailViewModel by viewModel()
    private var rate=5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHistoryOrderPagerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index=arguments?.getInt(PAGER_NUMBER,0)

        if (index==1){
            //order
            orderViewModel.getAllOrder()
            orderViewModel.allOrder.observe(viewLifecycleOwner){list ->
                if (list!=null){
                    val orderAdapter = OrderAdapter(
                        list,
                        clickListener = {
                            val popupView: View =
                                LayoutInflater.from(requireActivity()).inflate(R.layout.qrcode_popup, null)
                            popupWindow = PopupWindow(
                                popupView,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                true
                            )
                            val barcodeImage: ImageView = popupView.findViewById(R.id.iv_qc)
                            val generatedBarcode =
                                Utils.generateQrCode(it.id,250, 250)
                            if (generatedBarcode != null) {
                                barcodeImage.setImageBitmap(generatedBarcode)
                            }

                            val closeButton: Button = popupView.findViewById(R.id.btn_qc_cancel)
                            closeButton.setOnClickListener {
                                popupWindow.dismiss()
                            }

                            popupWindow.showAtLocation(
                                requireActivity().findViewById(android.R.id.content),
                                Gravity.CENTER,
                                0,
                                0
                            )
                        },
                        detailOrderClickListener = {
                            val intent= Intent(requireActivity(),DetailOrderActivity::class.java)
                            intent.putExtra("restaurantId", it.restaurant_id)
                            intent.putExtra("order",it.id)
                            startActivity(intent)
                       }
                    )
                    for (i in list){
                        detailViewModel.getDetailRestaurant(i.restaurant_id)
                        detailViewModel.restaurant.observe(viewLifecycleOwner){
                            with(orderAdapter.OrderViewHolder(view)){
                                title.text = it?.name
                                address.text = it?.address
                                pickup.text = resources.getString(R.string.pickup_time, it?.openTime, it?.closeTime)
                            }
                        }
                    }
                    binding.rvOrder.adapter=orderAdapter
                    binding.rvOrder.layoutManager= LinearLayoutManager(requireContext())
                }
            }
        }else{
            //history
            historyViewModel.getAllOrderHistory()
            historyViewModel.allOrderHistory.observe(viewLifecycleOwner){list ->
                if (list!=null){
                    val historyAdapter= HistoryAdapter(list){
                        val popupView: View = LayoutInflater.from(requireActivity()).inflate(R.layout.rate_popup, null)
                        popupWindow = PopupWindow(
                            popupView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            true
                        )

                        val ivRate1:ImageView=popupView.findViewById(R.id.iv_star_1)
                        val ivRate2:ImageView=popupView.findViewById(R.id.iv_star_2)
                        val ivRate3:ImageView=popupView.findViewById(R.id.iv_star_3)
                        val ivRate4:ImageView=popupView.findViewById(R.id.iv_star_4)
                        val ivRate5:ImageView=popupView.findViewById(R.id.iv_star_5)
                        ivRate1.setOnClickListener {
                            ivRate1.setImageResource(R.drawable.ic_star_bright)
                            ivRate2.setImageResource(R.drawable.ic_star)
                            ivRate3.setImageResource(R.drawable.ic_star)
                            ivRate4.setImageResource(R.drawable.ic_star)
                            ivRate5.setImageResource(R.drawable.ic_star)
                            rate=1
                        }
                        ivRate2.setOnClickListener {
                            ivRate1.setImageResource(R.drawable.ic_star_bright)
                            ivRate2.setImageResource(R.drawable.ic_star_bright)
                            ivRate3.setImageResource(R.drawable.ic_star)
                            ivRate4.setImageResource(R.drawable.ic_star)
                            ivRate5.setImageResource(R.drawable.ic_star)
                            rate=2
                        }
                        ivRate3.setOnClickListener {
                            ivRate1.setImageResource(R.drawable.ic_star_bright)
                            ivRate2.setImageResource(R.drawable.ic_star_bright)
                            ivRate3.setImageResource(R.drawable.ic_star_bright)
                            ivRate4.setImageResource(R.drawable.ic_star)
                            ivRate5.setImageResource(R.drawable.ic_star)
                            rate=3
                        }
                        ivRate4.setOnClickListener {
                            ivRate1.setImageResource(R.drawable.ic_star_bright)
                            ivRate2.setImageResource(R.drawable.ic_star_bright)
                            ivRate3.setImageResource(R.drawable.ic_star_bright)
                            ivRate4.setImageResource(R.drawable.ic_star_bright)
                            ivRate5.setImageResource(R.drawable.ic_star)
                            rate=4
                        }
                        ivRate5.setOnClickListener {
                            ivRate1.setImageResource(R.drawable.ic_star_bright)
                            ivRate2.setImageResource(R.drawable.ic_star_bright)
                            ivRate3.setImageResource(R.drawable.ic_star_bright)
                            ivRate4.setImageResource(R.drawable.ic_star_bright)
                            ivRate5.setImageResource(R.drawable.ic_star_bright)
                            rate=5
                        }
                        val closeButton: Button = popupView.findViewById(R.id.btn_rt_done)
                        closeButton.setOnClickListener {
                            popupWindow.dismiss()
                        }

                        popupWindow.showAtLocation(
                            requireActivity().findViewById(android.R.id.content),
                            Gravity.CENTER,
                            0,
                            0
                        )
                    }
                    for (i in list){
                        detailViewModel.getDetailRestaurant(i.restaurant_id)
                        detailViewModel.restaurant.observe(viewLifecycleOwner){
                            with(historyAdapter.HistoryViewHolder(view)){
                                title.text = it?.name
                                address.text = it?.address
                                pickup.text = resources.getString(R.string.pickup_time, it?.openTime, it?.closeTime)
                            }
                        }
                    }
                    binding.rvOrder.adapter=historyAdapter
                    binding.rvOrder.layoutManager= LinearLayoutManager(requireContext())
                }
            }
        }
    }

    companion object{
        const val PAGER_NUMBER = "1"
    }

}