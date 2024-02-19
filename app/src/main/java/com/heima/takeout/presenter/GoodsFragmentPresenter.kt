package com.heima.takeout.presenter

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.heima.takeout.model.beans.GoodsInfo
import com.heima.takeout.model.beans.GoodsTypeInfo
import com.heima.takeout.ui.fragment.GoodsFragment
import org.json.JSONObject


class GoodsFragmentPresenter(val goodsFragment: GoodsFragment) : NetPresenter() {
    val allTypeGoodList: ArrayList<GoodsInfo> = arrayListOf()
    var goodstypeList: List<GoodsTypeInfo> = arrayListOf()

    //连接服务器拿到此商家所有商品
    fun getBusinessInfo(sellerId: String) {
        val businessCall = takeOutService.getBusinessInfo(sellerId)
        businessCall.enqueue(callBack)
    }


    override fun paserJson(json: String) {

        val gson = Gson()
        val jsoObj = JSONObject(json)
        val allStr = jsoObj.getString("list")
        //List<GoodsTypeInfo>
        goodstypeList = gson.fromJson(allStr, object : TypeToken<List<GoodsTypeInfo>>() {
        }.type)
        Log.e("business", "该商家一共有" + goodstypeList.size + "个类别商品")

        for (i in 0 until goodstypeList.size) {
            val goodsTypeInfo = goodstypeList.get(i)
            var aTypeList: List<GoodsInfo>? = goodsTypeInfo.list
            if (aTypeList != null) {
                for (j in 0 until aTypeList.size) {
                    aTypeList.get(j).typeId = goodsTypeInfo.id
                    aTypeList.get(j).typeName = goodsTypeInfo.name
                }
                allTypeGoodList.addAll(aTypeList)
            }

        }
        goodsFragment.onLoadBusinessSuccess(goodstypeList, allTypeGoodList)

    }

    //根据商品类别ID，找到此类别的第一个商品位置
    fun getGoodsPostionByTypeId(typeId: Int): Int {
        var position: Int = -1
        for (j in 0 until allTypeGoodList.size) {
            val goodsInfo = allTypeGoodList.get(j)
            if (goodsInfo.typeId == typeId) {
                position = j
                break
            }
        }
        return position
    }

    fun getTypePositionByTypeId(newTypeId: Int): Int {
        var position: Int = -1
        for (i in 0 until goodstypeList.size) {
            val goodsTypeInfo = goodstypeList.get(i)
            if (goodsTypeInfo.id == newTypeId) {
                position = i
                break
            }
        }
        return position
    }

    fun getCartList():ArrayList<GoodsInfo> {
        //count>0なら　cart商品になる
        val cartList= arrayListOf<GoodsInfo>()
        for (j in 0 until allTypeGoodList.size){
            val goodsInfo=allTypeGoodList.get(j)
            if (goodsInfo.count>0){
                cartList.add(goodsInfo)
            }
        }
        return cartList

    }

    fun clearCart() {
        val cartList=getCartList()
        for (j in 0 until cartList.size){
            val goodsInfo=cartList.get(j)
            goodsInfo.count=0
        }
    }

}