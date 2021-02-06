package com.example.myapplication.view

import com.example.myapplication.bean.Order

interface OrderView {
    fun getOrderInfo(orderId: String): Order
    fun updateOrderInfo(order: Order)
    fun insertOrder(orderInfo: Order)
    fun deleteOrder(orderId: Order)
}