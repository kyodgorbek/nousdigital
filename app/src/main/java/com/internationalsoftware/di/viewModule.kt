package com.internationalsoftware.di

import com.core.base.navigateTo
import com.domain.features.item.ItemUseCase
import com.home.views.HomeActivity
import com.home.views.HomeViewModel
import com.imgdetails.views.ImageDetailsActivity
import com.imgdetails.views.ImageDetailsViewModel
import com.splash.views.SplashActivity
import com.splash.views.SplashViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext


val viewModule = applicationContext {

    // SPLASH

    factory { SplashActivity() }

    bean {
        SplashViewModel(get<ItemUseCase>())
    }

    viewModel { SplashViewModel(get<ItemUseCase>()) }

    // HOME

    bean { HomeViewModel(get<ItemUseCase>()) }

    factory(HomeActivity::class.java.canonicalName + "Screen") {
        HomeViewModel(get<ItemUseCase>()).navigateTo<HomeActivity>(it, get())
    }

    viewModel { HomeViewModel(get<ItemUseCase>()) }

    // IMAGE DETAILS

    bean { ImageDetailsViewModel() }

    factory(ImageDetailsActivity::class.java.canonicalName + "Screen") {
        ImageDetailsViewModel().navigateTo<ImageDetailsActivity>(it, get())
    }

    viewModel { ImageDetailsViewModel() }
}