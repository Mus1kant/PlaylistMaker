package com.example.androidiscool.data.di

import com.example.androidiscool.data.NetworkClient
import com.example.androidiscool.data.network.RetrofitNetworkClient
import com.example.androidiscool.data.network.TracksRepositoryImpl
import com.example.androidiscool.domain.api.TracksRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory<NetworkClient> {
        RetrofitNetworkClient()
    }

    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }
}