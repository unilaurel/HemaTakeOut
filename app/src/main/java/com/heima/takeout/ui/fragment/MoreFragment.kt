package com.heima.takeout.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.heima.takeout.R

class MoreFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //フラグメント内で、activityは現在のフラグメントが依存するアクティビティオブジェクトを取得するためのプロパティです。
        val view = View.inflate(activity, R.layout.fragment_, null)
        (view as TextView).setText("更多")

        return view
    }
}