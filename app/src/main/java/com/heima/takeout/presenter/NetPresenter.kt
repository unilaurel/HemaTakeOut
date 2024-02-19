package com.heima.takeout.presenter

import android.util.Log
import com.heima.takeout.model.net.ResponseInfo
import com.heima.takeout.model.net.TakeOutService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

abstract class NetPresenter {
    private  val TAG = "NetPresenter"
    val takeOutService: TakeOutService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.49:8080/TakeoutService/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        takeOutService = retrofit.create<TakeOutService>(TakeOutService::class.java)
    }

    val callBack=object :Callback<ResponseInfo> {
        override fun onResponse(call: Call<ResponseInfo>, response: Response<ResponseInfo>) {
            if (response == null) {
                Log.e(TAG, "onResponse: serverから成功に返しない1")
            } else {
                if (response.isSuccessful) {
                    val responseInfo = response.body()
                    if (responseInfo != null) {
                        if (responseInfo.code.equals("0")) {
                            val json = responseInfo.data
                            paserJson(json)

                        } else if (responseInfo.code.equals("-1")) {
                            //interface documentの定義によって、設計する
                        }
                    }
                } else {
                    Log.e(TAG, "onResponse: serverから失敗code返し")
                }
            }
        }

        override fun onFailure(call: Call<ResponseInfo>, t: Throwable) {
            //serverと繋げない
            Log.e(TAG, "onFailure:　serverと繋げない")
        }

    }

     abstract fun paserJson(json: String)
}