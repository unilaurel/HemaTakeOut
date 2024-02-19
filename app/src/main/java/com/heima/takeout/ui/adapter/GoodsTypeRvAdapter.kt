package com.heima.takeout.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.heima.takeout.R
import com.heima.takeout.model.beans.GoodsTypeInfo
import com.heima.takeout.ui.fragment.GoodsFragment


class GoodsTypeRvAdapter(val context: Context, val goodsFragment: GoodsFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var goodsTypeList: List<GoodsTypeInfo> = listOf()

    fun setDatas(list: List<GoodsTypeInfo>) {
        this.goodsTypeList = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val goodsTypeItemHolder = holder as GoodsTypeItemHolder
        goodsTypeItemHolder.bindData(goodsTypeList.get(position), position)
    }


    override fun getItemCount(): Int {
        return goodsTypeList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_type, parent, false)
        return GoodsTypeItemHolder(itemView)
    }

    var selectPosition = 0 //选中的位置

    inner class GoodsTypeItemHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val tvType: TextView
        var mPostion: Int = 0
        lateinit var goodsTypeInfo: GoodsTypeInfo
        var tvRedDotCount: TextView


        init {
            tvType = item.findViewById(R.id.type)
            tvRedDotCount = item.findViewById(R.id.tvRedDotCount)
            item.setOnClickListener {
                selectPosition = mPostion
                notifyDataSetChanged()

                //step2，右侧列表跳转到该类中的第一个商品
                val typeId = goodsTypeInfo.id
                //遍历所有商品，找到此position
                var postion: Int =
                    goodsFragment.goodsFragmentPresenter.getGoodsPostionByTypeId(typeId)
                goodsFragment.slhlv.setSelection(postion)

            }
        }

        fun bindData(goodsTypeInfo: GoodsTypeInfo, position: Int) {
            mPostion = position
            this.goodsTypeInfo = goodsTypeInfo
            tvType.text = goodsTypeInfo.name
//            選択された--background-white，字-bold
//            選択されていない--background-grey，字-普通
            if (position == selectPosition) {
                item.setBackgroundColor(Color.WHITE)
                tvType.setTypeface(Typeface.DEFAULT_BOLD)
                tvType.setTextColor(Color.BLACK)
            } else {
                item.setBackgroundColor(Color.parseColor("#b9dedcdc"))
                tvType.setTypeface(Typeface.DEFAULT)
                tvType.setTextColor(Color.GRAY)
            }
            tvRedDotCount.text = goodsTypeInfo.redDotCount.toString()
            if (goodsTypeInfo.redDotCount > 0) {
                tvRedDotCount.visibility = View.VISIBLE
            }else{
                tvRedDotCount.visibility=View.GONE
            }


        }
    }
}