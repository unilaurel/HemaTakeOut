package com.heima.takeout.model.beans

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "t_user")
class User {
    @DatabaseField(id=true)var id = 0// 指定されたIDを使用する
    @DatabaseField(columnName = "name") var name: String = ""
    @DatabaseField(columnName = "balance") var balance = 0f
    @DatabaseField(columnName = "discount") var discount = 0
    @DatabaseField(columnName = "integral") var integral = 0
    @DatabaseField(columnName = "phone")  var phone: String = ""
}