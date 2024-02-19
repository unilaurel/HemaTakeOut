package com.heima.takeout.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.heima.takeout.R
import com.heima.takeout.presenter.LoginActivityPresenter
import com.heima.takeout.utils.SMSUtil

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    lateinit var iv_user_back: ImageView
    lateinit var tv_user_code: TextView
    lateinit var et_user_phone: EditText
    lateinit var login_user:TextView


    val eventHandler = object : EventHandler() {
        override fun afterEvent(event: Int, result: Int, data: Any?) {
            if (data is Throwable) {
                val msg = data.message
                Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
            } else {
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    Log.e(TAG, "afterEvent: 获取验证码成功")
                }
            }
        }
    }

lateinit var loginActivityPresenter: LoginActivityPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginActivityPresenter= LoginActivityPresenter(this)
        setContentView(R.layout.activity_login)
        iv_user_back = findViewById(R.id.iv_user_back)
        et_user_phone = findViewById(R.id.et_user_phone)
        tv_user_code = findViewById(R.id.tv_user_code)
        login_user=findViewById(R.id.login_user)
//        SMSSDK.registerEventHandler(eventHandler)


        initListener()

    }

    override fun onDestroy() {
        super.onDestroy()
//        SMSSDK.unregisterEventHandler(eventHandler)
    }

    private fun initListener() {
        iv_user_back.setOnClickListener { finish() }
        tv_user_code.setOnClickListener {
            val phone = et_user_phone.text.toString().trim()
//            SMSSDK.getVerificationCode("86",phone)
            if (SMSUtil.judgePhoneNums(this, phone)) {
                Log.e(TAG, "initListener: phone OK")
                //倒计时
                tv_user_code.isEnabled = false
                Thread(CutDownTask()).start()
            } else {
                Log.e(TAG, "initListener: phone no OK")
            }
        }

        login_user.setOnClickListener{
            val phone = et_user_phone.text.toString().trim()
            loginActivityPresenter.loginByPhone(phone)
        }
    }

    fun onLoginSuccess() {
        Toast.makeText(this,"登陆成功",Toast.LENGTH_SHORT).show()
        finish()
    }

    fun onLoginFailed() {
        Toast.makeText(this,"登陆失败",Toast.LENGTH_SHORT).show()
    }

    companion object {
        val TIME_MINUS = -1
        val TIME_IS_OUT = 0
    }


    @SuppressLint("HandlerLeak")
    val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                TIME_MINUS -> tv_user_code.text = "剩余时间${time}秒"
                TIME_IS_OUT -> {
                    tv_user_code.text = "点击重试"
                    time = 10
                    tv_user_code.isEnabled = true
                }
            }
        }
    }
    var time = 10

    inner class CutDownTask : Runnable {
        override fun run() {
            while (time > 0) {
                handler.sendEmptyMessage(TIME_MINUS)
                SystemClock.sleep(999)
                time--
            }
            handler.sendEmptyMessage(TIME_IS_OUT)
        }
    }
}


