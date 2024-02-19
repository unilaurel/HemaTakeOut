package com.heima.takeout.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.heima.takeout.R
import com.heima.takeout.ui.activity.LoginActivity
import com.heima.takeout.utils.TakeoutApp

class MeFragment : Fragment() {
    lateinit var ll_userinfo: LinearLayout
    lateinit var username: TextView
    lateinit var phone: TextView
    lateinit var ivLogin: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        //フラグメント内で、activityは現在のフラグメントが依存するアクティビティオブジェクトを取得するためのプロパティです。
        val userView = View.inflate(activity, R.layout.fragment_user, null)
        ivLogin = userView.findViewById<ImageView>(R.id.login)
        ll_userinfo = userView.findViewById<LinearLayout>(R.id.ll_userinfo)
        username = userView.findViewById<TextView>(R.id.username)
        phone = userView.findViewById<TextView>(R.id.phone)
        ivLogin.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            activity?.startActivity(intent)
        }

        return userView
    }

    override fun onStart() {
        super.onStart()
        val user = TakeoutApp.sUser
        if (user.id == -1) {
            //未登録
            ll_userinfo.visibility=View.GONE
            ivLogin.visibility=View.VISIBLE

        } else {
            ll_userinfo.visibility = View.VISIBLE
            ivLogin.visibility=View.GONE
            username.text="欢迎您，${user.name}"
            phone.text=user.phone

        }
    }
}