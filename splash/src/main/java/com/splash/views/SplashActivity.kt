package com.splash.views

import android.os.Bundle
import com.core.base.BaseActivity
import com.core.base.navigateTo
import com.core.extensions.koinParameters
import com.core.extensions.observe
import com.domain.models.Item
import com.home.views.HomeActivity
import com.splash.R
import org.koin.android.architecture.ext.viewModel

class SplashActivity : BaseActivity<SplashViewModel>() {

    override val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.fetchItems()
        prepareObservers()
    }

    private fun prepareObservers() {
        viewModel.items()
            .observe(this) { item -> navigateToHome(item)}
            .removeObserversOnDestroy()

        viewModel.error()
            .observe(this) { navigateToHome() }
            .removeObserversOnDestroy()
    }

    private fun navigateToHome(item: Item = Item()) {
        viewModel.navigateTo<HomeActivity>(HomeActivity.params(item).koinParameters, this)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}