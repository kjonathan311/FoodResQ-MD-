package com.capstone.foodresq.ui.main.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodresq.data.classes.Order
import com.capstone.foodresq.data.repository.GetOrderRepository
import kotlinx.coroutines.launch

class HistoryOrderViewModel(
    private val repository: GetOrderRepository
): ViewModel() {
    val allOrderHistory = MutableLiveData<List<Order>?>()

    fun getAllOrderHistory(){
        viewModelScope.launch {
            try {
                val historyOrder = repository.getOrderHistory()
                allOrderHistory.value = historyOrder
            } catch (e : Exception){
                e.printStackTrace()
            }finally {

            }
        }
    }

}