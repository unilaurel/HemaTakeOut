package com.heima.takeout.model.net

import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface TakeOutService{

    //Retrofit turns your HTTP API into a Java interface.
    @GET("home")
    fun getHomeInfo():Call<ResponseInfo>
    @POST("login")
     fun loginByPhone(@Query("phone") phone: String):Call<ResponseInfo>
    @GET("order")
     fun getOrderList(@Query("id")userId: String):Call<ResponseInfo>

    //Rxjavaを使用する
     @GET("order")
     fun getOrderListByRxJava(@Query("id")userId: String): Observable<ResponseInfo>


    @GET("business")
    fun getBusinessInfo(@Query("sellerId") sellerId: String): Call<ResponseInfo>
}