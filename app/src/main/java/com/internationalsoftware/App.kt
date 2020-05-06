package com.internationalsoftware

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.internationalsoftware.di.dataModule
import com.internationalsoftware.di.domainModule
import com.internationalsoftware.di.viewModule
import org.koin.android.ext.android.startKoin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)


        // koin 1.0.0 will allow the application to load it's modules on activity creation
        // best approach would be to update it as soon as this new version gets stable
        // also, separate the modules by feature
        startKoin(
            this,
            listOf(
                dataModule,
                domainModule,
                viewModule
            )
        )
    }
}