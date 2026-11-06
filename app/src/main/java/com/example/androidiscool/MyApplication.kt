package com.example.androidiscool

import android.app.Application
import com.example.androidiscool.data.di.repositoryModule
import com.example.androidiscool.domain.api.TrackSearchInteractor
import com.example.androidiscool.domain.impl.TrackSearchInteractorImpl
import com.example.androidiscool.ui.view_model.SearchViewModel
import com.example.androidiscool.ui.view_model.TrackViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val domainModule = module {
            factory<TrackSearchInteractor> {
                TrackSearchInteractorImpl(get())
            }
        }

        val viewModelModule = module {
            viewModel {
                SearchViewModel(get())
            }

            viewModel {
                TrackViewModel(get())
            }
        }

        startKoin {
            androidContext(this@MyApplication)

            modules(
                repositoryModule,
                domainModule,
                viewModelModule
            )
        }
    }
}