package com.heima.takeout.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.heima.takeout.R
import com.heima.takeout.model.beans.Order
import com.heima.takeout.presenter.OrderFragmentPresenter
import com.heima.takeout.ui.adapter.OrderRvAdpter
import com.heima.takeout.utils.TakeoutApp

class OrderFragment : Fragment() {

    lateinit var rv_order_list: RecyclerView
    lateinit var orderPresenter: OrderFragmentPresenter
    lateinit var adpter: OrderRvAdpter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //フラグメント内で、activityは現在のフラグメントが依存するアクティビティオブジェクトを取得するためのプロパティです。
        val orderView = View.inflate(activity, R.layout.fragment_order, null)
        rv_order_list = orderView.findViewById(R.id.rv_order_list)
        rv_order_list.layoutManager = LinearLayoutManager(activity)
        orderPresenter = OrderFragmentPresenter(this)
        adpter = OrderRvAdpter(requireActivity())
        rv_order_list.adapter = adpter
        swipeRefreshLayout=orderView.findViewById(R.id.srl_order)

        swipeRefreshLayout.setOnRefreshListener(object :SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                val userId = TakeoutApp.sUser.id
                if (userId == -1) {
                    Toast.makeText(activity, "请先登陆，再请求订单", Toast.LENGTH_SHORT).show()
                } else {
                    orderPresenter.getOrderList(userId.toString())
                }
            }
        })

        return orderView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //serverからdataを取得する
        val userId = TakeoutApp.sUser.id
        if (userId == -1) {
            Toast.makeText(activity, "请先登陆，再请求订单", Toast.LENGTH_SHORT).show()
        } else {
            orderPresenter.getOrderList(userId.toString())
        }
    }

    fun onOrderSuccess(orderList: List<Order>) {
       //adapterにdataを設置する
        adpter.setOrderData(orderList)
        swipeRefreshLayout.isRefreshing=false
    }

    fun onOrderFailed() {
        Toast.makeText(activity, "服务器繁忙", Toast.LENGTH_SHORT).show()
        swipeRefreshLayout.isRefreshing=false
    }
}