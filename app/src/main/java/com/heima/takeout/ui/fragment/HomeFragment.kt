package com.heima.takeout.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heima.takeout.R
import com.heima.takeout.dagger2.component.DaggerHomeFragmentComponent
import com.heima.takeout.dagger2.module.HomeFragmentModule
import com.heima.takeout.model.beans.Seller
import com.heima.takeout.presenter.HomeFragmentPresenter
import com.heima.takeout.ui.adapter.HomeRvAdapter
import javax.inject.Inject

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    lateinit var homeRvAdapter: HomeRvAdapter
    lateinit var rvHome: RecyclerView
    lateinit var fView: View
    @Inject
    lateinit var homeFragmentPresenter: HomeFragmentPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //フラグメント内で、activityは現在のフラグメントが依存するアクティビティオブジェクトを取得するためのプロパティです。
        fView = View.inflate(activity, R.layout.fragment_home, null)
        rvHome = fView.findViewById<RecyclerView>(R.id.rv_home)
        rvHome.layoutManager = LinearLayoutManager(activity)
        homeRvAdapter = HomeRvAdapter(requireActivity())
        rvHome.adapter = homeRvAdapter
//        homeFragmentPresenter = HomeFragmentPresenter(this)

// View層とPresenter層を分離し、Dagger2（アノテーションベースの依存性注入）を使用してHomeFragmentPresenterを生成する。
DaggerHomeFragmentComponent.builder().homeFragmentModule(HomeFragmentModule(this)).build().inject(this)


        distance = 120.dp2px()

        return fView
    }

    fun Int.dp2px(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    val datas: ArrayList<String> = ArrayList<String>()
    var sum: Int = 0
    var distance: Int = 0
    var alpha = 55
    private fun initData() {
//        for (i in 0 until 50) {
//            datas.add("我是商家：" + i)
//        }

        homeFragmentPresenter.getHomeInfo()
//        homeRvAdapter.setData(datas)

    }

    val allList: ArrayList<Seller> = ArrayList()
    fun  onHomeSuccess(nearbySellers: List<Seller>, otherSellers: List<Seller>) {
        allList.clear()
        allList.addAll(nearbySellers)
        allList.addAll(otherSellers)
        homeRvAdapter.setData(allList)
        Log.e(TAG, "onHomeSuccess: homepage dataを成功的に取得した")

        //スクロールイベントを監視するにはデータが必要です
        rvHome.setOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.e(TAG, "onScrolled: dy:$dy")
                sum += dy
                Log.e(TAG, "onScrolled: sum:$sum")

                if (sum > distance) {
                    alpha = 255;
                } else {
                    alpha = (sum * 200) / distance
                    alpha += 55
                }
                val fLinearLayout = fView.findViewById<LinearLayout>(R.id.ll_title_container)
                fLinearLayout.setBackgroundColor(Color.argb(alpha, 0x31, 0x90, 0xe8))
            }
        })

//        Toast.makeText(context, "homepage dataを成功的に取得した", Toast.LENGTH_SHORT).show()
    }

    fun onHomeFailed() {
//        Toast.makeText(context, "homepage dataの取得は失敗した", Toast.LENGTH_SHORT).show()
    }

}