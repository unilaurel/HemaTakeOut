package com.heima.takeout.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.heima.takeout.R
import com.heima.takeout.model.beans.GoodsInfo
import com.heima.takeout.ui.activity.BusinessActivity
import com.heima.takeout.ui.fragment.GoodsFragment
import com.heima.takeout.utils.PriceFormater
import com.squareup.picasso.Picasso
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter

private const val DURATION: Long = 1000

class GoodsAdapter(val context: Context, val goodsFragment: GoodsFragment) : BaseAdapter(),
    StickyListHeadersAdapter {
    private val TAG = "GoodsAdapter"
    var goodsList: List<GoodsInfo> = arrayListOf()

    fun setDatas(goodsInfoList: List<GoodsInfo>) {
        this.goodsList = goodsInfoList
        notifyDataSetChanged()
    }

    inner class GoodsItemHolder(itemView: View) : View.OnClickListener {
        lateinit var goodsInfo: GoodsInfo

        val ivIcon: ImageView
        val tvName: TextView
        val tvForm: TextView
        val tvMonthSale: TextView
        val tvNewPrice: TextView
        val tvOldPrice: TextView
        val btnAdd: ImageButton
        val btnMinus: ImageButton
        val tvCount: TextView


        init {
            ivIcon = itemView.findViewById(R.id.iv_icon)
            tvName = itemView.findViewById(R.id.tv_name)
            tvForm = itemView.findViewById(R.id.tv_form)
            tvMonthSale = itemView.findViewById(R.id.tv_month_sale)
            tvNewPrice = itemView.findViewById(R.id.tv_newprice)
            tvOldPrice = itemView.findViewById(R.id.tv_oldprice)
            tvCount = itemView.findViewById(R.id.tv_count)
            btnAdd = itemView.findViewById(R.id.ib_add)
            btnMinus = itemView.findViewById(R.id.ib_minus)
            btnAdd.setOnClickListener(this)
            btnMinus.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var isAdd: Boolean = false
            when (v?.id) {
                R.id.ib_add -> {
                    isAdd = true
                    doAddOperation()
                }

                R.id.ib_minus -> doMinusOperation()
            }

            processRedDotCount(isAdd)
            //cartを更新する
            (goodsFragment.activity as BusinessActivity).updateCartUi()
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

        private fun doMinusOperation() {
            var count = goodsInfo.count
            if (count == 1) {
                val hideAnimation: AnimationSet = hideShowAnimation()
                tvCount.startAnimation(hideAnimation)
                btnMinus.startAnimation(hideAnimation)
            }
            count--
            goodsInfo.count = count
            notifyDataSetChanged()
        }

        private fun doAddOperation() {
            var count = goodsInfo.count
            if (count == 0) {
                val showAnimation: AnimationSet = getShowAnimation()
                tvCount.startAnimation(showAnimation)
                btnMinus.startAnimation(showAnimation)
            }
            count++
            goodsInfo.count = count
            notifyDataSetChanged()

            //抛物线
            var ib = ImageButton(context)
            ib.setBackgroundResource(R.drawable.button_add)
            val srcLoaction = IntArray(2)
            btnAdd.getLocationInWindow(srcLoaction)
            Log.e(TAG, srcLoaction[0].toString() + ":" + srcLoaction[1])
            ib.x = srcLoaction[0].toFloat()
            ib.y = srcLoaction[1].toFloat()

            (goodsFragment.activity as BusinessActivity).addImageButton(
                ib,
                btnAdd.width,
                btnAdd.height
            )
            //执行抛物线动画（水平位移，垂直加速位移）
            val destLocation = (goodsFragment.activity as BusinessActivity).getCartLocation()
            val parabolaAnim: AnimationSet = getParabolaAnimation(ib, srcLoaction, destLocation)
            ib.startAnimation(parabolaAnim)
        }

        private fun getParabolaAnimation(
            ib: ImageButton,
            srcLocation: IntArray,
            destLocation: IntArray
        ): AnimationSet {
            val parabolaAnim: AnimationSet = AnimationSet(false)
            parabolaAnim.duration = DURATION
            val translateX = TranslateAnimation(
                Animation.ABSOLUTE, 0.0f,
                Animation.ABSOLUTE, destLocation[0].toFloat() - srcLocation[0].toFloat(),
                Animation.ABSOLUTE, 0.0f,
                Animation.ABSOLUTE, 0.0f
            )
            translateX.duration = DURATION
            parabolaAnim.addAnimation(translateX)

            val translateY = TranslateAnimation(
                Animation.ABSOLUTE, 0.0f,
                Animation.ABSOLUTE, 0.0f,
                Animation.ABSOLUTE, 0.0f,
                Animation.ABSOLUTE, destLocation[1].toFloat() - srcLocation[1].toFloat(),
            )
            translateY.setInterpolator(AccelerateInterpolator())
            translateY.duration = DURATION
            parabolaAnim.addAnimation(translateY)
            parabolaAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    (ib.parent as ViewGroup).removeView(ib)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })

            return parabolaAnim
        }

        private fun getShowAnimation(): AnimationSet {
            var animationSet: AnimationSet = AnimationSet(false)
            animationSet.duration = DURATION
            val alphaAnim: Animation = AlphaAnimation(0.0f, 1.0f)
            alphaAnim.duration = DURATION
            animationSet.addAnimation(alphaAnim)
            val rotateAnim: Animation = RotateAnimation(
                0.0f,
                720.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            rotateAnim.duration = DURATION
            animationSet.addAnimation(rotateAnim)
            val translateAnim: Animation = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
            )
            translateAnim.duration = DURATION
            animationSet.addAnimation(translateAnim)
            return animationSet
        }

        private fun hideShowAnimation(): AnimationSet {
            var animationSet: AnimationSet = AnimationSet(false)
            animationSet.duration = DURATION
            val alphaAnim: Animation = AlphaAnimation(0.0f, 1.0f)
            alphaAnim.duration = DURATION
            animationSet.addAnimation(alphaAnim)
            val rotateAnim: Animation = RotateAnimation(
                0.0f,
                720.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            rotateAnim.duration = DURATION
            animationSet.addAnimation(rotateAnim)
            val translateAnim: Animation = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
            )
            translateAnim.duration = DURATION
            animationSet.addAnimation(translateAnim)
            return animationSet
        }

        fun bindData(goodsInfo: GoodsInfo) {
            this.goodsInfo = goodsInfo
            Picasso.get().load(goodsInfo.icon).into(ivIcon)
            tvName.text = goodsInfo.name
            tvForm.text = goodsInfo.form
            tvMonthSale.text = "月售${goodsInfo.monthSaleNum}份"
            tvNewPrice.text = PriceFormater.format(goodsInfo.newPrice!!.toFloat())
////            tvNewPrice.text = "$${goodsInfo.newPrice}"
            tvOldPrice.text = "￥${goodsInfo.oldPrice}"
            tvOldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            if (goodsInfo.oldPrice > 0) {
                tvOldPrice.visibility = View.VISIBLE
            } else {
                tvOldPrice.visibility = View.GONE
            }
            tvCount.text = goodsInfo.count.toString()
            if (goodsInfo.count > 0) {
                tvCount.visibility = View.VISIBLE
                btnMinus.visibility = View.VISIBLE
            } else {
                tvCount.visibility = View.INVISIBLE
                btnMinus.visibility = View.INVISIBLE
            }

        }
    }

    override fun getCount(): Int {
        return goodsList.size
    }

    override fun getItem(position: Int): Any {
        return goodsList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView: View
        var goodsItemHolder: GoodsItemHolder
        if (convertView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_goods, parent, false)
            goodsItemHolder = GoodsItemHolder(itemView)
            itemView.tag = goodsItemHolder
        } else {
            itemView = convertView
            goodsItemHolder = itemView.tag as GoodsItemHolder
        }

        goodsItemHolder.bindData(goodsList.get(position))
        return itemView
    }

    override fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val goodsInfo: GoodsInfo = goodsList.get(position)
        val typeName = goodsInfo.typeName
        var textView = LayoutInflater.from(context)
            .inflate(R.layout.item_type_header, parent, false) as TextView
        textView.text = typeName

        return textView
    }

    override fun getHeaderId(position: Int): Long {
        val goodsInfo: GoodsInfo = goodsList.get(position)
        val typeId = goodsInfo.typeId
        return typeId.toLong()
    }
}


