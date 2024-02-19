package com.heima.takeout.dagger2.module

import com.heima.takeout.presenter.HomeFragmentPresenter
import com.heima.takeout.ui.fragment.HomeFragment
import dagger.Module
import dagger.Provides

@Module class HomeFragmentModule(val homeFragment: HomeFragment){

   @Provides fun providerHomeFragmentPresenter():HomeFragmentPresenter{
       return HomeFragmentPresenter(homeFragment)
   }
}