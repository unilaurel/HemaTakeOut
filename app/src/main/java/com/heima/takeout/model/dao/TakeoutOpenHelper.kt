package com.heima.takeout.model.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.heima.takeout.model.beans.User
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils


class TakeoutOpenHelper(val context: Context) :
    OrmLiteSqliteOpenHelper(context, "takeout_kotlin.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        //user tableを作成する
        TableUtils.createTable(connectionSource, User::class.java)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        connectionSource: ConnectionSource?,
        olderversion: Int,
        newversion: Int
    ) {
    }
}