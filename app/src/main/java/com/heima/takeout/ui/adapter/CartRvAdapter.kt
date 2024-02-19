package com.heima.takeout.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.heima.takeout.R
import com.heima.takeout.model.beans.GoodsInfo
import com.heima.takeout.ui.activity.BusinessActivity
import com.heima.takeout.ui.fragment.GoodsFragment
import com.heima.takeout.utils.PriceFormater


class CartRvAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var cartList: ArrayList<GoodsInfo> = arrayListOf()
    val goodsFragment: GoodsFragment

    init {
        goodsFragment = (context as BusinessActivity).fragments.get(0) as GoodsFragment
    }

    fun setsCartList(cartList: ArrayList<GoodsInfo>) {
        this.cartList = cartList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return CartItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CartItemHolder).bindData(cartList.get(position))

    }

    inner class CartItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        val tvName: TextView
        val tvAllPrice: TextView
        val tvCount: TextView
        val ibAdd: ImageButton
        val ibMinus: ImageButton
        lateinit var goodsInfo: GoodsInfo

        init {
            tvName = itemView.findViewById(R.id.tv_name) as TextView
            tvAllPrice = itemView.findViewById(R.id.tv_type_all_price) as TextView
            tvCount = itemView.findViewById(R.id.tv_count) as TextView
            ibAdd = itemView.findViewById(R.id.ib_add) as ImageButton
            ibMinus = itemView.findViewById(R.id.ib_minus) as ImageButton
            ibAdd.setOnClickListener(this)
            ibMinus.setOnClickListener(this)
        }

        fun bindData(goodsInfo: GoodsInfo) {
            this.goodsInfo = goodsInfo
            tvName.text = goodsInfo.name
            tvAllPrice.text = PriceFormater.format(goodsInfo.newPrice.toFloat() * goodsInfo.count)
            tvCount.text = goodsInfo.count.toString()
        }

        override fun onClick(v: View?) {
            var isAdd = false
            when (v?.id) {
                R.id.ib_add -> {

                    isAdd = true
                    doAddOperation()
                }

                R.id.ib_minus -> doMinusOperation()
            }
            //左侧红点
            processRedDotCount(isAdd)
            //下方购物车数据
            (context as BusinessActivity).updateCartUi()


        }

        private fun doMinusOperation() {
            //数据层count
            var count = goodsInfo.count

            if (count == 1) {
                cartList.remove(goodsInfo)
                if (cartList.size == 0) {
                    //没有条目则关闭购物车
                    (context as BusinessActivity).showOrHideCart()
                }
            }
            count--

            goodsInfo.count = count
            //购物车内部的数量与价格
            notifyDataSetChanged()
            //左侧红点

            //右侧列表
            goodsFragment.goodsAdapter.notifyDataSetChanged()
            //下方购物车数据

        }

        private fun doAddOperation() {
            //数据层count
            var count = goodsInfo.count
            count++
            goodsInfo.count = count
            //购物车内部的数量与价格
            notifyDataSetChanged()
            //左侧红点

            //右侧列表
            goodsFragment.goodsAdapter.notifyDataSetChanged()
            //下方购物车数据

        }

        private fun processRedDotCount(isAdd: Boolean) {
            // この商品のカテゴリを見つける
            var typeId = goodsInfo.typeId
            // このカテゴリが左側のリスト内でどの位置にあるかを見つける（リストをループする）
            var typePostion =
                goodsFragment.goodsFragmentPresenter.getTypePositionByTypeId(typeId)
            // 最後に tvRedDotCount を見つける
            var goodTypeInfo = goodsFragment.goodsFragmentPresenter.goodstypeList.get(typePostion)
            var redDotCount = goodTypeInfo.redDotCount
            if (isAdd) {
                redDotCount++
            } else {
                redDotCount--
            }
            goodTypeInfo.redDotCount = redDotCount
            //左側のリストを更新する
            goodsFragment.goodsTypeAdapter.notifyDataSetChanged()
        }

    }
}


