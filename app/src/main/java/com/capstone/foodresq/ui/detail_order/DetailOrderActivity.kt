package com.capstone.foodresq.ui.detail_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.foodresq.R
import com.capstone.foodresq.data.classes.FoodItemOrder
import com.capstone.foodresq.databinding.ActivityDetailOrderBinding

class DetailOrderActivity : AppCompatActivity() {

    lateinit var binding:ActivityDetailOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMenuIcon()

        val exampleFoodItemOrderList = listOf(
            FoodItemOrder(1),
            FoodItemOrder(2),
            FoodItemOrder(3),
        )

        val FoodItemOrderAdapter= DetailOrderAdapter(exampleFoodItemOrderList)
        binding.rvFoodOrder.layoutManager= LinearLayoutManager(this)
        binding.rvFoodOrder.adapter=FoodItemOrderAdapter
    }

    fun setMenuIcon(){
        binding.toolbarDetailOrder.setNavigationIcon(R.drawable.ic_back)
        binding.toolbarDetailOrder.setNavigationOnClickListener({
            finish()
        })
    }
}