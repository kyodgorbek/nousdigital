package com.home.views

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.core.base.BaseActivity
import com.core.base.navigateTo
import com.core.extensions.*
import com.domain.models.Data
import com.domain.models.Item
import com.home.R
import com.home.adapters.HomeAdapter
import com.imgdetails.views.ImageDetailsActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch
import org.koin.android.architecture.ext.viewModel


class HomeActivity : BaseActivity<HomeViewModel>() {
    override val viewModel by viewModel<HomeViewModel>()
    private val item by lazy { intent?.extras?.getParcelable<Item>(ITEM_EXTRA) ?: Item() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setStatusBarTransparent()
        initializeComponents()
        prepareObservers()
    }

    private fun initializeComponents() {
        viewModel.launch {
            doWait(3000) {
                layoutLoadingHome.fadeOut()

                layoutErrorHome.apply {
                    fadeCondition(item.data.isEmpty())
                    setOnClickListener {
                        viewModel.fetchItems()
                        layoutErrorHome.fadeOut()
                    }
                }

                rvListHome.fadeCondition(item.data.isNotEmpty(), isSetInvisible = true)
                setAdapter(item)
            }
        }
    }

    private fun prepareObservers() {
        viewModel.loading()
            .observe(this) { isLoading ->
                layoutLoadingHome.isEnabled = isLoading
                layoutLoadingHome.fadeCondition(isLoading)
            }
            .removeObserversOnDestroy()

        viewModel.items()
            .observe(this) { item ->
                rvListHome.fadeIn()
                layoutErrorHome.isEnabled = false
                layoutErrorHome.isClickable = false
                setAdapter(item)
            }
            .removeObserversOnDestroy()

        viewModel.error()
            .observe(this) {
                layoutErrorHome.isEnabled = true
                layoutErrorHome.isClickable = true
                layoutErrorHome.fadeIn()
                rvListHome.fadeOutInvisible()
            }
            .removeObserversOnDestroy()
    }

    private fun setAdapter(item: Item) {
        rvListHome.layoutManager = GridLayoutManager(this, 3)
        rvListHome.adapter = HomeAdapter(item.data) { imageView, data -> click(imageView, data) }

        txtQtdPhotosHome.text = getString(R.string.home_qtd_photos, item.data.size)
    }

    private fun click(imageView: ImageView, data: Data) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            imageView,
            ViewCompat.getTransitionName(imageView) ?: ""
        )

        viewModel.navigateTo<ImageDetailsActivity>(
            ImageDetailsActivity.params(data).koinParameters,
            this,
            options.toBundle()
        )
    }

    companion object {
        private const val ITEM_EXTRA = "itemExtra"

        @JvmStatic
        fun params(item: Item): Bundle {
            return Bundle().apply {
                putParcelable(ITEM_EXTRA, item)
            }
        }
    }
}