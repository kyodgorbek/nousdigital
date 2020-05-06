package com.imgdetails.views

import android.content.Intent
import android.content.Intent.createChooser
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.core.base.BaseActivity
import com.core.base.navigateTo
import com.core.extensions.toBitmap
import com.domain.models.Data
import com.imgdetails.R
import kotlinx.android.synthetic.main.activity_image_details.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.architecture.ext.viewModel


class ImageDetailsActivity : BaseActivity<ImageDetailsViewModel>() {
    override val viewModel by viewModel<ImageDetailsViewModel>()
    private val data by lazy { intent?.extras?.getParcelable<Data>(DATA_EXTRA) ?: Data() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)
        setStatusBarTransparent()

        setSupportActionBar(toolbar)
        setBackOnActionBarEnabled(true)
        setActionBarLeftButtonIcon(R.drawable.ic_close)
        toolbar.setBackgroundColor(Color.TRANSPARENT)
        title = ""

        initializeComponents()
    }

    private fun initializeComponents() {
        Glide.with(imgImageDetails)
            .load(data.image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    imgImageDetails.setBackgroundResource(R.drawable.not_found_image)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean = false

            })
            .into(imgImageDetails)

        txtTitleImageDetails.text = data.title
        txtDescriptionImageDetails.text = data.description

        btnSendEmailImageDetails.setOnClickListener { sendEmail() }
    }

    private fun sendEmail() {
        if (!permissionIsGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            doPermissionRequest(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return
        }

        Intent(Intent.ACTION_SEND).apply {
            val path = MediaStore.Images.Media.insertImage(
                contentResolver,
                if (imgImageDetails.drawable != null)
                    imgImageDetails.drawable.toBitmap()
                else
                    getDrawable(R.drawable.not_found_image)?.toBitmap()
                ,
                "",
                null
            )

            val uri = Uri.parse(path)

            type = "vnd.android.cursor.dir/email"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
            putExtra(Intent.EXTRA_SUBJECT, this@ImageDetailsActivity.data.title)
            putExtra(Intent.EXTRA_TEXT, this@ImageDetailsActivity.data.description)
            putExtra(Intent.EXTRA_STREAM, uri)

        }.run {
            viewModel.navigateTo<ImageDetailsActivity>(
                this@ImageDetailsActivity,
                createChooser(this, "Send mail...")
            )
        }
    }

    companion object {
        private const val DATA_EXTRA = "dataExtra"

        @JvmStatic
        fun params(data: Data): Bundle {
            return Bundle().apply {
                putParcelable(DATA_EXTRA, data)
            }
        }
    }
}