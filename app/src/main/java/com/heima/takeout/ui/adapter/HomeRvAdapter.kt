package com.heima.takeout.ui.adapter


import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.heima.takeout.R
import com.heima.takeout.model.beans.Seller
import com.heima.takeout.ui.activity.BusinessActivity
import com.squareup.picasso.Picasso
import java.util.Arrays
import java.util.Timer
import java.util.TimerTask


class HomeRvAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val TYPE_TITLE = 0
        val TYPE_SELLER = 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_TITLE
        } else {
            return TYPE_SELLER
        }
    }

    var mDatas: ArrayList<Seller> = ArrayList()

    fun setData(datas: ArrayList<Seller>) {
        mDatas = datas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TITLE -> return TitleHolder(View.inflate(context, R.layout.item_title, null))
            TYPE_SELLER -> return SellerHolder(View.inflate(context, R.layout.item_seller, null))
            else -> return TitleHolder(View.inflate(context, R.layout.item_title, null))
        }
    }

    override fun getItemCount(): Int {
        if (mDatas.size > 0) {
            return mDatas.size + 1
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            TYPE_TITLE -> (holder as TitleHolder).bindData(context)
            TYPE_SELLER -> (holder as SellerHolder).bindData(mDatas[position - 1])
        }


    }

    inner class SellerHolder(item: View) : RecyclerView.ViewHolder(item) {
        val tvTitle: TextView
        val ivLogo: ImageView
        val rbScope: RatingBar
        val tvSale: TextView
        val tvSendPrice: TextView
        val tvDistance: TextView

        init {
            tvTitle = item.findViewById(R.id.tv_title)
            ivLogo = item.findViewById(R.id.seller_logo)
            rbScope = item.findViewById(R.id.ratingBar)
            tvSale = item.findViewById(R.id.tv_home_sale)
            tvSendPrice = item.findViewById(R.id.tv_home_send_price)
            tvDistance = item.findViewById(R.id.tv_home_distance)

            item.setOnClickListener {
                val intent:Intent= Intent(context,
                    BusinessActivity::class.java)
                context.startActivity(intent)
            }


        }

        fun bindData(seller: Seller) {
            tvTitle.text = seller.name
            Picasso.get().load(seller.icon).into(ivLogo)
            rbScope.rating = seller.score.toFloat()
            tvSale.text = "月售${seller.sale}单"
            tvSendPrice.text = "¥${seller.sendPrice}起送/配送费¥${seller.deliveryFee} "
            tvDistance.text = "${seller.distance}"


        }
    }


    val URL1 =
        "https://img1.baidu.com/it/u=3050610867,1056529856&fm=253&fmt=auto&app=138&f=JPEG?w=1280&h=427"
    val URL2 =
        "https://img.zcool.cn/community/010d00591cfa18b5b3086ed4fae493.png@1280w_1l_2o_100sh.png"
    val URL3 = "https://img.zcool.cn/community/01de045c419e25a801203d229c6665.jpg@2o.jpg"
    var URL4 =
        "https://1.s91i.faiusr.com/4/AFsIkK06EAQYACCO-f-wBSimqYv4BDDuBTjoAg%21800x800.png?_tm=3&v=1579155086730"

    inner class TitleHolder(item: View) : RecyclerView.ViewHolder(item) {
        fun bindData(context: Context) {
            val viewPager: ViewPager = itemView.findViewById(R.id.viewPager)
            val images = Arrays.asList(URL1, URL2, URL3, URL4) // Replace with your image resources
            val adapter = MyPagerAdapter(context, images)
            viewPager.adapter = adapter

            // 设置定时器实现自动轮播
            val handler = Handler()
            val update = Runnable {
                val currentPage = viewPager.currentItem
                val nextPage = (currentPage + 1) % images.size
                viewPager.setCurrentItem(nextPage, true)
            }
            val swipeTimer = Timer()
            swipeTimer.schedule(object : TimerTask() {
                override fun run() {
                    handler.post(update)
                }
            }, 3000, 3000) // 设置轮播间隔为3秒
        }
    }

    private inner class MyPagerAdapter(
        private val context: Context,
        private val images: List<String>
    ) : PagerAdapter() {
        override fun getCount(): Int {
            return images.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(context)
            Glide.with(context)
                .load(images[position])
                .into(imageView)
            container.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}