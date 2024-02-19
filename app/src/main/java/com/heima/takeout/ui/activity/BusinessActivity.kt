package com.heima.takeout.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.flipboard.bottomsheet.BottomSheetLayout
import com.google.android.material.tabs.TabLayout
import com.heima.takeout.R
import com.heima.takeout.ui.adapter.CartRvAdapter
import com.heima.takeout.ui.fragment.CommentsFragment
import com.heima.takeout.ui.fragment.GoodsFragment
import com.heima.takeout.ui.fragment.SellerFragment
import com.heima.takeout.utils.PriceFormater

class BusinessActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var fl_Container: FrameLayout
    lateinit var vp: ViewPager
    lateinit var fragments: List<Fragment>
    lateinit var tabs: TabLayout
    lateinit var icon_cart: ImageView
    lateinit var tvSelectNum: TextView
    lateinit var tvCountPrice: TextView
    var bottomSheetView: View? = null
    lateinit var bottomSheetLayout: BottomSheetLayout
    lateinit var rvCart: RecyclerView
    lateinit var cartRvAdapter: CartRvAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)
        fl_Container = findViewById(R.id.fl_Container)
        vp = findViewById(R.id.vp)
        tabs = findViewById(R.id.tabs)
        icon_cart = findViewById(R.id.imgCart)
        tvSelectNum = findViewById(R.id.tvSelectNum)
        tvCountPrice = findViewById(R.id.tvCountPrice)
        bottomSheetLayout = findViewById(R.id.bottomSheetLayout)


        if (checkDeviceHasNavigationBar(this)) {
            fl_Container.setPadding(0, 0, 0, 27.dp2px())
        }

        vp.adapter = BusinessFragmentPagerAdapter(supportFragmentManager)
        tabs.setupWithViewPager(vp)


        initFragments()

    }

    private fun initFragments() {
        fragments = listOf<Fragment>(GoodsFragment(), SellerFragment(), CommentsFragment())


    }

    val titles = listOf<String>("商品", "商家", "评论")

    inner class BusinessFragmentPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager) {
        override fun getPageTitle(position: Int): CharSequence? {
            return titles.get(position)
        }

        override fun getCount(): Int {
            return titles.size

        }

        override fun getItem(position: Int): Fragment {
            return fragments.get(position)
        }


    }

    //获取是否存在NavigationBar
    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride || "" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
        }
        return hasNavigationBar
    }

    fun Int.dp2px(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    fun addImageButton(ib: ImageButton, width: Int, height: Int) {
        fl_Container.addView(ib, width, height)
    }

    fun getCartLocation(): IntArray {
        val destLocation = IntArray(2)
        icon_cart.getLocationInWindow(destLocation)
        return destLocation

    }

    fun updateCartUi() {
        //count ,priceを更新する
        var count = 0
        var countPrice = 0.0f
        //哪些属于购物车
        val goodsFragment: GoodsFragment = fragments.get(0) as GoodsFragment
        val cartList = goodsFragment.goodsFragmentPresenter.getCartList()
        for (i in 0 until cartList.size) {
            val goodsInfo = cartList.get(i)
            count += goodsInfo.count
            countPrice += goodsInfo.count * goodsInfo.newPrice.toFloat()
        }
        if (count > 0) {
            tvSelectNum.visibility = View.VISIBLE

        } else {
            tvSelectNum.visibility = View.GONE
        }
        tvSelectNum.text = count.toString()
        tvCountPrice.text = PriceFormater.format(countPrice)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom -> showOrHideCart()
        }
    }

    public fun showOrHideCart() {
        if (bottomSheetView == null) {
            //layoutをload する
            bottomSheetView = LayoutInflater.from(this)
                .inflate(R.layout.cart_list, window.decorView as ViewGroup, false)
            rvCart= (bottomSheetView as View).findViewById(R.id.rvCart)
            rvCart.layoutManager=LinearLayoutManager(this)
            cartRvAdapter= CartRvAdapter(this)
            rvCart.adapter=cartRvAdapter


        }
        //bottomSheetViewの内容を表示するかを判断する
        if (bottomSheetLayout.isSheetShowing) {
            //关闭内容显示
            bottomSheetLayout.dismissSheet()
        } else {
            //哪些属于购物车
            val goodsFragment: GoodsFragment = fragments.get(0) as GoodsFragment
            val cartList = goodsFragment.goodsFragmentPresenter.getCartList()
            cartRvAdapter.setsCartList(cartList)

            bottomSheetLayout.showWithSheetView(bottomSheetView)
        }


}

    private fun clearRedDot() {
        val goodsFragment: GoodsFragment = fragments.get(0) as GoodsFragment
        val goodstypeList = goodsFragment.goodsFragmentPresenter.goodstypeList
        for (i in 0 until goodstypeList.size) {
            val goodsTypeInfo = goodstypeList.get(i)
            goodsTypeInfo.redDotCount = 0
        }
    }
}


