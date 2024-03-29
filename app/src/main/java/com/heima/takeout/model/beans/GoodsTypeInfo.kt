package com.heima.takeout.model.beans

class GoodsTypeInfo {
    var redDotCount: Int=0
    var id = 0 //商品类型id
    var name: String = "" //商品类型名称
    var info: String = "" //特价信息
    var list: List<GoodsInfo>? = null //商品列表

    constructor() : super()
    constructor(id: Int, name: String, info: String, list: List<GoodsInfo>?,) : super() {
        this.id = id
        this.name = name
        this.info = info
        this.list = list
    }

    override fun toString(): String {
        return "GoodsTypeInfo [id=$id, name=$name, info=$info, list=$list]"
    }
}