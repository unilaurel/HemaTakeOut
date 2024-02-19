package com.heima.takeout.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.heima.takeout.R
import com.heima.takeout.model.beans.Order

class OrderRvAdpter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
    }
    private var orderList: List<Order> = ArrayList()

    fun setOrderData(orders: List<Order>) {
        this.orderList = orders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val itemView = View.inflate(context, R.layout.item_order_item, null)//没有填充满,原因是测量模式是UNSPECIFY
        //通过返回值已经addview，如果attachToRoot使用true会再一次addView，就会报错
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_order_item, parent, false)
        return OrderItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OrderItemHolder).bindData(orderList.get(position))
    }

    inner class OrderItemHolder(item: View) : RecyclerView.ViewHolder(item) {
        fun bindData(order: Order) {
            tv_order_item_seller_name.text = order.seller!!.name
            tv_order_item_type.text = order.type

        }

        var tv_order_item_seller_name: TextView
        var tv_order_item_type: TextView

        init {
            //findviewid
            tv_order_item_seller_name =
                item.findViewById(R.id.tv_order_item_seller_name)
            tv_order_item_type = item.findViewById(R.id.tv_order_item_type)
        }
    }
}