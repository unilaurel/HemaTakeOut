package com.heima.takeout.presenter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.heima.takeout.model.beans.Seller
import com.heima.takeout.ui.fragment.HomeFragment
import org.json.JSONObject


class HomeFragmentPresenter(val homeFragment: HomeFragment) : NetPresenter() {
    private val TAG = "HomeFragmentPresenter"
//    val takeOutService: TakeOutService
//
//    init {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.1.49:8080/TakeoutService/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        takeOutService = retrofit.create<TakeOutService>(TakeOutService::class.java)
//    }

    fun getHomeInfo() {
        val homeCall = takeOutService.getHomeInfo()
        homeCall.enqueue(callBack)


    }

    override fun paserJson(json: String) {
        val gson = Gson()
        var jsonObject = JSONObject(json)
        var nearby = jsonObject.getString("nearbySellerList")
        val nearbySellers: List<Seller> =
            gson.fromJson(nearby, object : TypeToken<List<Seller>>() {}.type)
        var other = jsonObject.getString("otherSellerList")
        val otherSellers: List<Seller> =
            gson.fromJson(other, object : TypeToken<List<Seller>>() {}.type)

        if (nearbySellers.isEmpty() && otherSellers.isEmpty()) {
            //dataがなければ、fail画面
            homeFragment.onHomeFailed()
        } else {
            //dataがあれば、success画面
            homeFragment.onHomeSuccess(nearbySellers, otherSellers)
        }


    }
}


