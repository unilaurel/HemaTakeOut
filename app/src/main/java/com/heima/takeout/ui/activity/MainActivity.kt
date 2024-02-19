package com.heima.takeout.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.heima.takeout.R
import com.heima.takeout.ui.fragment.HomeFragment
import com.heima.takeout.ui.fragment.MeFragment
import com.heima.takeout.ui.fragment.MoreFragment
import com.heima.takeout.ui.fragment.OrderFragment


class MainActivity : AppCompatActivity() {
    lateinit var main_botton_bar: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_botton_bar = findViewById<LinearLayout>(R.id.main_bottom_bar)
        //判断是否有虚拟按键，有的话增加paddingBottom=50dp
        if (checkDeviceHasNavigationBar(this)) {
            val view = findViewById<LinearLayout>(R.id.ll_main_activity)
            view.setPadding(0,0,0,25.dp2px())


        }
        initBottomBar()
        changeIndex(0)
    }

    fun Int.dp2px():Int{
        return  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,toFloat(),resources.displayMetrics).toInt()
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
            } else if ("0" == navBarOverride || ""== navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
        }
        return hasNavigationBar
    }

    val fragments: List<Fragment> =
        listOf<Fragment>(HomeFragment(), OrderFragment(), MeFragment(), MoreFragment())

    private fun initBottomBar() {
        for (i in 0 until main_botton_bar.childCount)
//            main_botton_bar.getChildAt(i).setOnClickListener(object : OnClickListener {
//                override fun onClick(v: View?) {
//                    changeIndex(i)
//                }
//
//            })

            main_botton_bar.getChildAt(i).setOnClickListener {
                view->changeIndex(i)
            }
    }

    private fun changeIndex(index: Int) {
        for (i in 0 until main_botton_bar.childCount) {
            val child = main_botton_bar.getChildAt(i)
            if (i == index) {
                //選択時、無効な効果
                setEnable(child, false)

            } else {
                //未選択時、enable=true
                setEnable(child, true)
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.main_content, fragments.get(index))
            .commit()
    }

    private fun setEnable(child: View, isEnable: Boolean) {
        child.isEnabled = isEnable
        if (child is ViewGroup) {
            for (i in 0 until child.childCount) {
                child.getChildAt(i).isEnabled = isEnable
            }
        }
    }
}


