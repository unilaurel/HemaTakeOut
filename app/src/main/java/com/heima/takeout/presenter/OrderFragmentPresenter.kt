package com.heima.takeout.presenter

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.heima.takeout.model.beans.Order
import com.heima.takeout.model.net.ResponseInfo
import com.heima.takeout.ui.fragment.OrderFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class OrderFragmentPresenter(val orderFragment: OrderFragment) : NetPresenter() {
    private val TAG = "OrderFragmentPresenter"
    fun getOrderList(userId: String) {
        val observable: Observable<ResponseInfo> = takeOutService.getOrderListByRxJava(userId)
//        orderCall.enqueue(callBack)
//        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<ResponseInfo> {
//                override fun onSubscribe(d: Disposable) {
//                }
//
//                override fun onError(e: Throwable) {
//                    Log.e(TAG, "onError: Rxjava:" + e.localizedMessage)
//                }
//
//                override fun onComplete() {
//                }
//
//                override fun onNext(responseInfo: ResponseInfo) {
//                    if (responseInfo != null) {
//                        paserJson(responseInfo.data)
//                    }
//                }
//
//            })

        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                paserJson(it.data)
            }, {
                Log.e(TAG, "onError: Rxjava:" + it.localizedMessage)
            }, {
                Log.e(TAG, "getOrderList: onComplete")
            })
    }

    override fun paserJson(json: String) {
        //parse List<Order>
        val orderList: List<Order> =
            Gson().fromJson(json, object : TypeToken<List<Order>>() {}.type)
        if (orderList.isNotEmpty()) {
            orderFragment.onOrderSuccess(orderList)
        } else {
            orderFragment.onOrderFailed()
        }
    }
}
