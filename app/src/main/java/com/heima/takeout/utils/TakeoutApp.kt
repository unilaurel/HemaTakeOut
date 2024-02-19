package com.heima.takeout.utils

import com.heima.takeout.model.beans.User
import com.mob.MobApplication

class TakeoutApp:MobApplication() {
    companion object{
        var sUser:User= User()
    }

    override fun onCreate() {
        super.onCreate()
        sUser.id=-1
    }
}