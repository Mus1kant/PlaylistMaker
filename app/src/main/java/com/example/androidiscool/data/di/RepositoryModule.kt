package com.example.androidiscool.data.di

import com.example.androidiscool.data.NetworkClient
import com.example.androidiscool.data.network.RetrofitNetworkClient
import com.example.androidiscool.data.repository.TracksRepositoryImpl
import com.example.androidiscool.data.storage.Storage
import com.example.androidiscool.domain.api.TracksRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<NetworkClient> { RetrofitNetworkClient() }
    single { Storage() }
    single<TracksRepository> { TracksRepositoryImpl(get(), get()) }
}
