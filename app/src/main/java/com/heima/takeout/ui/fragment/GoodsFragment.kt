package com.heima.takeout.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heima.takeout.R
import com.heima.takeout.model.beans.GoodsInfo
import com.heima.takeout.model.beans.GoodsTypeInfo
import com.heima.takeout.presenter.GoodsFragmentPresenter
import com.heima.takeout.ui.adapter.GoodsAdapter
import com.heima.takeout.ui.adapter.GoodsTypeRvAdapter
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

class GoodsFragment : Fragment() {
    private  val TAG = "GoodsFragment"
    lateinit var rvGoodsType: RecyclerView
    lateinit var slhlv: StickyListHeadersListView
    lateinit var goodsFragmentPresenter: GoodsFragmentPresenter
    lateinit var goodsTypeAdapter: GoodsTypeRvAdapter
    lateinit var goodsAdapter: GoodsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val goodsView =
            LayoutInflater.from(activity).inflate(R.layout.fragment_goods, container, false)
        //left
        rvGoodsType = goodsView.findViewById(R.id.rv_goods_type)
        rvGoodsType.layoutManager = LinearLayoutManager(activity)
        goodsTypeAdapter = GoodsTypeRvAdapter(requireActivity(), this)
        rvGoodsType.adapter = goodsTypeAdapter
        goodsFragmentPresenter = GoodsFragmentPresenter(this)

        //right
        slhlv = goodsView.findViewById(R.id.slhlv)
        goodsAdapter = GoodsAdapter(requireActivity(),this)
        slhlv.adapter = goodsAdapter


        return goodsView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        goodsFragmentPresenter.getBusinessInfo("2")
    }

    fun onLoadBusinessSuccess(
        goodstypeList: List<GoodsTypeInfo>?,
        allTypeGoodList: ArrayList<GoodsInfo>
    ) {
        if (goodstypeList != null) {
            goodsTypeAdapter.setDatas(goodstypeList)
            goodsAdapter.setDatas(allTypeGoodList)

            slhlv.setOnScrollListener(object : AbsListView.OnScrollListener {
                override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                }

                override fun onScroll(
                    view: AbsListView?,
                    firstVisibleItem: Int,
                    visibleItemCount: Int,
                    totalItemCount: Int
                ) {
                    //old position
                    val oldPosition = goodsTypeAdapter.selectPosition
                    Log.e(TAG, "oldPosition: "+oldPosition )
                    //new typeId
                    val newTypeId =
                        goodsFragmentPresenter.allTypeGoodList.get(firstVisibleItem).typeId

                    Log.e(TAG, "newTypeId: "+newTypeId )
                    //new typeIdのposition
                    val newPosition = goodsFragmentPresenter.getTypePositionByTypeId(newTypeId)

                    Log.e(TAG, "newPosition: "+newPosition )
                    if (newPosition != oldPosition) {
                        goodsTypeAdapter.selectPosition = newPosition
                        goodsTypeAdapter.notifyDataSetChanged()
                    }
                }
            })
        }
    }


}