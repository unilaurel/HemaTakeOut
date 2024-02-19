package com.heima.takeout.dagger2.component

import com.heima.takeout.dagger2.module.HomeFragmentModule
import com.heima.takeout.ui.fragment.HomeFragment
import dagger.Component

@Component(modules = arrayOf(HomeFragmentModule::class)) interface HomeFragmentComponent {
    fun inject(homeFragment: HomeFragment)
}